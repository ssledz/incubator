package pl.softech.knf.ofe.opf.investments.xls.imp;

import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.shared.xls.parser.ParsingEventListener;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface InvestmentsParsingEventListener extends ParsingEventListener {

    void record(Instrument instrument, String openPensionFundName, final double investmentValue);

    void total(Instrument instrument, final double totalInvestmentValue);

    void total(String openPensionFundName, final double totalInvestmentValue);

}
