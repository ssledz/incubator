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
package pl.softech.knf.ofe.opf.members;

import java.io.File;

import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.members.xls.XlsMembersRepository;
import pl.softech.knf.ofe.opf.members.xls.XlsMembersRepositoryFactory;
import pl.softech.knf.ofe.shared.task.Task;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class MembersDbExportTask implements Task {

	private final OpenPensionFundRepository jdbcRepository;
	private final XlsMembersRepositoryFactory xlsRepositoryFactory;

	public MembersDbExportTask(final OpenPensionFundRepository jdbcRepository,
							   final XlsMembersRepositoryFactory xlsRepositoryFactory) {
		this.jdbcRepository = jdbcRepository;
		this.xlsRepositoryFactory = xlsRepositoryFactory;
	}

	@Override
	public void execute(final File payload) {
		final XlsMembersRepository xlsRepository = xlsRepositoryFactory.create(payload);
		xlsRepository.save(jdbcRepository.findAll());
	}

}
