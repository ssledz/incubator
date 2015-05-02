package pl.softech.knf.ofe.opf;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import pl.softech.knf.ofe.opf.jdbc.DatabasePopulator;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;
import pl.softech.knf.ofe.opf.xls.DataProvider;
import pl.softech.knf.ofe.opf.xls.XlsWritter;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public abstract class OpenPensionFundAbstractModule extends AbstractModule {

    public void bindDataProviders(Class<? extends DataProvider>... dataProviders) {
        Multibinder<DataProvider> binder = Multibinder.newSetBinder(binder(), DataProvider.class);
        for (Class<? extends DataProvider> provider : dataProviders) {
            binder.addBinding().to(provider);
        }
    }

    public void bindDatabasePopulators(Class<? extends DatabasePopulator>... databasePopulators) {
        Multibinder<DatabasePopulator> binder = Multibinder.newSetBinder(binder(), DatabasePopulator.class);
        for (Class<? extends DatabasePopulator> populator : databasePopulators) {
            binder.addBinding().to(populator);
        }
    }

    public void bindOpenPensionFundRowMappers(Class<? extends OpenPensionFundRowMapper>...
                                                      rowMappers) {
        Multibinder<OpenPensionFundRowMapper> binder = Multibinder.newSetBinder(binder(), OpenPensionFundRowMapper.class);
        for (Class<? extends OpenPensionFundRowMapper> rowMapper : rowMappers) {
            binder.addBinding().to(rowMapper);
        }
    }

    public void bindXlsWritters(Class<? extends XlsWritter>... writers) {
        Multibinder<XlsWritter> binder = Multibinder.newSetBinder(binder(), XlsWritter.class);
        for (Class<? extends XlsWritter> writter : writers) {
            binder.addBinding().to(writter);
        }
    }

}
