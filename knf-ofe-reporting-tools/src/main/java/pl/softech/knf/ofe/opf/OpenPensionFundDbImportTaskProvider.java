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

import com.google.inject.Provider;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;

import javax.inject.Inject;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundDbImportTaskProvider implements Provider<OpenPensionFundDbImportTask> {

    private final OpenPensionFundRepository jdbcRepository;
    private final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory;

    @Inject
    OpenPensionFundDbImportTaskProvider(@Jdbc final OpenPensionFundRepository jdbcRepository,
                                        final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory) {
        this.jdbcRepository = jdbcRepository;
        this.xlsRepositoryFactory = xlsRepositoryFactory;
    }

    @Override
    public OpenPensionFundDbImportTask get() {
        return new OpenPensionFundDbImportTask(jdbcRepository, xlsRepositoryFactory);
    }

}
