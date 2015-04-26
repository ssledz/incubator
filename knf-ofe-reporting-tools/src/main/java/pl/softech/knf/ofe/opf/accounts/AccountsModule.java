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
package pl.softech.knf.ofe.opf.accounts;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.SimpleOpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.members.MembersDbExportTask;
import pl.softech.knf.ofe.opf.members.MembersDbExportTaskProvider;
import pl.softech.knf.ofe.opf.members.MembersDbImportTask;
import pl.softech.knf.ofe.opf.members.MembersDbImportTaskProvider;
import pl.softech.knf.ofe.opf.members.jdbc.JdbcMembersRepository;
import pl.softech.knf.ofe.opf.members.xls.XlsMembersRepository;
import pl.softech.knf.ofe.opf.members.xls.XlsMembersRepositoryFactory;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsModule extends AbstractModule {

	@Override
	protected void configure() {


	}

}
