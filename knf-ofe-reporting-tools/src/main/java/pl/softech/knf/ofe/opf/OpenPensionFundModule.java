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

import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.jdbc.JdbcOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(OpenPensionFundRepository.class).annotatedWith(Jdbc.class).to(JdbcOpenPensionFundRepository.class).in(Singleton.class);
		
		install(new FactoryModuleBuilder().implement(OpenPensionFundRepository.class, Xls.class, XlsOpenPensionFundRepository.class).build(
				XlsOpenPensionFundRepositoryFactory.class));
		
		bind(OpenPensionFundDbImportTask.class).toProvider(OpenPensionFundDbImportTaskProvider.class);
		bind(OpenPensionFundDbExportTask.class).toProvider(OpenPensionFundDbExportTaskProvider.class);

		bind(OpenPensionFundNameTranslator.class).to(XlsOpenPensionFundNameTranslator.class);
	}

}
