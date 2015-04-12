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
package pl.softech.knf.ofe.opf.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.PoiException;
import pl.softech.knf.ofe.opf.xls.export.XlsOpenPensionFundOutput;
import pl.softech.knf.ofe.opf.xls.imp.ParsingEventListenerAdapter;
import pl.softech.knf.ofe.opf.xls.imp.XlsOpenPensionFundParser;
import pl.softech.knf.ofe.shared.jdbc.DataAccessException;

import com.google.inject.assistedinject.Assisted;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepository implements OpenPensionFundRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(XlsOpenPensionFundRepository.class);
	
	private static final String MEMBERS_SHEET_NAME = "I Members";
	private final File xlsFile;

	@Inject
	XlsOpenPensionFundRepository(final @Assisted File xlsFile) {
		this.xlsFile = xlsFile;
	}

	private Sheet loadSheet(final String name) {
		try (final InputStream inp = new FileInputStream(xlsFile)) {
			final Workbook wb = WorkbookFactory.create(inp);
			return wb.getSheet(name);
		} catch (final Exception e) {
			throw new PoiException(e);
		}
	}

	@Override
	public List<OpenPensionFund> findAll() {

		final Sheet members = loadSheet(MEMBERS_SHEET_NAME);

		final XlsOpenPensionFundParser parser = new XlsOpenPensionFundParser();

		final List<OpenPensionFund> funds = new LinkedList<>();

		parser.addParsingEventListener(new ParsingEventListenerAdapter() {

			private Date date;

			@Override
			public void date(final Date date) {
				this.date = date;
			}

			@Override
			public void record(final String name, final long numberOfMembers) {
				funds.add(new OpenPensionFund(name, numberOfMembers, date));
			}

		});

		parser.parseSheet(members);

		return funds;
	}

	private Workbook loadOrCreate(final File file) {
		if (xlsFile.exists()) {
			try (final InputStream inp = new FileInputStream(xlsFile)) {
				return WorkbookFactory.create(inp);
			} catch (final Exception e) {
				throw new PoiException(e);
			}
		}
		return new HSSFWorkbook();
	}

	@Override
	public void save(final List<OpenPensionFund> opfs) {

		final Workbook wb = loadOrCreate(xlsFile);

		try (FileOutputStream out = new FileOutputStream(xlsFile)) {

			String sheetName = MEMBERS_SHEET_NAME;
			Sheet sheet = wb.getSheet(sheetName);
			
			int it = 1;
			while (sheet != null) {
				sheetName = String.format("%s%d", MEMBERS_SHEET_NAME, it++);
				sheet = wb.getSheet(sheetName);
			}

			sheet = wb.createSheet(sheetName);

			final XlsOpenPensionFundOutput output = new XlsOpenPensionFundOutput();
			output.write(opfs, sheet);
			wb.write(out);
		} catch (final Exception e) {
			LOGGER.error("", e);
			new DataAccessException(e);
		}
	}

}
