package pl.softech.knf.ofe.opf.contributions.xls.imp;

import com.google.common.eventbus.EventBus;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundDateAdjuster;
import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.contributions.Contribution;
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
public class ContributionProvider implements DataProvider {

    private static final Pattern SHEET_NAME_PATTERN = Pattern.compile(".*\\s?Contributions\\s?.*", Pattern.CASE_INSENSITIVE);

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;
    private final EventBus eventBus;

    @Inject
    public ContributionProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster, EventBus eventBus) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
        this.eventBus = eventBus;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        XlsContributionParser parser = new XlsContributionParser(eventBus);

        List<OpenPensionFund> funds = new LinkedList<>();

        parser.addParsingEventListener(new ContributionParsingEventListenerAdapter() {

            private Date date;

            @Override
            public void date(Date date) {
                this.date = dateAdjuster.adjust(date);
            }

            @Override
            public void record(String name, double amount, double interests, long number, double averageBasis) {
                funds.add(new OpenPensionFund(new OpenPensionFund.Builder()
                        .withDate(date)
                        .withName(nameTranslator.translate(name))
                        .withContribution(new Contribution.Builder()
                                        .withAmount(amount)
                                        .withInterests(interests)
                                        .withNumber(number)
                                        .withAverageBasis(averageBasis)
                        )
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
                .map(f -> new ContributionDataPopulator(f))
                .collect(Collectors.toList())
                .iterator();
    }

    private static class ContributionDataPopulator extends AbstractDataPopulator {

        public ContributionDataPopulator(OpenPensionFund fund) {
            super(fund);
        }

        @Override
        public void populate(OpenPensionFund.Builder builder) {
            Contribution contribution = fund.getContribution();
            builder.withContribution(contribution.toBuilder());
        }
    }
}
