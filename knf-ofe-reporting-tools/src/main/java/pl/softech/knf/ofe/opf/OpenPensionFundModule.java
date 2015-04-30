package pl.softech.knf.ofe.opf;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.accounts.AccountsModule;
import pl.softech.knf.ofe.opf.jdbc.JdbcOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.members.MembersModule;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(OpenPensionFundNameTranslator.class).to(SimpleOpenPensionFundNameTranslator.class);
        bind(OpenPensionFundDateAdjuster.class).to(SimpleOpenPensionFundDateAdjuster.class);

        bind(OpenPensionFundRepository.class).annotatedWith(Jdbc.class).to(JdbcOpenPensionFundRepository.class);

        install(new FactoryModuleBuilder().implement(OpenPensionFundRepository.class, Xls.class, XlsOpenPensionFundRepository.class).build(
                XlsOpenPensionFundRepositoryFactory.class));

        bind(OpenPensionFundDbImportTask.class).toProvider(OpenPensionFundDbImportTaskProvider.class);

        install(new MembersModule());
        install(new AccountsModule());
    }
}
