package pl.softech.knf.ofe.opf.investments.xls.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.investments.Investment;
import pl.softech.knf.ofe.opf.investments.jdbc.InstrumentRepository;
import pl.softech.knf.ofe.opf.xls.AbstractXlsWritter;
import pl.softech.knf.ofe.opf.xls.XlsWritter;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsInvestmentsWritter implements XlsWritter {

    private final InstrumentRepository instrumentRepository;

    @Inject
    public XlsInvestmentsWritter(InstrumentRepository instrumentRepository) {
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void write(List<OpenPensionFund> funds, Workbook wb) {

        instrumentRepository.findAll()
                .forEach(instrument -> {
                    new InvestmentByInstrument(instrument).write(funds, wb);
                });


    }

    private static class InvestmentByInstrument extends AbstractXlsWritter {

        private final Instrument instrument;

        public InvestmentByInstrument(Instrument instrument) {
            this.instrument = instrument;
            this.secondColumnName = instrument.getName();
            this.sheetName = "Investment By instrument " + instrument.getId();
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {

            Optional<Investment> investment = fund.getInvestmens()
                    .stream()
                    .filter(inv -> inv.getInstrument().equals(instrument))
                    .findFirst();

            if (investment.isPresent()) {
                cell.setCellValue((float) investment.get().getValue() / 100.0);
            } else {
                cell.setCellValue(0);
            }


        }
    }
}
