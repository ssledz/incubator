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

import org.slf4j.Logger;
import pl.softech.knf.ofe.InjectLogger;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;
import pl.softech.knf.ofe.shared.task.Task;

import javax.inject.Inject;
import java.io.File;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundDbExportTask implements Task {

    @InjectLogger
    protected Logger logger;

    private final OpenPensionFundRepository jdbcRepository;
    private final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory;

    @Inject
    public OpenPensionFundDbExportTask(@Jdbc final OpenPensionFundRepository jdbcRepository,
                                       final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory) {
        this.jdbcRepository = jdbcRepository;
        this.xlsRepositoryFactory = xlsRepositoryFactory;
    }

    @Override
    public void execute(final File payload) {
        final XlsOpenPensionFundRepository xlsRepository = xlsRepositoryFactory.create(payload);
        xlsRepository.save(jdbcRepository.findAll());
    }

}
