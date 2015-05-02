package pl.softech.knf.ofe.opf.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface DatabasePopulator {
    void populate(OpenPensionFund fund);
}
