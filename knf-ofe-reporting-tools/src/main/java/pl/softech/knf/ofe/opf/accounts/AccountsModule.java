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
import com.google.inject.multibindings.Multibinder;
import pl.softech.knf.ofe.opf.DataProvider;
import pl.softech.knf.ofe.opf.accounts.jdbc.AccountsDatabasePopulator;
import pl.softech.knf.ofe.opf.accounts.jdbc.AccountsRowMapper;
import pl.softech.knf.ofe.opf.accounts.xls.imp.AccountsProvider;
import pl.softech.knf.ofe.opf.jdbc.DatabasePopulator;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<DataProvider> dataProviderBinder = Multibinder.newSetBinder(binder(), DataProvider.class);
        dataProviderBinder.addBinding().to(AccountsProvider.class);

        Multibinder<DatabasePopulator> databasePopulatorBinder = Multibinder.newSetBinder(binder(), DatabasePopulator.class);
        databasePopulatorBinder.addBinding().to(AccountsDatabasePopulator.class);

        Multibinder<OpenPensionFundRowMapper> rowMapperBinder = Multibinder.newSetBinder(binder(), OpenPensionFundRowMapper.class);
        rowMapperBinder.addBinding().to(AccountsRowMapper.class);
    }

}
