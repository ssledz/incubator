package pl.softech.knf.ofe.opf.xls;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;
import org.junit.Before;
import org.junit.Test;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.*;
import pl.softech.knf.ofe.opf.accounts.xls.imp.AccountsProvider;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepositoryTest {

    private XlsOpenPensionFundRepositoryFactory factory;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new TestModule());
        factory = injector.getInstance(XlsOpenPensionFundRepositoryFactory.class);
    }

    private File loadFile(String filename) {
        return new File(XlsOpenPensionFundRepositoryTest.class.getClassLoader().getResource(filename).getFile());
    }

    private OpenPensionFund fund(List<OpenPensionFund> funds, String name) {
        return funds.stream()
                .filter(f -> f.getName().equals(name))
                .collect(Collectors.toList())
                .iterator().next();
    }

    @Test
    public void testFindAll() throws Exception {
        XlsOpenPensionFundRepository repository = factory.create(loadFile("dane0402_tcm75-4044.xls"));
        List<OpenPensionFund> funds = repository.findAll();
        funds.forEach(f -> System.out.println(f));

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertEquals(863_581L, fund.getNumberOfAccount().getTotal());
        assertEquals(116_742L, fund.getNumberOfAccount().getInactive());

        fund = fund(funds, "OFE Allianz Polska");
        assertEquals(232_685L, fund.getNumberOfAccount().getTotal());
        assertEquals(37_952L, fund.getNumberOfAccount().getInactive());

        fund = fund(funds, "Bankowy OFE");
        assertEquals(412_034L, fund.getNumberOfAccount().getTotal());
        assertEquals(100_144L, fund.getNumberOfAccount().getInactive());

    }

    private static class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(OpenPensionFundNameTranslator.class).to(SimpleOpenPensionFundNameTranslator.class);

            install(new FactoryModuleBuilder()
                            .implement(OpenPensionFundRepository.class, Xls.class, XlsOpenPensionFundRepository.class)
                            .build(XlsOpenPensionFundRepositoryFactory.class)
            );
            Multibinder<DataProvider> dataProviderBinder = Multibinder.newSetBinder(binder(), DataProvider.class);
            dataProviderBinder.addBinding().to(AccountsProvider.class);
        }
    }
}