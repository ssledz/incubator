package pl.softech.knf.ofe.opf.accunits;

import pl.softech.knf.ofe.opf.OpenPensionFundAbstractModule;
import pl.softech.knf.ofe.opf.accunits.jdbc.AccountingUnitsDatabasePopulator;
import pl.softech.knf.ofe.opf.accunits.jdbc.AccountingUnitsRowMapper;
import pl.softech.knf.ofe.opf.accunits.xls.export.XlsAccountingUnitsWritter;
import pl.softech.knf.ofe.opf.accunits.xls.imp.AccountingUnitsProvider;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountingUnitsModule extends OpenPensionFundAbstractModule {

    @Override
    protected void configure() {
        bindDataProviders(AccountingUnitsProvider.class);
        bindDatabasePopulators(AccountingUnitsDatabasePopulator.class);
        bindOpenPensionFundRowMappers(AccountingUnitsRowMapper.class);
        bindXlsWritters(XlsAccountingUnitsWritter.class);
    }
}
