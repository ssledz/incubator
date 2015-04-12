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
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.PoiException;

import com.google.inject.assistedinject.Assisted;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepository implements OpenPensionFundRepository {

	private static final String MEMBERS_SHEET_NAME = "I Members";
	private final File xlsFile;

	@Inject
	public XlsOpenPensionFundRepository(final @Assisted File xlsFile) {
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

	@Override
	public void save(final OpenPensionFund opf) {
		throw new UnsupportedOperationException();
	}

}
