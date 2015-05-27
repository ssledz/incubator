package pl.softech.knf.ofe.opf.investments.xls.imp;

import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Ignore;
import org.junit.Test;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.investments.InstrumentFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static pl.softech.knf.ofe.opf.TestUtils.loadSheet;
import static pl.softech.knf.ofe.opf.TestUtils.parseDate;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsPortfolioParserTest {

    @Test
    public void testParseSheet1() {

        Sheet sheet = loadSheet("Portfel inwestycyjny OFE", "2013_01_tcm75-33446.xls");

        String[][] header = {
                {
                        "Kategoria lokat",
                        "Opis kategorii lokat",
                        "AEGON OFE",
                        "Allianz Polska OFE",
                        "Amplico OFE",
                        "Aviva OFE Aviva BZ WBK",
                        "AXA OFE",
                        "Generali OFE",
                        "ING OFE",
                        "Nordea OFE",
                        "Pekao OFE",
                        "PKO BP Bankowy OFE",
                        "OFE Pocztylion",
                        "OFE POLSAT",
                        "OFE PZU \"Złota Jesień\"",
                        "OFE WARTA",
                        "Razem:"
                }
        };

        InstrumentFactory instrumentFactory = (name, description) -> new Instrument(name.trim(), name, description);

        Instrument instr1 = instrumentFactory.create("Akcje i prawa z nimi związane spółek notowanych na regulowanym rynku giełdowym",
                "E. Akcje spółek notowanych na regulowanym rynku giełdowym, a także notowanych na regulowanym rynku " +
                        "giełdowym prawach poboru, prawach do akcji oraz obligacji zamiennych na akcje tych spółek");

        Instrument instr2 = instrumentFactory.create("Certyfikaty inwestycyjne",
                "H. Certyfikaty inwestycyjne emitowane przez fundusze inwestycyjne zamknięte");

        Map<Instrument, Map<String, Double>> inst2inv = new ImmutableMap.Builder<Instrument, Map<String, Double>>()
                .put(instr1,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AEGON OFE", 4_285_363_686.55)
                                .put("Allianz Polska OFE", 2_972_137_755.88)
                                .build())
                .put(instr2,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AEGON OFE", 28_414_996.68)
                                .put("Allianz Polska OFE", 0.0)
                                .build())
                .build();

        Map<Instrument, Double> totalInstr2value = new ImmutableMap.Builder<Instrument, Double>()
                .put(instr1, 100_418_602_123.23)
                .put(instr2, 255_397_918.44)
                .build();

        Map<String, Double> totalOpf2value = new ImmutableMap.Builder<String, Double>()
                .put("AEGON OFE", 11_665_976_947.27)
                .put("Allianz Polska OFE", 8_195_937_780.28)
                .build();

        testTestParseSheet(instrumentFactory, sheet, "2013-01-31", header, inst2inv, totalInstr2value, totalOpf2value);
    }

    @Test
    public void testParseSheet2() {

        Sheet sheet = loadSheet("Portfel inwestycyjny OFE", "dane1207_tcm75-6283.xls");

        String[][] header = {
                {
                        "Kategoria lokat",
                        "AEGON OFE",
                        "AIG OFE",
                        "Allianz Polska OFE",
                        "AXA OFE",
                        "Bankowy OFE",
                        "Commercial Union OFE BPH CU WBK",
                        "OFE „DOM”",
                        "Generali OFE",
                        "ING Nationale-Nederlanden Polska OFE",
                        "Nordea OFE",
                        "Pekao OFE",
                        "OFE Pocztylion",
                        "OFE Polsat",
                        "OFE  PZU „Złota Jesień”",
                        "OFE Skarbiec-Emerytura",
                        "Portfel razem"
                }

        };

        InstrumentFactory instrumentFactory = (name, description) -> new Instrument(name.trim(), name, description);

        Instrument instr1 = instrumentFactory.create("Obligacje, bony skarbowe", null);

        Instrument instr2 = instrumentFactory.create("Dłużne p.w. gwarantowane lub poręczane przez SP lub NBP", null);

        Map<Instrument, Map<String, Double>> inst2inv = new ImmutableMap.Builder<Instrument, Map<String, Double>>()
                .put(instr1,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AEGON OFE", 1_709_762_319.86)
                                .put("Allianz Polska OFE", 2_206_424_174.74)
                                .build())
                .put(instr2,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AEGON OFE", 0.0)
                                .put("Allianz Polska OFE", 14_511_496.88)
                                .build())
                .build();

        Map<Instrument, Double> totalInstr2value = new ImmutableMap.Builder<Instrument, Double>()
                .put(instr1, 83_951_259_557.03)
                .put(instr2, 49_067_880.06)
                .build();

        Map<String, Double> totalOpf2value = new ImmutableMap.Builder<String, Double>()
                .put("AEGON OFE", 2_938_204_875.44)
                .put("Allianz Polska OFE", 3_449_655_555.08)
                .build();

        testTestParseSheet(instrumentFactory, sheet, "2007-12-31", header, inst2inv, totalInstr2value, totalOpf2value);
    }


    @Test
    public void testParseSheet3() {

        Sheet sheet = loadSheet(" Portfel inwestycyjny OFE", "dane0104_tcm75-4000.xls");

        String[][] header = {
                {
                        "Otwarty fundusz emerytalny",
                        "Obligacje, bony skarbowe",
                        "Dłużne p.w. gwarantowane lub poręczane przez SP lub NBP",
                        "Depozyty, bankowe p.w. \n(w walucie polskiej)",
                        "Depozyty, bankowe p.w. (w walutach państw obcych)",
                        "Akcje  notowane na regulowanym rynku giełdowym",
                        "Akcje  notowane na regulowanym rynku pozagiełdowym, nie notowane dopuszczone do publicznego obrotu",
                        "Akcje NFI",
                        "Certyfikaty inwestycyjne funduszy inwestycyjnych",
                        "Jednostki uczestnictwa funduszy inwestycyjnych",
                        "Dłużne pozaskarbowe p.w.  dopuszczone do publicznego obrotu",
                        "Dłużne pozaskarbowe p.w. nie dopuszczone do publicznego obrotu",
                        "Obligacje przychodowe",
                        "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie dopuszczone do publicznego obrotu",
                        "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie nie dopuszczone do publicznego obrotu",
                        "Inne dłużne p.w. spółek publicznych",
                        "Inne dłużne p.w. dopuszczone do publicznego obrotu",
                        "Listy zastawne",
                        "Kwity depozytowe",
                        "Inne lokaty",
                        "Portfel razem"
                }
        };

        InstrumentFactory instrumentFactory = (name, description) -> new Instrument(name.trim(), name, description);

        Instrument instr1 = instrumentFactory.create("Obligacje, bony skarbowe", null);

        Instrument instr2 = instrumentFactory.create("Dłużne p.w. gwarantowane lub poręczane przez SP lub NBP", null);

        Map<Instrument, Map<String, Double>> inst2inv = new ImmutableMap.Builder<Instrument, Map<String, Double>>()
                .put(instr1,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AIG OFE", 2_153_303_974.10)
                                .put("OFE Allianz Polska", 795_187_978.20)
                                .build())
                .put(instr2,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AIG OFE", 0.0)
                                .put("OFE Allianz Polska", 0.0)
                                .build())
                .build();

        Map<Instrument, Double> totalInstr2value = new ImmutableMap.Builder<Instrument, Double>()
                .put(instr1, 28_468_511_616.62)
                .put(instr2, 6_419_583.51)
                .build();

        Map<String, Double> totalOpf2value = new ImmutableMap.Builder<String, Double>()
                .put("AIG OFE", 3_909_543_922.09)
                .put("OFE Allianz Polska", 1_227_404_854.78)
                .build();

        testTestParseSheet(instrumentFactory, sheet, "2004-01-30", header, inst2inv, totalInstr2value, totalOpf2value);
    }

    @Test
    public void testParseSheet4() {

        Sheet sheet = loadSheet("Porfel inwestycyjny", "dane0402_tcm75-4044.xls");

        String[][] header = {
                {
                        "Otwarty Fundusz Emerytalny",
                        "Akcje NFI",
                        "Akcje spółek notowanych na regulowanym rynku giełdowym",
                        "Bony skarbowe",
                        "Depozyty bankowe i bankowe papiery wartościowe",
                        "Obligacje",
                        "Inwestycje za granicą",
                        "Inne lokaty",
                        "Razem"
                }

        };

        InstrumentFactory instrumentFactory = (name, description) -> new Instrument(name.trim(), name, description);

        Instrument instr1 = instrumentFactory.create("Akcje NFI", null);

        Instrument instr2 = instrumentFactory.create("Akcje spółek notowanych na regulowanym rynku giełdowym", null);

        Map<Instrument, Map<String, Double>> inst2inv = new ImmutableMap.Builder<Instrument, Map<String, Double>>()
                .put(instr1,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AIG OFE", 0.0)
                                .put("OFE Allianz Polska", 0.0)
                                .build())
                .put(instr2,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AIG OFE", 587_526_105.26)
                                .put("OFE Allianz Polska", 167_560_543.48)
                                .build())
                .build();

        Map<Instrument, Double> totalInstr2value = new ImmutableMap.Builder<Instrument, Double>()
                .put(instr1, 69_675_907.09)
                .put(instr2, 7_124_525_861.00)
                .build();

        Map<String, Double> totalOpf2value = new ImmutableMap.Builder<String, Double>()
                .put("AIG OFE", 1_977_501_103.60)
                .put("OFE Allianz Polska", 579_362_646.76)
                .build();

        testTestParseSheet(instrumentFactory, sheet, "2002-04-30", header, inst2inv, totalInstr2value, totalOpf2value);
    }

    @Test
    public void testParseSheet5() {

        Sheet sheet = loadSheet("V  Portfel inwestycyjny OFE", "dane0403_tcm75-4025.xls");

        String[][] header = {
                {
                        "OFE",
                        "Obligacje, bony skarbowe",
                        "Dłużne p.w. gwarantowane lub poręczane przez SP lub NBP",
                        "Depozyty, bankowe p.w.",
                        "Akcje  notowane na regulowanym rynku giełdowym",
                        "Akcje  notowane na regulowanym rynku pozagiełdowy, nie notowane dopuszczone do publicznego obrotu",
                        "Akcje NFI",
                        "Certyfikaty inwestycyjne funduszy inwestycyjnych",
                        "Jednostki uczestnictwa funduszy inwestycyjnych",
                        "Dłużne pozaskarbowe p.w.  dopuszczone do publicznego obrotu",
                        "Dłużne pozaskarbowe p.w. nie dopuszczone do publicznego obrotu",
                        "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie dopuszczone do publicznego obrotu",
                        "Dłużne pozaskarbowe p.w. zabezpieczone całkowicie nie dopuszczone do publicznego obrotu",
                        "Inne dłużne p.w. spółek publicznych",
                        "Inne dłużne p.w. dopuszczone do publicznego obrotu",
                        "Inne lokaty",
                        "Portfel razem"
                }

        };

        InstrumentFactory instrumentFactory = (name, description) -> new Instrument(name.trim(), name, description);

        Instrument instr1 = instrumentFactory.create("Obligacje, bony skarbowe", null);

        Instrument instr2 = instrumentFactory.create("Depozyty, bankowe p.w.", null);

        Map<Instrument, Map<String, Double>> inst2inv = new ImmutableMap.Builder<Instrument, Map<String, Double>>()
                .put(instr1,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AIG OFE", 2_091_934_946.50)
                                .put("OFE Allianz Polska", 739_530_966.50)
                                .build())
                .put(instr2,
                        new ImmutableMap.Builder<String, Double>()
                                .put("AIG OFE", 20_692_745.15)
                                .put("OFE Allianz Polska", 22_716_880.68)
                                .build())
                .build();

        Map<Instrument, Double> totalInstr2value = new ImmutableMap.Builder<Instrument, Double>()
                .put(instr1, 23_845_280_821.48)
                .put(instr2, 1_198_780_160.86)
                .build();

        Map<String, Double> totalOpf2value = new ImmutableMap.Builder<String, Double>()
                .put("AIG OFE", 2_889_459_282.70)
                .put("OFE Allianz Polska", 915_737_618.90)
                .build();

        testTestParseSheet(instrumentFactory, sheet, "2003-04-30", header, inst2inv, totalInstr2value, totalOpf2value);
    }

    private void testTestParseSheet(InstrumentFactory instrumentFactory, Sheet sheet, String sheetDate, String[][] header, Map<Instrument,
            Map<String, Double>> inst2inv,
                                    Map<Instrument, Double> totalInstr2value, Map<String, Double> totalOpf2value) {

        Set<String> parsedTotalOpf = new HashSet<>();
        Set<String> parsedTotalInst = new HashSet<>();

        final boolean[] parsedDate = new boolean[1];
        final int[] parsedHeader = new int[1];

        Map<String, Set<String>> parsedRecord = inst2inv.entrySet().stream().map(e -> {
            Map<String, Set<String>> map = new HashMap<>();
            map.put(e.getKey().getIdentifier(), new HashSet<>(e.getValue().keySet()));
            return map;
        }).reduce(new HashMap<>(), (res, map) -> {
            res.putAll(map);
            return res;
        });

        parsedTotalInst.addAll(totalInstr2value.keySet().stream().map(instr -> instr.getIdentifier()).collect(Collectors.toSet()));

        parsedTotalOpf.addAll(totalOpf2value.keySet());

        InvestmentsPortfolioParser parser = new InvestmentsPortfolioParser(instrumentFactory);

        parser.addParsingEventListener(new InvestmentsParsingEventListener() {
            @Override
            public void record(Instrument instrument, String openPensionFundName, double investmentValue) {
                if (inst2inv.containsKey(instrument)) {
                    Map<String, Double> opf2value = inst2inv.get(instrument);
                    if (opf2value.containsKey(openPensionFundName)) {
                        parsedRecord.get(instrument.getIdentifier()).remove(openPensionFundName);
                        assertEquals(opf2value.get(openPensionFundName), investmentValue, 0.01);
                    }
                }
            }

            @Override
            public void total(Instrument instrument, double totalInvestmentValue) {
                if (totalInstr2value.containsKey(instrument)) {
                    assertEquals(totalInstr2value.get(instrument), totalInvestmentValue, 0.01);
                    parsedTotalInst.remove(instrument.getIdentifier());
                }
            }

            @Override
            public void total(String openPensionFundName, double totalInvestmentValue) {
                if (totalOpf2value.containsKey(openPensionFundName)) {
                    assertEquals(totalOpf2value.get(openPensionFundName), totalInvestmentValue, 0.01);
                    parsedTotalOpf.remove(openPensionFundName);
                }
            }

            @Override
            public void date(Date date) {
                assertEquals(parseDate(sheetDate), date);
                parsedDate[0] = true;
            }

            @Override
            public void header(String[] columns) {
                assertArrayEquals(header[parsedHeader[0]++], columns);
            }
        });

        parser.parseSheet(sheet);

        assertTrue(parsedDate[0]);
        assertEquals(header.length, parsedHeader[0]);
        assertTrue(parsedRecord.values().stream().reduce(new HashSet<>(), (res, set) -> {
            res.addAll(set);
            return set;
        }).isEmpty());
        assertTrue(parsedTotalInst.isEmpty());
        assertTrue(parsedTotalOpf.isEmpty());
    }

}