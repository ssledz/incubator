package pl.softech.knf.ofe.opf.xls;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.junit.Before;
import org.junit.Test;
import pl.softech.knf.ofe.Xls;
import pl.softech.knf.ofe.opf.*;
import pl.softech.knf.ofe.opf.accounts.xls.export.XlsAccountsWritter;
import pl.softech.knf.ofe.opf.accounts.xls.imp.AccountsProvider;
import pl.softech.knf.ofe.opf.contributions.xls.export.XlsContributionWritter;
import pl.softech.knf.ofe.opf.contributions.xls.imp.ContributionProvider;
import pl.softech.knf.ofe.opf.members.xls.export.XlsMembersWritter;
import pl.softech.knf.ofe.opf.members.xls.imp.MembersProvider;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        assertThat(fund.getContribution().getAmount(), is(51_206_226_08L));
        assertThat(fund.getContribution().getInterests(), is(29_473_99L));
        assertThat(fund.getContribution().getNumber(), is(371_654L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_887_39L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(221_643L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(232_685L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(37_952L));
        assertThat(fund.getContribution().getAmount(), is(15_410_327_58L));
        assertThat(fund.getContribution().getInterests(), is(10_828_91L));
        assertThat(fund.getContribution().getNumber(), is(101_344L));
        assertThat(fund.getContribution().getAverageBasis(), is(2_083_01L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(389_963L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(412_034L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(100_144L));
        assertThat(fund.getContribution().getAmount(), is(16_414_335_72L));
        assertThat(fund.getContribution().getInterests(), is(16_079_32L));
        assertThat(fund.getContribution().getNumber(), is(134_579L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_670_79L));

    }

    @Test
    public void testFindAllForFile2() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0104_tcm75-4000.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(957_605L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_004_096L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(156_391L));
        assertThat(fund.getContribution().getAmount(), is(71_916_685_21L));
        assertThat(fund.getContribution().getInterests(), is(3_830_135_01L));
        assertThat(fund.getContribution().getNumber(), is(795_050L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_239_12L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(250_614L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(257_837L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(28_278L));
        assertThat(fund.getContribution().getAmount(), is(21_241_560_78L));
        assertThat(fund.getContribution().getInterests(), is(1_065_790_54L));
        assertThat(fund.getContribution().getNumber(), is(211_288L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_377_17L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(400_643L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(422_436L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(84_854L));
        assertThat(fund.getContribution().getAmount(), is(26_431_187_55L));
        assertThat(fund.getContribution().getInterests(), is(1_719_746_48L));
        assertThat(fund.getContribution().getNumber(), is(296_110L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_222_76L));

    }

    @Test
    public void testFindAllForFile3() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0204_tcm75-4001.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(967_298L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_010_385L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(148_021L));
        assertThat(fund.getContribution().getAmount(), is(52_213_815_86L));
        assertThat(fund.getContribution().getInterests(), is(1_480_994_27L));
        assertThat(fund.getContribution().getNumber(), is(859_200L));
        assertThat(fund.getContribution().getAverageBasis(), is(832_47L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(250_550L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(260_707L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(26_544L));
        assertThat(fund.getContribution().getAmount(), is(15_601_012_87L));
        assertThat(fund.getContribution().getInterests(), is(457_431_76L));
        assertThat(fund.getContribution().getNumber(), is(229_322L));
        assertThat(fund.getContribution().getAverageBasis(), is(931_93L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(401_378L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(424_671L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(83_041L));
        assertThat(fund.getContribution().getAmount(), is(17_063_266_79L));
        assertThat(fund.getContribution().getInterests(), is(819_623_70L));
        assertThat(fund.getContribution().getNumber(), is(305_750L));
        assertThat(fund.getContribution().getAverageBasis(), is(764_49L));

    }

    @Test
    public void testFindAllForFile4() throws Exception {

        List<OpenPensionFund> funds = findAll("2010_01k_tcm75-18109.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(777_818L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(803_757L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(70_205L));
        assertThat(fund.getContribution().getAmount(), is(67_476_829_23L));
        assertThat(fund.getContribution().getInterests(), is(478_718_27L));
        assertThat(fund.getContribution().getNumber(), is(548_279L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_685_89L));

        fund = fund(funds, "Allianz Polska OFE");
        assertThat(fund.getNumberOfMembers(), is(411_307L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(427_191L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(32_020L));
        assertThat(fund.getContribution().getAmount(), is(47_201_793_65L));
        assertThat(fund.getContribution().getInterests(), is(353_153_64L));
        assertThat(fund.getContribution().getNumber(), is(340_264L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_900_29L));

        fund = fund(funds, "Amplico OFE");
        assertThat(fund.getNumberOfMembers(), is(1_115_294L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_140_253L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(40_158L));
        assertThat(fund.getContribution().getAmount(), is(128_792_997_83L));
        assertThat(fund.getContribution().getInterests(), is(844_115_64L));
        assertThat(fund.getContribution().getNumber(), is(895_031L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_971_20L));

    }

    @Test
    public void testFindAllForFile5() throws Exception {

        List<OpenPensionFund> funds = findAll("2013_01_tcm75-33446.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(946_965L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(959_172L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(22_776L));
        assertThat(fund.getContribution().getAmount(), is(31_471_150_66L));
        assertThat(fund.getContribution().getInterests(), is(313_742_68L));
        assertThat(fund.getContribution().getNumber(), is(817_645L));
        assertThat(fund.getContribution().getAverageBasis(), is(0L));

        fund = fund(funds, "Allianz Polska OFE");
        assertThat(fund.getNumberOfMembers(), is(555_274L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(578_748L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(5_668L));
        assertThat(fund.getContribution().getAmount(), is(23_129_590_28L));
        assertThat(fund.getContribution().getInterests(), is(190_165_10L));
        assertThat(fund.getContribution().getNumber(), is(541_514L));
        assertThat(fund.getContribution().getAverageBasis(), is(0L));

        fund = fund(funds, "Amplico OFE");
        assertThat(fund.getNumberOfMembers(), is(1_275_028L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_309_734L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(13_421L));
        assertThat(fund.getContribution().getAmount(), is(55_642_539_61L));
        assertThat(fund.getContribution().getInterests(), is(516_596_24L));
        assertThat(fund.getContribution().getNumber(), is(1_275_721L));
        assertThat(fund.getContribution().getAverageBasis(), is(0L));

    }

    @Test
    public void testFindAllForFile6() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0403_tcm75-4025.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(881_819L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(925_812L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(135_116L));
        assertThat(fund.getContribution().getAmount(), is(115_821_751_45L));
        assertThat(fund.getContribution().getInterests(), is(91_747_04L));
        assertThat(fund.getContribution().getNumber(), is(1_141_674L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_389_71L));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(239_588L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(249_067L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(28_633L));
        assertThat(fund.getContribution().getAmount(), is(39_361_005_45L));
        assertThat(fund.getContribution().getInterests(), is(20_647_26L));
        assertThat(fund.getContribution().getNumber(), is(325_350L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_657_27L));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(389_589L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(414_928L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(89_902L));
        assertThat(fund.getContribution().getAmount(), is(38_588_587_16L));
        assertThat(fund.getContribution().getInterests(), is(18_381_41L));
        assertThat(fund.getContribution().getNumber(), is(439_640L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_202_37L));

    }

    @Test
    public void testFindAllForFile7() throws Exception {

        List<OpenPensionFund> funds = findAll("dane1207_tcm75-6283.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(351_050L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(363_224L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(27_469L));
        assertThat(fund.getContribution().getAmount(), is(19_728_377_85L));
        assertThat(fund.getContribution().getInterests(), is(169_057_72L));
        assertThat(fund.getContribution().getNumber(), is(237_954L));
        assertThat(fund.getContribution().getAverageBasis(), is(1_135_73L));

    }

    private static class TestModule extends OpenPensionFundAbstractModule {

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

            bindDataProviders(MembersProvider.class, AccountsProvider.class, ContributionProvider.class);
            bindXlsWritters(XlsMembersWritter.class, XlsAccountsWritter.class, XlsContributionWritter.class);
        }
    }
}