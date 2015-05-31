package pl.softech.knf.ofe.opf.accunits.xls.imp;

import pl.softech.knf.ofe.shared.xls.parser.ParsingEventListener;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface AccountingUnitsParsingEventListener extends ParsingEventListener {

    void record(final String name, final double accountingUnitValue);

    void total(final double accountingUnitValue);

}
