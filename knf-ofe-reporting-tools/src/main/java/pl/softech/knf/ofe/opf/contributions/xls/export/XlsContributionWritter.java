package pl.softech.knf.ofe.opf.contributions.xls.export;

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
public class XlsContributionWritter implements XlsWritter {

    @Override
    public void write(List<OpenPensionFund> funds, Workbook wb) {
        new ContributionAmount().write(funds, wb);
        new ContributionInterests().write(funds, wb);
        new ContributionNumber().write(funds, wb);
        new ContributionAverageBasis().write(funds, wb);
    }


    private static class ContributionAmount extends AbstractXlsWritter {

        ContributionAmount() {
            this.secondColumnName = "Amount of contribution in zl";
            this.sheetName = "Contribution - amount";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {
            cell.setCellValue((float) fund.getContribution().getAmount() / 100.0);
        }
    }

    private static class ContributionInterests extends AbstractXlsWritter {

        ContributionInterests() {
            this.secondColumnName = "Interests in zl";
            this.sheetName = "Contribution - interests";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {
            cell.setCellValue((float) fund.getContribution().getInterests() / 100.0);
        }
    }

    private static class ContributionNumber extends AbstractXlsWritter {

        ContributionNumber() {
            this.secondColumnName = "Number of contributions";
            this.sheetName = "Contribution - number";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {
            cell.setCellValue(fund.getContribution().getNumber());
        }
    }

    private static class ContributionAverageBasis extends AbstractXlsWritter {

        ContributionAverageBasis() {
            this.secondColumnName = "Average basis in zl";
            this.sheetName = "Contribution - average basis";
        }

        @Override
        protected void writeCell(Cell cell, OpenPensionFund fund) {
            cell.setCellValue((float) fund.getContribution().getAverageBasis() / 100.0);
        }
    }

}
