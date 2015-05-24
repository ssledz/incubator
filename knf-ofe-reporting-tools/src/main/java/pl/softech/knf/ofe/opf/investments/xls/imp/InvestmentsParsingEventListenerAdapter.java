package pl.softech.knf.ofe.opf.investments.xls.imp;

import pl.softech.knf.ofe.opf.investments.Instrument;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsParsingEventListenerAdapter implements InvestmentsParsingEventListener {
    @Override
    public void record(Instrument instrument, String openPensionFundName, double investmentValue) {

    }

    @Override
    public void total(Instrument instrument, double totalInvestmentValue) {

    }

    @Override
    public void total(String openPensionFundName, double totalInvestmentValue) {

    }

    @Override
    public void date(Date date) {

    }

    @Override
    public void header(String[] columns) {

    }
}
