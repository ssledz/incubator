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

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import pl.softech.knf.ofe.InjectLogger;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.opf.event.DatabasePopulatorErrorEvent;
import pl.softech.knf.ofe.opf.event.LackOfDataEvent;
import pl.softech.knf.ofe.opf.event.ParserNotInEndingStateAfterFinish;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;
import pl.softech.knf.ofe.shared.task.Task;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundDbImportTask implements Task {

    @InjectLogger
    protected Logger logger;

    private final OpenPensionFundRepository jdbcRepository;
    private final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory;

    private boolean importFailed = false;

    private File currentPayload;

    @Inject
    public OpenPensionFundDbImportTask(@Jdbc final OpenPensionFundRepository jdbcRepository,
                                       final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory) {
        this.jdbcRepository = jdbcRepository;
        this.xlsRepositoryFactory = xlsRepositoryFactory;
    }

    @Subscribe
    public void listen(DatabasePopulatorErrorEvent event) {
        event.log(logger);
        importFailed = true;
    }

    @Subscribe
    public void listen(LackOfDataEvent event) {
        event.log(logger, currentPayload);
    }

    @Subscribe
    public void listen(ParserNotInEndingStateAfterFinish event) {
        event.log(logger, currentPayload);
    }

    @Override
    public void execute(final File payload) {
        logger.info("Importing {} ...", payload.getAbsoluteFile());
        currentPayload = payload;
        try {
            final XlsOpenPensionFundRepository xlsRepository = xlsRepositoryFactory.create(payload);
            final List<OpenPensionFund> funds = xlsRepository.findAll();
            jdbcRepository.save(funds);
            if (funds.isEmpty()) {
                logger.warn("No open pension funds found for {}", payload.getAbsoluteFile());
            } else if (!importFailed) {
                logger.info("Importing finished successfully for {}", payload.getAbsoluteFile());
            }
        } catch (final Exception e) {
            importFailed = true;
            logger.error("", e);
        }

        if (importFailed) {
            logger.error("Import was failed for {}", payload.getAbsoluteFile());
        }

    }

}
