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
package pl.softech.knf.ofe;

import javax.sql.DataSource;

import pl.softech.knf.ofe.opf.OpenPensionFundModule;
import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.SimpleOpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.accounts.AccountsModule;
import pl.softech.knf.ofe.opf.members.MembersModule;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {

        bindConstant()
                .annotatedWith(JdbcConnectionUrl.class)
                .to("jdbc:mysql://localhost:3306/knf_ofe?createDatabaseIfNotExist=true&sessionVariables=sql_mode=NO_BACKSLASH_ESCAPES&useUnicode=yes&characterEncoding=UTF-8&characterSetResults=utf8&connectionCollation=utf8_unicode_ci");
        bindConstant().annotatedWith(JdbcUser.class).to("test");
        bindConstant().annotatedWith(JdbcPassword.class).to("test");

        bind(JdbcTemplate.class).in(Singleton.class);

        install(new OpenPensionFundModule());

    }

    @Provides
    public DataSource provideDataSource(@JdbcUser final String user, @JdbcPassword final String password,
                                        @JdbcConnectionUrl final String url) {
        final ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(url);
        cpds.setUser(user);
        cpds.setPassword(password);
        return cpds;
    }

}
