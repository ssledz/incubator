package pl.softech.knf.ofe.opf.netassets;

import pl.softech.knf.ofe.opf.OpenPensionFundAbstractModule;
import pl.softech.knf.ofe.opf.netassets.jdbc.NetAssetsDatabasePopulator;
import pl.softech.knf.ofe.opf.netassets.jdbc.NetAssetsRowMapper;
import pl.softech.knf.ofe.opf.netassets.xls.export.XlsNetAssetsWritter;
import pl.softech.knf.ofe.opf.netassets.xls.imp.NetAssetsProvider;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class NetAssetsModule extends OpenPensionFundAbstractModule {

    @Override
    protected void configure() {

        bindDataProviders(NetAssetsProvider.class);
        bindDatabasePopulators(NetAssetsDatabasePopulator.class);
        bindOpenPensionFundRowMappers(NetAssetsRowMapper.class);
        bindXlsWritters(XlsNetAssetsWritter.class);

    }
}
