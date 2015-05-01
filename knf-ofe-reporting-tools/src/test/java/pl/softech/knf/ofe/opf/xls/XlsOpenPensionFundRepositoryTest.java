package pl.softech.knf.ofe.opf.xls;

import com.google.common.eventbus.EventBus;
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
import pl.softech.knf.ofe.opf.members.xls.imp.MembersProvider;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

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

    private List<OpenPensionFund> findAll(final String fileName) {
        XlsOpenPensionFundRepository repository = factory.create(loadFile(fileName));
        return repository.findAll();
    }

    @Test
    public void testFindAllForFile1() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0402_tcm75-4044.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(840_573L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(863_581L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(116_742L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(221_643L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(232_685L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(37_952L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(389_963L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(412_034L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(100_144L));

    }

    @Test
    public void testFindAllForFile2() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0104_tcm75-4000.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(957_605L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_004_096L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(156_391L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(250_614L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(257_837L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(28_278L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(400_643L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(422_436L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(84_854L));

    }

    @Test
    public void testFindAllForFile3() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0204_tcm75-4001.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(967_298L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_010_385L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(148_021L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(250_550L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(260_707L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(26_544L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(401_378L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(424_671L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(83_041L));

    }

    @Test
    public void testFindAllForFile4() throws Exception {

        List<OpenPensionFund> funds = findAll("2010_01k_tcm75-18109.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(777_818L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(803_757L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(70_205L));

        fund = fund(funds, "Allianz Polska OFE");
        assertThat(fund.getNumberOfMembers(), is(411_307L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(427_191L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(32_020L));

        fund = fund(funds, "Amplico OFE");
        assertThat(fund.getNumberOfMembers(), is(1_115_294L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_140_253L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(40_158L));

    }

    @Test
    public void testFindAllForFile5() throws Exception {

        List<OpenPensionFund> funds = findAll("2013_01_tcm75-33446.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(946_965L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(959_172L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(22_776L));

        fund = fund(funds, "Allianz Polska OFE");
        assertThat(fund.getNumberOfMembers(), is(555_274L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(578_748L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(5_668L));

        fund = fund(funds, "Amplico OFE");
        assertThat(fund.getNumberOfMembers(), is(1_275_028L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_309_734L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(13_421L));

    }

    @Test
    public void testFindAllForFile6() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0403_tcm75-4025.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(881_819L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(925_812L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(135_116L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(239_588L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(249_067L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(28_633L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(389_589L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(414_928L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(89_902L));

    }

    private static class TestModule extends AbstractModule {

        @Override
        protected void configure() {

            EventBus eventBus = new EventBus("Default EventBus");
            bind(EventBus.class).toInstance(eventBus);

            bind(OpenPensionFundNameTranslator.class).to(SimpleOpenPensionFundNameTranslator.class);
            bind(OpenPensionFundDateAdjuster.class).to(SimpleOpenPensionFundDateAdjuster.class);

            install(new FactoryModuleBuilder()
                            .implement(OpenPensionFundRepository.class, Xls.class, XlsOpenPensionFundRepository.class)
                            .build(XlsOpenPensionFundRepositoryFactory.class)
            );
            Multibinder<DataProvider> dataProviderBinder = Multibinder.newSetBinder(binder(), DataProvider.class);
            dataProviderBinder.addBinding().to(AccountsProvider.class);
            dataProviderBinder.addBinding().to(MembersProvider.class);
        }
    }
}