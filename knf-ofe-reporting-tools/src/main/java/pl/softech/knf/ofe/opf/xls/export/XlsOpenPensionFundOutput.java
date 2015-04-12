/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe.opf.xls.export;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import pl.softech.knf.ofe.opf.OpenPensionFund;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundOutput {

	public void write(final List<OpenPensionFund> funds, final Sheet sheet) {

		int rowIdx = 1;
		final int colIdx = 1;

		final Set<Date> allDates = new HashSet<>();
		final Map<String, Map<Date, OpenPensionFund>> name2funds = new TreeMap<>();

		funds.stream().forEach(fund -> {
			allDates.add(fund.getDate());
			Map<Date, OpenPensionFund> map = name2funds.get(fund.getName());
			if (map == null) {
				map = new HashMap<>();
				name2funds.put(fund.getName(), map);
			}
			map.put(fund.getDate(), fund);
		});

		final List<Date> dates = new ArrayList<>(allDates);
		Collections.sort(dates);

		buildHeader(dates, sheet, rowIdx, colIdx);
		rowIdx += 2;

		for (final Map.Entry<String, Map<Date, OpenPensionFund>> e : name2funds.entrySet()) {

			final Row row = sheet.createRow(rowIdx++);
			Cell cell = row.createCell(colIdx);
			cell.setCellValue(e.getKey());

			int colIt = colIdx + 1;
			final Map<Date, OpenPensionFund> date2fund = e.getValue();
			for (final Date date : dates) {
				final OpenPensionFund fund = date2fund.get(date);
				cell = row.createCell(colIt++);
				cell.setCellValue(fund.getNumberOfMembers());
			}

		}

	}

	private Font createHeaderFont(final Workbook wb, final short size) {
		final Font font = wb.createFont();
		font.setFontHeightInPoints(size);
		font.setFontName("Arial");
		font.setBold(true);
		return font;
	}

	private void buildHeader(final List<Date> dates, final Sheet sheet, final int rowIdx, final int colIdx) {

		final Workbook wb = sheet.getWorkbook();
		final CreationHelper createHelper = wb.getCreationHelper();
		final CellStyle dateCellStyle = wb.createCellStyle();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-mm-yyyy"));

		Row row = sheet.createRow(rowIdx);

		Cell cell = row.createCell(colIdx);
		cell.setCellValue("Open Pension Fund");

		final CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setFont(createHeaderFont(wb, (short) 12));
		cell.setCellStyle(cellStyle);

		cell = row.createCell(colIdx + 1);
		cell.setCellValue("Number of members");
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

	private static List<OpenPensionFund> createMocks(final String name) throws Exception {
		final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		final List<OpenPensionFund> funds = new LinkedList<>();
		funds.add(new OpenPensionFund(name, 1, df.parse("01-01-2015")));
		funds.add(new OpenPensionFund(name, 2, df.parse("01-02-2015")));
		funds.add(new OpenPensionFund(name, 3, df.parse("01-03-2015")));
		funds.add(new OpenPensionFund(name, 4, df.parse("01-04-2015")));
		funds.add(new OpenPensionFund(name, 5, df.parse("01-05-2015")));
		return funds;
	}

	public static void main(final String[] args) throws Exception {

		final List<OpenPensionFund> funds = new LinkedList<>();
		funds.addAll(createMocks("AIG OFE"));
		funds.addAll(createMocks("OFE Allianz Polska"));
		funds.addAll(createMocks("Bankowy OFE"));
		funds.addAll(createMocks("Commercial Union OFE BPH CU WBK"));

		try (FileOutputStream out = new FileOutputStream(new File("/home/ssledz/knf-ofe-work-dir/work", "workbook.xls"))) {
			final Workbook wb = new HSSFWorkbook();
			final Sheet sheet = wb.createSheet("I Members");
			final XlsOpenPensionFundOutput output = new XlsOpenPensionFundOutput();
			output.write(funds, sheet);
			wb.write(out);
			wb.close();
		}
	}

}
