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
package pl.softech.knf.ofe.opf.members.xls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
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
import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.shared.xls.PoiException;
import pl.softech.knf.ofe.opf.members.xls.export.XlsMembersOutput;
import pl.softech.knf.ofe.opf.members.xls.imp.MembersParsingEventListenerAdapter;
import pl.softech.knf.ofe.opf.members.xls.imp.XlsMembersParser;
import pl.softech.knf.ofe.shared.jdbc.DataAccessException;

import com.google.inject.assistedinject.Assisted;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsMembersRepository implements OpenPensionFundRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(XlsMembersRepository.class);

    private static final String MEMBERS_SHEET_NAME = "Members";
    private static final String MEMBERS_SHEET_NAME2 = "I Members";

    private final File xlsFile;
    private final OpenPensionFundNameTranslator nameTranslator;

    @Inject
    XlsMembersRepository(final @Assisted File xlsFile, OpenPensionFundNameTranslator nameTranslator) {
        this.xlsFile = xlsFile;
        this.nameTranslator = nameTranslator;
    }

    private Sheet loadSheet(final String name) {
        try (final InputStream inp = new FileInputStream(xlsFile)) {
            final Workbook wb = WorkbookFactory.create(inp);
            return wb.getSheet(name);
        } catch (final Exception e) {
            throw new PoiException(e);
        }
    }

    private static String[] args(final String... args) {
        return args;
    }

    @Override
    public List<OpenPensionFund> findAll() {

        Sheet members = loadSheet(MEMBERS_SHEET_NAME);

        if (members == null) {
            members = loadSheet(MEMBERS_SHEET_NAME2);
        }

        if (members == null) {
            LOGGER.warn("There is no '{}' or '{}' sheet in file {}", args(MEMBERS_SHEET_NAME, MEMBERS_SHEET_NAME2, xlsFile.getAbsolutePath()));
            return Collections.emptyList();
        }

        final XlsMembersParser parser = new XlsMembersParser();

        final List<OpenPensionFund> funds = new LinkedList<>();

        parser.addParsingEventListener(new MembersParsingEventListenerAdapter() {

            private Date date;

            @Override
            public void date(final Date date) {
                this.date = date;
            }

            @Override
            public void record(final String name, final long numberOfMembers) {
                funds.add(new OpenPensionFund(new OpenPensionFund.Builder()
                                .withName(nameTranslator.translate(name))
                                .withDate(date)
                                .withNumberOfMembers(numberOfMembers))
                );
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

            final XlsMembersOutput output = new XlsMembersOutput();
            output.write(opfs, sheet);
            wb.write(out);
        } catch (final Exception e) {
            LOGGER.error("", e);
            new DataAccessException(e);
        }
    }

}
