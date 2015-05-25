package pl.softech.knf.ofe.opf.investments;

import pl.softech.knf.ofe.opf.OpenPensionFundAbstractModule;
import pl.softech.knf.ofe.opf.investments.jdbc.InstrumentRepository;
import pl.softech.knf.ofe.opf.investments.jdbc.InvestmentsDatabasePopulator;
import pl.softech.knf.ofe.opf.investments.jdbc.InvestmentsRowMapper;
import pl.softech.knf.ofe.opf.investments.xls.export.XlsInvestmentsWritter;
import pl.softech.knf.ofe.opf.investments.xls.imp.InvestmentsProvider;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsModule extends OpenPensionFundAbstractModule {

    @Override
    protected void configure() {

        bind(InstrumentFactory.class).to(SimpleInstrumentFactory.class);
        bind(InstrumentRepository.class);

        bindDataProviders(InvestmentsProvider.class);
        bindDatabasePopulators(InvestmentsDatabasePopulator.class);
        bindOpenPensionFundRowMappers(InvestmentsRowMapper.class);
        bindXlsWritters(XlsInvestmentsWritter.class);

    }
}
