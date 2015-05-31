package pl.softech.knf.ofe.opf.netassets.xls.export;

import org.apache.poi.ss.usermodel.Cell;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.xls.AbstractXlsWritter;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsNetAssetsWritter extends AbstractXlsWritter {

    public XlsNetAssetsWritter() {
        this.secondColumnName = "Net assets in zl";
        this.sheetName = "Net assets";
    }

    @Override
    protected void writeCell(Cell cell, OpenPensionFund fund) {
        cell.setCellValue(fund.getNetAssets().getValueAsDouble());
    }
}
