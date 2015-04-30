package pl.softech.knf.ofe.opf;

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
