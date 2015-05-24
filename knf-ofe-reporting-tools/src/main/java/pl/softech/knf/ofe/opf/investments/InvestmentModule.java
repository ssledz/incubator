package pl.softech.knf.ofe.opf.investments;

import pl.softech.knf.ofe.opf.OpenPensionFundAbstractModule;
import pl.softech.knf.ofe.opf.investments.xls.imp.InvestmentsProvider;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentModule extends OpenPensionFundAbstractModule {

    @Override
    protected void configure() {

        bind(InstrumentFactory.class).to(SimpleInstrumentFactory.class);

        bindDataProviders(InvestmentsProvider.class);
//        bindDatabasePopulators(InvestmentsDatabasePopulator.class);
//        bindOpenPensionFundRowMappers(InvestmentsRowMapper.class);
//        bindXlsWritters(XlsInvestmentsWritter.class);

    }
}
