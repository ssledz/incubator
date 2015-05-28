package pl.softech.knf.ofe.opf.members.xls.imp;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.*;
import pl.softech.knf.ofe.opf.xls.AbstractDataPopulator;
import pl.softech.knf.ofe.opf.xls.DataPopulator;
import pl.softech.knf.ofe.opf.xls.DataProvider;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static pl.softech.knf.ofe.shared.xls.XlsUtils.getSheetByPattern;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class MembersProvider implements DataProvider {

    private static final Pattern SHEET_NAME_PATTERN = Pattern.compile(".*\\s?Members\\s?.*", Pattern.CASE_INSENSITIVE);

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;

    @Inject
    public MembersProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
    }

    private static String[] args(final String... args) {
        return args;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        XlsMembersParser parser = new XlsMembersParser();

        List<OpenPensionFund> funds = new LinkedList<>();

        parser.addParsingEventListener(new MembersParsingEventListenerAdapter() {

            private Date date;

            @Override
            public void date(Date date) {
                this.date = dateAdjuster.adjust(date);
            }

            @Override
            public void record(String name, long numberOfMembers) {
                funds.add(new OpenPensionFund(new OpenPensionFund.Builder()
                        .withDate(date)
                        .withName(nameTranslator.translate(name))
                        .withNumberOfMembers(numberOfMembers)
                ));
            }
        });

        Sheet sheet = getSheetByPattern(wb, SHEET_NAME_PATTERN);

        if (sheet == null) {
            return new ArrayList<DataPopulator>().iterator();
        }

        parser.parseSheet(sheet);

        return funds
                .stream()
                .map(f -> new MembersDataPopulator(f))
                .collect(Collectors.toList())
                .iterator();
    }

    private static class MembersDataPopulator extends AbstractDataPopulator {

        public MembersDataPopulator(OpenPensionFund fund) {
            super(fund);
        }

        @Override
        public void populate(OpenPensionFund.Builder builder) {
            builder.withNumberOfMembers(fund.getNumberOfMembers());
        }
    }
}
