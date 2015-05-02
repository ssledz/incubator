package pl.softech.knf.ofe.opf.accounts.xls.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.xls.AbstractXlsWritter;
import pl.softech.knf.ofe.opf.xls.XlsWritter;

import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsAccountsWritter implements XlsWritter {

    @Override
    public void write(List<OpenPensionFund> funds, Workbook wb) {

        TotalNumberOfAccounts total = new TotalNumberOfAccounts();
        InactiveNumberOfAccounts inactive = new InactiveNumberOfAccounts();

        total.write(funds, wb);
        inactive.write(funds, wb);

    }


    private static class TotalNumberOfAccounts extends AbstractXlsWritter {

        TotalNumberOfAccounts() {
            this.secondColumnName = "Total Number of Accounts";
            this.sheetName = "Accounts - total";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {
            cell.setCellValue(fund.getNumberOfAccounts().getTotal());
        }
    }

    private static class InactiveNumberOfAccounts extends AbstractXlsWritter {

        InactiveNumberOfAccounts() {
            this.secondColumnName = "Inactive Number of Accounts";
            this.sheetName = "Accounts - inactive";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {
            cell.setCellValue(fund.getNumberOfAccounts().getInactive());
        }
    }

}
