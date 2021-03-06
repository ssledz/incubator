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
import pl.softech.knf.ofe.opf.accunits.xls.export.XlsAccountingUnitsWritter;
import pl.softech.knf.ofe.opf.accunits.xls.imp.AccountingUnitsProvider;
import pl.softech.knf.ofe.opf.contributions.xls.export.XlsContributionWritter;
import pl.softech.knf.ofe.opf.contributions.xls.imp.ContributionProvider;
import pl.softech.knf.ofe.opf.investments.InstrumentFactory;
import pl.softech.knf.ofe.opf.investments.SimpleInstrumentFactory;
import pl.softech.knf.ofe.opf.investments.xls.imp.InvestmentsProvider;
import pl.softech.knf.ofe.opf.members.xls.export.XlsMembersWritter;
import pl.softech.knf.ofe.opf.members.xls.imp.MembersProvider;
import pl.softech.knf.ofe.opf.netassets.xls.export.XlsNetAssetsWritter;
import pl.softech.knf.ofe.opf.netassets.xls.imp.NetAssetsProvider;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundRepositoryTest {

    private XlsOpenPensionFundRepositoryFactory factory;
    private InstrumentFactory instrumentFactory;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new TestModule());
        factory = injector.getInstance(XlsOpenPensionFundRepositoryFactory.class);
        instrumentFactory = injector.getInstance(InstrumentFactory.class);
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
        assertThat(fund.getNetAssets().getValue(), is(2_040_035_253_0400L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(13_5800L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(863_581L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(116_742L));
        assertThat(fund.getContribution().getAmount().getValue(), is(51_206_226_0800L));
        assertThat(fund.getContribution().getInterests().getValue(), is(29_473_9900L));
        assertThat(fund.getContribution().getNumber(), is(371_654L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_887_3900L));
        assertThat(fund.getInvestmens().size(), is(7));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));
        assertThat(investment(fund, "Obligacje"), is(Optional.of(1_358_779_766_2500L)));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(221_643L));
        assertThat(fund.getNetAssets().getValue(), is(597_753_606_4500L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(14_2200L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(232_685L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(37_952L));
        assertThat(fund.getContribution().getAmount().getValue(), is(15_410_327_5800L));
        assertThat(fund.getContribution().getInterests().getValue(), is(10_828_9100L));
        assertThat(fund.getContribution().getNumber(), is(101_344L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(2_083_0100L));
        assertThat(fund.getInvestmens().size(), is(7));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));
        assertThat(investment(fund, "Obligacje"), is(Optional.of(403_096_037_7500L)));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(389_963L));
        assertThat(fund.getNetAssets().getValue(), is(752_803_410_0599L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(14_5000L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(412_034L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(100_144L));
        assertThat(fund.getContribution().getAmount().getValue(), is(16_414_335_7200L));
        assertThat(fund.getContribution().getInterests().getValue(), is(16_079_3200L));
        assertThat(fund.getContribution().getNumber(), is(134_579L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_670_7900L));
        assertThat(fund.getInvestmens().size(), is(7));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(14_211_095_7800L)));
        assertThat(investment(fund, "Obligacje"), is(Optional.of(453_192_676_1700L)));

    }

    @Test
    public void testFindAllForFile2() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0104_tcm75-4000.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(957_605L));
        assertThat(fund.getNetAssets().getValue(), is(4_009_524_601_7700L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(16_5900L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_004_096L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(156_391L));
        assertThat(fund.getContribution().getAmount().getValue(), is(71_916_685_2100L));
        assertThat(fund.getContribution().getInterests().getValue(), is(3_830_135_0100L));
        assertThat(fund.getContribution().getNumber(), is(795_050L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_239_1171L));
        assertThat(fund.getInvestmens().size(), is(19));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(2_153_303_974_1000L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));


        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(250_614L));
        assertThat(fund.getNetAssets().getValue(), is(1_265_517_251_6300L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(17_2800L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(257_837L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(28_278L));
        assertThat(fund.getContribution().getAmount().getValue(), is(21_241_560_7800L));
        assertThat(fund.getContribution().getInterests().getValue(), is(1_065_790_5400L));
        assertThat(fund.getContribution().getNumber(), is(211_288L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_377_1737L));
        assertThat(fund.getInvestmens().size(), is(19));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(795_187_978_2000L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(400_643L));
        assertThat(fund.getNetAssets().getValue(), is(1_438_222_748_2800L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(17_7399L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(422_436L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(84_854L));
        assertThat(fund.getContribution().getAmount().getValue(), is(26_431_187_5500L));
        assertThat(fund.getContribution().getInterests().getValue(), is(1_719_746_4800L));
        assertThat(fund.getContribution().getNumber(), is(296_110L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_222_7586L));
        assertThat(fund.getInvestmens().size(), is(19));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(840_525_124_9900L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(7_748_509_3000L)));

    }

    @Test
    public void testFindAllForFile3() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0204_tcm75-4001.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(967_298L));
        assertThat(fund.getNetAssets().getValue(), is(4_128_658_203_5200L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(17_0000L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_010_385L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(148_021L));
        assertThat(fund.getContribution().getAmount().getValue(), is(52_213_815_8600L));
        assertThat(fund.getContribution().getInterests().getValue(), is(1_480_994_2700L));
        assertThat(fund.getContribution().getNumber(), is(859_200L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(832_4694L));
        assertThat(fund.getInvestmens().size(), is(19));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(2_298_907_895_0000L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(250_550L));
        assertThat(fund.getNetAssets().getValue(), is(1_302_504_414_6600L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(17_6200L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(260_707L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(26_544L));
        assertThat(fund.getContribution().getAmount().getValue(), is(15_601_012_8700L));
        assertThat(fund.getContribution().getInterests().getValue(), is(457_431_7600L));
        assertThat(fund.getContribution().getNumber(), is(229_322L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(931_9319L));
        assertThat(fund.getInvestmens().size(), is(19));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(801_291_120_5000L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(401_378L));
        assertThat(fund.getNetAssets().getValue(), is(1_492_324_439_6600L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(18_2700L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(424_671L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(83_041L));
        assertThat(fund.getContribution().getAmount().getValue(), is(17_063_266_7900L));
        assertThat(fund.getContribution().getInterests().getValue(), is(819_623_7000L));
        assertThat(fund.getContribution().getNumber(), is(305_750L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(764_4918L));
        assertThat(fund.getInvestmens().size(), is(19));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(695_420_972_1100L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(9_265_238_7800L)));

    }

    private Optional<Long> investment(OpenPensionFund fund, String instrumentName) {
        return fund.getInvestmens().stream().filter(inv -> inv.getInstrument().equals(instrumentFactory.create(instrumentName, null)))
                .map(inv -> inv.getMoney().getValue()).findFirst();

    }

    @Test
    public void testFindAllForFile4() throws Exception {

        List<OpenPensionFund> funds = findAll("2010_01k_tcm75-18109.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(777_818L));
        assertThat(fund.getNetAssets().getValue(), is(7_385_713_494_6100L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(27_4800L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(803_757L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(70_205L));
        assertThat(fund.getContribution().getAmount().getValue(), is(67_476_829_2300L));
        assertThat(fund.getContribution().getInterests().getValue(), is(478_718_2700L));
        assertThat(fund.getContribution().getNumber(), is(548_279L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_685_8900L));
        assertThat(fund.getInvestmens().size(), is(18));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(89_397_0000L)));
        assertThat(investment(fund, "Certyfikaty inwestycyjne"), is(Optional.of(17_403_011_6000L)));
        assertThat(investment(fund, "Dłużne skarbowe"), is(Optional.of(4_715_250_802_8400L)));

        fund = fund(funds, "Allianz Polska OFE");
        assertThat(fund.getNumberOfMembers(), is(411_307L));
        assertThat(fund.getNetAssets().getValue(), is(5_129_831_314_7500L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(26_2300L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(427_191L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(32_020L));
        assertThat(fund.getContribution().getAmount().getValue(), is(47_201_793_6500L));
        assertThat(fund.getContribution().getInterests().getValue(), is(353_153_6400L));
        assertThat(fund.getContribution().getNumber(), is(340_264L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_900_2900L));
        assertThat(fund.getInvestmens().size(), is(18));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));
        assertThat(investment(fund, "Certyfikaty inwestycyjne"), is(Optional.of(0L)));
        assertThat(investment(fund, "Dłużne skarbowe"), is(Optional.of(3_180_935_048_8100L)));

        fund = fund(funds, "Amplico OFE");
        assertThat(fund.getNumberOfMembers(), is(1_115_294L));
        assertThat(fund.getNetAssets().getValue(), is(13_929_425_570_1700L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(26_8000L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_140_253L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(40_158L));
        assertThat(fund.getContribution().getAmount().getValue(), is(128_792_997_8300L));
        assertThat(fund.getContribution().getInterests().getValue(), is(844_115_6400L));
        assertThat(fund.getContribution().getNumber(), is(895_031L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_971_2000L));
        assertThat(fund.getInvestmens().size(), is(18));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(5_650_839_7500L)));
        assertThat(investment(fund, "Certyfikaty inwestycyjne"), is(Optional.of(41_162_719_2500L)));
        assertThat(investment(fund, "Dłużne skarbowe"), is(Optional.of(7_491_246_327_2900L)));

    }

    @Test
    public void testFindAllForFile5() throws Exception {

        List<OpenPensionFund> funds = findAll("2013_01_tcm75-33446.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(946_965L));
        assertThat(fund.getNetAssets().getValue(), is(11_519_034_182_0000L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(33_0100L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(959_172L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(22_776L));
        assertThat(fund.getContribution().getAmount().getValue(), is(31_471_150_6600L));
        assertThat(fund.getContribution().getInterests().getValue(), is(313_742_6800L));
        assertThat(fund.getContribution().getNumber(), is(817_645L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(0L));
        assertThat(fund.getInvestmens().size(), is(17));
        assertThat(investment(fund, "Certyfikaty inwestycyjne"), is(Optional.of(28_414_996_6800L)));
        assertThat(investment(fund, "Depozyty i bankowe papiery wartościowe w innych walutach"), is(Optional.of(0L)));
        assertThat(investment(fund, "Depozyty i bankowe papiery wartościowe w walucie krajowej"), is(Optional.of(519_194_697_9400L)));

        fund = fund(funds, "Allianz Polska OFE");
        assertThat(fund.getNumberOfMembers(), is(555_274L));
        assertThat(fund.getNetAssets().getValue(), is(8_178_030_856_8100L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(32_4300L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(578_748L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(5_668L));
        assertThat(fund.getContribution().getAmount().getValue(), is(23_129_590_2800L));
        assertThat(fund.getContribution().getInterests().getValue(), is(190_165_1000L));
        assertThat(fund.getContribution().getNumber(), is(541_514L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(0L));
        assertThat(fund.getInvestmens().size(), is(17));
        assertThat(investment(fund, "Certyfikaty inwestycyjne"), is(Optional.of(0L)));
        assertThat(investment(fund, "Depozyty i bankowe papiery wartościowe w innych walutach"), is(Optional.of(0L)));
        assertThat(investment(fund, "Depozyty i bankowe papiery wartościowe w walucie krajowej"), is(Optional.of(16_508_366_1600L)));

        fund = fund(funds, "Amplico OFE");
        assertThat(fund.getNumberOfMembers(), is(1_275_028L));
        assertThat(fund.getNetAssets().getValue(), is(21_336_613_212_3300L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(32_9500L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(1_309_734L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(13_421L));
        assertThat(fund.getContribution().getAmount().getValue(), is(55_642_539_6100L));
        assertThat(fund.getContribution().getInterests().getValue(), is(516_596_2400L));
        assertThat(fund.getContribution().getNumber(), is(1_275_721L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(0L));
        assertThat(fund.getInvestmens().size(), is(17));
        assertThat(investment(fund, "Certyfikaty inwestycyjne"), is(Optional.of(31_544_183_3200L)));
        assertThat(investment(fund, "Depozyty i bankowe papiery wartościowe w innych walutach"), is(Optional.of(5_612_661_7699L)));
        assertThat(investment(fund, "Depozyty i bankowe papiery wartościowe w walucie krajowej"), is(Optional.of(725_684_301_2700L)));

    }

    @Test
    public void testFindAllForFile6() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0403_tcm75-4025.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(881_819L));
        assertThat(fund.getNetAssets().getValue(), is(2_990_443_123_6300L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(14_8000L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(925_812L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(135_116L));
        assertThat(fund.getContribution().getAmount().getValue(), is(115_821_751_4500L));
        assertThat(fund.getContribution().getInterests().getValue(), is(91_747_0399L));
        assertThat(fund.getContribution().getNumber(), is(1_141_674L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_389_7100L));
        assertThat(fund.getInvestmens().size(), is(15));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(2_091_934_946_5000L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));

        fund = fund(funds, "OFE Allianz Polska");
        assertThat(fund.getNumberOfMembers(), is(239_588L));
        assertThat(fund.getNetAssets().getValue(), is(943_649_473_0900L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(15_6400L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(249_067L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(28_633L));
        assertThat(fund.getContribution().getAmount().getValue(), is(39_361_005_4500L));
        assertThat(fund.getContribution().getInterests().getValue(), is(20_647_2599L));
        assertThat(fund.getContribution().getNumber(), is(325_350L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_657_2700L));
        assertThat(fund.getInvestmens().size(), is(15));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(739_530_966_5000L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));

        fund = fund(funds, "Bankowy OFE");
        assertThat(fund.getNumberOfMembers(), is(389_589L));
        assertThat(fund.getNetAssets().getValue(), is(1_069_821_597_2000L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(15_9400L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(414_928L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(89_902L));
        assertThat(fund.getContribution().getAmount().getValue(), is(38_588_587_1599L));
        assertThat(fund.getContribution().getInterests().getValue(), is(18_381_4100L));
        assertThat(fund.getContribution().getNumber(), is(439_640L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_202_3699L));
        assertThat(fund.getInvestmens().size(), is(15));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(683_356_832_2400L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(10_880_885_4000L)));

    }

    @Test
    public void testFindAllForFile7() throws Exception {

        List<OpenPensionFund> funds = findAll("dane1207_tcm75-6283.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(351_050L));
        assertThat(fund.getNetAssets().getValue(), is(2_945_276_572_0700L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(27_6200L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(363_224L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(27_469L));
        assertThat(fund.getContribution().getAmount().getValue(), is(19_728_377_8500L));
        assertThat(fund.getContribution().getInterests().getValue(), is(169_057_7200L));
        assertThat(fund.getContribution().getNumber(), is(237_954L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_135_7300L));
        assertThat(fund.getInvestmens().size(), is(14));
        assertThat(investment(fund, "Obligacje, bony skarbowe"), is(Optional.of(1_709_762_319_8599L)));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(4_317_600_0000L)));

    }

    @Test
    public void testFindAllForFile8() throws Exception {

        List<OpenPensionFund> funds = findAll("2014_06_tcm75-38540.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(937_534L));
        assertThat(fund.getNetAssets().getValue(), is(6_469_701_190_3900L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(35_6800L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(945_483L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(18_770L));
        assertThat(fund.getContribution().getAmount().getValue(), is(37_766_608_2299L));
        assertThat(fund.getContribution().getInterests().getValue(), is(213_479_7900L));
        assertThat(fund.getContribution().getNumber(), is(769_962L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(0L));
        assertThat(fund.getInvestmens().size(), is(21));
        assertThat(investment(fund, "5. Depozyty bankowe w walucie polskiej"), is(Optional.of(293_882_557_2500L)));
        assertThat(investment(fund, "19. Krajowe obligacje przychodowe"), is(Optional.of(25_582_732_1400L)));

    }

    @Test
    public void testFindAllForFile9() throws Exception {

        List<OpenPensionFund> funds = findAll("dane0103_tcm75-4020.xls");

        OpenPensionFund fund = fund(funds, "AIG OFE");
        assertThat(fund.getNumberOfMembers(), is(868_815L));
        assertThat(fund.getNetAssets().getValue(), is(2_731_317_351_2900L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(14_3800L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(896_162L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(123_426L));
        assertThat(fund.getContribution().getAmount().getValue(), is(66_570_085_1100L));
        assertThat(fund.getContribution().getInterests().getValue(), is(23_046_6400L));
        assertThat(fund.getContribution().getNumber(), is(727_374L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(1_253_7100L));
        assertThat(fund.getInvestmens().size(), is(7));
        assertThat(investment(fund, "Akcje NFI"), is(Optional.of(0L)));
        assertThat(investment(fund, "Obligacje"), is(Optional.of(1_867_033_150_8300L)));

    }

    @Test
    public void testFindAllForFile10() throws Exception {

        List<OpenPensionFund> funds = findAll("2014_11c_tcm75-40019.xls");

        OpenPensionFund fund = fund(funds, "AEGON OFE");
        assertThat(fund.getNumberOfMembers(), is(929_601L));
        assertThat(fund.getNetAssets().getValue(), is(6_490_295_030_6700L));
        assertThat(fund.getAccountingUnitValue() .getValue(), is(36_6500L));
        assertThat(fund.getNumberOfAccounts().getTotal(), is(938_460L));
        assertThat(fund.getNumberOfAccounts().getInactive(), is(16_611L));
        assertThat(fund.getContribution().getAmount().getValue(), is(6_587_898_4700L));
        assertThat(fund.getContribution().getInterests().getValue(), is(124_941_1600L));
        assertThat(fund.getContribution().getNumber(), is(141_339L));
        assertThat(fund.getContribution().getAverageBasis().getValue(), is(0L));
        assertThat(fund.getInvestmens().size(), is(24));
        assertThat(investment(fund, "5. Depozyty bankowe w walucie polskiej"), is(Optional.of(395_427_888_2300L)));
        assertThat(investment(fund, "19. Krajowe obligacje przychodowe"), is(Optional.of(24_190_892_5100L)));

    }

    private static class TestModule extends OpenPensionFundAbstractModule {

        @Override
        protected void configure() {

            EventBus eventBus = new EventBus("Default EventBus");
            bind(EventBus.class).toInstance(eventBus);

            bind(OpenPensionFundNameTranslator.class).to(SimpleOpenPensionFundNameTranslator.class);
            bind(OpenPensionFundDateAdjuster.class).to(SimpleOpenPensionFundDateAdjuster.class);
            bind(InstrumentFactory.class).to(SimpleInstrumentFactory.class);

            install(new FactoryModuleBuilder()
                            .implement(OpenPensionFundRepository.class, Xls.class, XlsOpenPensionFundRepository.class)
                            .build(XlsOpenPensionFundRepositoryFactory.class)
            );

            bindDataProviders(MembersProvider.class, AccountsProvider.class, ContributionProvider.class, InvestmentsProvider.class,
                    NetAssetsProvider.class, AccountingUnitsProvider.class);

            bindXlsWritters(XlsMembersWritter.class, XlsAccountsWritter.class, XlsContributionWritter.class, XlsNetAssetsWritter.class,
                    XlsAccountingUnitsWritter.class);
        }
    }
}