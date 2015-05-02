package pl.softech.knf.ofe.opf.xls;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import pl.softech.knf.ofe.opf.OpenPensionFund;

import java.util.*;

import static java.util.Objects.*;

import static pl.softech.knf.ofe.shared.xls.XlsUtils.createHeaderFont;
import static pl.softech.knf.ofe.shared.xls.XlsUtils.createSheet;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public abstract class AbstractXlsWritter implements XlsWritter {

    protected String firstColumnName = "Open Pension Fund";
    protected String secondColumnName;
    protected String sheetName;

    @Override
    public void write(List<OpenPensionFund> funds, Workbook wb) {

        requireNonNull(firstColumnName);
        requireNonNull(secondColumnName);
        requireNonNull(sheetName);

        Sheet sheet = createSheet(wb, sheetName);

        int rowIdx = 1;
        final int colIdx = 1;

        WritterHelper helper = new WritterHelper(funds);

        HeaderBuilder headerBuilder = new HeaderBuilder();
        headerBuilder
                .withColumn(colIdx)
                .withRow(rowIdx)
                .withDateFormat("mm-yyyy")
                .withFirstColumnName(firstColumnName)
                .withSecondColumnName(secondColumnName)
                .withDates(helper.allDates)
                .build(sheet);
        rowIdx += 2;

        for (final Map.Entry<String, Map<Date, OpenPensionFund>> e : helper.name2funds.entrySet()) {

            final Row row = sheet.createRow(rowIdx++);
            Cell cell = row.createCell(colIdx);
            cell.setCellValue(e.getKey());

            int colIt = colIdx + 1;
            final Map<Date, OpenPensionFund> date2fund = e.getValue();
            for (final Date date : helper.allDates) {
                final OpenPensionFund fund = date2fund.get(date);
                cell = row.createCell(colIt++);
                if (fund == null) {
                    continue;
                }
                writeCell(cell, fund);
            }

        }
    }

    protected abstract void writeCell(Cell cell, OpenPensionFund fund);

    private static class HeaderBuilder {

        private String dateFormat;
        private String firstColumnName;
        private String secondColumnName;

        private int rowIdx;
        private int colIdx;

        private List<Date> dates;

        public HeaderBuilder withDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
            return this;
        }

        public HeaderBuilder withFirstColumnName(String firstColumnName) {
            this.firstColumnName = firstColumnName;
            return this;
        }

        public HeaderBuilder withSecondColumnName(String secondColumnName) {
            this.secondColumnName = secondColumnName;
            return this;
        }

        public HeaderBuilder withRow(int row) {
            this.rowIdx = row;
            return this;
        }

        public HeaderBuilder withColumn(int column) {
            this.colIdx = column;
            return this;
        }

        public HeaderBuilder withDates(List<Date> dates) {
            this.dates = dates;
            return this;
        }

        public void build(final Sheet sheet) {
            final Workbook wb = sheet.getWorkbook();
            final CreationHelper createHelper = wb.getCreationHelper();
            final CellStyle dateCellStyle = wb.createCellStyle();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dateFormat));

            Row row = sheet.createRow(rowIdx);

            Cell cell = row.createCell(colIdx);
            cell.setCellValue(firstColumnName);

            final CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyle.setFont(createHeaderFont(wb, (short) 12, "Arial"));
            cell.setCellStyle(cellStyle);

            cell = row.createCell(colIdx + 1);
            cell.setCellValue(secondColumnName);
            cell.setCellStyle(cellStyle);
            row = sheet.createRow(rowIdx + 1);
            sheet.addMergedRegion(new CellRangeAddress(// merge Open Pension Fund
                    rowIdx, // first row (0-based)
                    rowIdx + 1, // last row (0-based)
                    colIdx, // first column (0-based)
                    colIdx // last column (0-based)
            ));

            sheet.addMergedRegion(new CellRangeAddress(// merge Number of members
                    rowIdx, // first row (0-based)
                    rowIdx, // last row (0-based)
                    colIdx + 1, // first column (0-based)
                    colIdx + dates.size() // last column (0-based)
            ));

            int colIt = colIdx + 1;
            for (final Date date : dates) {
                cell = row.createCell(colIt++);
                cell.setCellValue(date);
                cell.setCellStyle(dateCellStyle);
            }
        }
    }

    protected static class WritterHelper {

        final List<Date> allDates = new ArrayList<>();

        final Map<String, Map<Date, OpenPensionFund>> name2funds = new TreeMap<>();

        WritterHelper(List<OpenPensionFund> funds) {

            final Set<Date> dates = new HashSet<>();

            funds.stream().forEach(fund -> {
                dates.add(fund.getDate());
                Map<Date, OpenPensionFund> map = name2funds.get(fund.getName());
                if (map == null) {
                    map = new HashMap<>();
                    name2funds.put(fund.getName(), map);
                }
                map.put(fund.getDate(), fund);
            });

            allDates.addAll(dates);
            Collections.sort(allDates);
        }

    }
}
