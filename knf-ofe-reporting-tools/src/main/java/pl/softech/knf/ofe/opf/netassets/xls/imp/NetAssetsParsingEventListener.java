package pl.softech.knf.ofe.opf.netassets.xls.imp;

import pl.softech.knf.ofe.shared.xls.parser.ParsingEventListener;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface NetAssetsParsingEventListener extends ParsingEventListener {

    void record(final String name, final double amount);

    void total(final double amount);

}
