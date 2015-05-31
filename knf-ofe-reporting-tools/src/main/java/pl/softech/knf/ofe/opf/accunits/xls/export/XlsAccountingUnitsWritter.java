package pl.softech.knf.ofe.opf.accunits.xls.export;

import org.apache.poi.ss.usermodel.Cell;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.xls.AbstractXlsWritter;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsAccountingUnitsWritter extends AbstractXlsWritter {


    public XlsAccountingUnitsWritter() {
        this.secondColumnName = "Accounting unit's value in zl";
        this.sheetName = "Accounting unit";
    }

    @Override
    protected void writeCell(Cell cell, OpenPensionFund fund) {
        cell.setCellValue(fund.getAccountingUnitValue().getValueAsDouble());
    }
}
