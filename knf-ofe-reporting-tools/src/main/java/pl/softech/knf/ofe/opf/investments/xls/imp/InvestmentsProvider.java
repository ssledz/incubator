package pl.softech.knf.ofe.opf.investments.xls.imp;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundDateAdjuster;
import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.investments.InstrumentFactory;
import pl.softech.knf.ofe.opf.investments.Investment;
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
public class InvestmentsProvider implements DataProvider {

    private static final String[] SHEET_NAMES = {
            "Portfel inwestycyjny OFE",
            " Portfel inwestycyjny OFE",
            "Porfel inwestycyjny",
            "V  Portfel inwestycyjny OFE"
    };

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;
    private final InstrumentFactory instrumentFactory;

    @Inject
    public InvestmentsProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster, InstrumentFactory
            instrumentFactory) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
        this.instrumentFactory = instrumentFactory;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        InvestmentsPortfolioParser parser = new InvestmentsPortfolioParser(instrumentFactory);

        final Map<String, OpenPensionFund.Builder> name2fundBuilder = new HashMap<>();

        parser.addParsingEventListener(new InvestmentsParsingEventListenerAdapter() {

            private Date date;

            @Override
            public void record(Instrument instrument, String openPensionFundName, double investmentValue) {

                String opfName = nameTranslator.translate(openPensionFundName);

                OpenPensionFund.Builder fundBuilder = name2fundBuilder.get(opfName);

                if (fundBuilder == null) {
                    fundBuilder = new OpenPensionFund.Builder()
                            .withDate(date)
                            .withName(opfName);
                    name2fundBuilder.put(opfName, fundBuilder);
                }

                fundBuilder.addInvestment(new Investment(instrument, Math.round(investmentValue * 100)));

            }

            @Override
            public void date(Date date) {
                this.date = dateAdjuster.adjust(date);
            }
        });

        Sheet sheet = null;

        for (String sheetName : SHEET_NAMES) {
            sheet = wb.getSheet(sheetName);
            if (sheet != null) {
                break;
            }
        }

        if (sheet == null) {
            return new ArrayList<DataPopulator>().iterator();
        }

        parser.parseSheet(sheet);

        return name2fundBuilder.values()
                .stream()
                .map(b -> new InvestmentsDataPopulator(new OpenPensionFund(b)))
                .collect(Collectors.toList())
                .iterator();
    }

    private static class InvestmentsDataPopulator extends AbstractDataPopulator {

        public InvestmentsDataPopulator(OpenPensionFund fund) {
            super(fund);
        }

        @Override
        public void populate(OpenPensionFund.Builder builder) {
            Collection<Investment> investments = fund.getInvestmens();
            builder.addInvestments(investments);
        }
    }
}
