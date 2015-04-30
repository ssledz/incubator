package pl.softech.knf.ofe.opf.members.xls.imp;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.softech.knf.ofe.opf.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class MembersProvider implements DataProvider {

    private static final String MEMBERS_SHEET_NAME = "Members";
    private static final String MEMBERS_SHEET_NAME2 = "I Members";

    private static final Logger LOGGER = LoggerFactory.getLogger(MembersProvider.class);

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

        Sheet sheet = wb.getSheet(MEMBERS_SHEET_NAME);

        if (sheet == null) {
            sheet = wb.getSheet(MEMBERS_SHEET_NAME2);
        }

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
