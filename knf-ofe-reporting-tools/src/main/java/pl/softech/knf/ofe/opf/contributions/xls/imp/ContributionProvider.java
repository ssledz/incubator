package pl.softech.knf.ofe.opf.contributions.xls.imp;

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
import java.util.stream.Collectors;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class ContributionProvider implements DataProvider {

    private static final String CONTRIBUTION_SHEET_NAME = "Contributions";
    private static final String CONTRIBUTION_SHEET_NAME2 = "III Contributions";

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;

    @Inject
    public ContributionProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        XlsContributionParser parser = new XlsContributionParser();

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
                                        .withAmount((long) (amount * 100))
                                        .withInterests((long) (interests * 100))
                                        .withNumber(number)
                                        .withAverageBasis((long) (averageBasis * 100))
                        )
                ));
            }

        });

        Sheet sheet = wb.getSheet(CONTRIBUTION_SHEET_NAME);

        if (sheet == null) {
            sheet = wb.getSheet(CONTRIBUTION_SHEET_NAME2);
        }

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
