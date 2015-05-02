package pl.softech.knf.ofe.opf.xls;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.xls.DataPopulator;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public abstract class AbstractDataPopulator implements DataPopulator {

    protected final OpenPensionFund fund;

    public AbstractDataPopulator(OpenPensionFund fund) {
        this.fund = fund;
    }

    @Override
    public OpenPensionFund.Key key() {
        return fund.getKey();
    }

}
