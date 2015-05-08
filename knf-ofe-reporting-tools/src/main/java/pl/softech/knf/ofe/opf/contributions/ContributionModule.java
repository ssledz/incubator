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
package pl.softech.knf.ofe.opf.contributions;

import pl.softech.knf.ofe.opf.OpenPensionFundAbstractModule;
import pl.softech.knf.ofe.opf.contributions.jdbc.ContributionDatabasePopulator;
import pl.softech.knf.ofe.opf.contributions.jdbc.ContributionRowMapper;
import pl.softech.knf.ofe.opf.contributions.xls.export.XlsContributionWritter;
import pl.softech.knf.ofe.opf.contributions.xls.imp.ContributionProvider;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class ContributionModule extends OpenPensionFundAbstractModule {

    @Override
    protected void configure() {
        bindDataProviders(ContributionProvider.class);
        bindDatabasePopulators(ContributionDatabasePopulator.class);
        bindOpenPensionFundRowMappers(ContributionRowMapper.class);
        bindXlsWritters(XlsContributionWritter.class);
    }

}
