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
package pl.softech.knf.ofe.opf;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;
import pl.softech.knf.ofe.shared.task.Task;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundDbImportTask implements Task {

	private static Logger LOGGER = LoggerFactory.getLogger(OpenPensionFundDbImportTask.class);

	private final OpenPensionFundRepository jdbcRepository;
	private final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory;

	public OpenPensionFundDbImportTask(final OpenPensionFundRepository jdbcRepository,
			final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory) {
		this.jdbcRepository = jdbcRepository;
		this.xlsRepositoryFactory = xlsRepositoryFactory;
	}

	@Override
	public void execute(final File payload) {
		LOGGER.info("Importing...");
		try {
			final XlsOpenPensionFundRepository xlsRepository = xlsRepositoryFactory.create(payload);
			jdbcRepository.save(xlsRepository.findAll());
			LOGGER.info("Importing finished successfully for {}", payload.getAbsoluteFile());
		} catch (final Exception e) {
			LOGGER.error(String.format("Import was failed for %s", payload.getAbsoluteFile()), e);
		}
		
	}

}
