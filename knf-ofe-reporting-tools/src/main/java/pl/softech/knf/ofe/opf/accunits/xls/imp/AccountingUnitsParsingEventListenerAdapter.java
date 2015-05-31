package pl.softech.knf.ofe.opf.accunits.xls.imp;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountingUnitsParsingEventListenerAdapter implements AccountingUnitsParsingEventListener {
    @Override
    public void record(String name, double accountingUnitValue) {
        
    }

    @Override
    public void total(double accountingUnitValue) {

    }

    @Override
    public void date(Date date) {

    }

    @Override
    public void header(String[] columns) {

    }
}
