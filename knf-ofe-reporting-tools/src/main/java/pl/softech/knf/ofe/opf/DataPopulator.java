package pl.softech.knf.ofe.opf;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface DataPopulator {

    OpenPensionFund.Key key();

    void populate(OpenPensionFund.Builder builder);

}
