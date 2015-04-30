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

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.DataProvider;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.accounts.xls.imp.AccountsProvider;
import pl.softech.knf.ofe.opf.members.jdbc.JdbcMembersRepository;
import pl.softech.knf.ofe.opf.members.xls.XlsMembersRepository;
import pl.softech.knf.ofe.opf.members.xls.XlsMembersRepositoryFactory;
import pl.softech.knf.ofe.opf.members.xls.imp.MembersProvider;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class MembersModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(OpenPensionFundRepository.class).annotatedWith(Jdbc.class).to(JdbcMembersRepository.class).in(Singleton.class);

        install(new FactoryModuleBuilder().implement(OpenPensionFundRepository.class, Xls.class, XlsMembersRepository.class).build(
                XlsMembersRepositoryFactory.class));

        bind(MembersDbImportTask.class).toProvider(MembersDbImportTaskProvider.class);
        bind(MembersDbExportTask.class).toProvider(MembersDbExportTaskProvider.class);

        Multibinder<DataProvider> dataProviderBinder = Multibinder.newSetBinder(binder(), DataProvider.class);
        dataProviderBinder.addBinding().to(MembersProvider.class);

    }

}
