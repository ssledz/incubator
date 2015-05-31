package pl.softech.knf.ofe.opf;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import pl.softech.knf.ofe.Jdbc;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.accounts.AccountsModule;
import pl.softech.knf.ofe.opf.accunits.AccountingUnitsModule;
import pl.softech.knf.ofe.opf.contributions.ContributionModule;
import pl.softech.knf.ofe.opf.investments.InvestmentsModule;
import pl.softech.knf.ofe.opf.jdbc.JdbcOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.members.MembersModule;
import pl.softech.knf.ofe.opf.netassets.NetAssetsModule;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFundModule extends AbstractModule {

    @Override
    protected void configure() {

        EventBus eventBus = new EventBus("Default EventBus");

        bind(EventBus.class).toInstance(eventBus);

        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                typeEncounter.register((InjectionListener<I>) i -> eventBus.register(i)
                );
            }
        });

        bind(OpenPensionFundNameTranslator.class).to(SimpleOpenPensionFundNameTranslator.class);
        bind(OpenPensionFundDateAdjuster.class).to(SimpleOpenPensionFundDateAdjuster.class);

        bind(OpenPensionFundRepository.class).annotatedWith(Jdbc.class).to(JdbcOpenPensionFundRepository.class);

        install(new FactoryModuleBuilder().implement(OpenPensionFundRepository.class, Xls.class, XlsOpenPensionFundRepository.class).build(
                XlsOpenPensionFundRepositoryFactory.class));

        bind(OpenPensionFundDbImportTask.class);
        bind(OpenPensionFundDbExportTask.class);

        install(new MembersModule());
        install(new AccountsModule());
        install(new ContributionModule());
        install(new InvestmentsModule());
        install(new NetAssetsModule());
        install(new AccountingUnitsModule());
    }
}
