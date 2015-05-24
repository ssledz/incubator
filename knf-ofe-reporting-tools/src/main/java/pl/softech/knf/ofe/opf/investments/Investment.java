package pl.softech.knf.ofe.opf.investments;

import pl.softech.knf.ofe.opf.OpenPensionFund;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class Investment {

    private final OpenPensionFund openPensionFund;
    private final Instrument instrument;
    private final long value;

    public Investment(OpenPensionFund openPensionFund, Instrument instrument, long value) {
        this.openPensionFund = openPensionFund;
        this.instrument = instrument;
        this.value = value;
    }

    public OpenPensionFund getOpenPensionFund() {
        return openPensionFund;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public long getValue() {
        return value;
    }
}
