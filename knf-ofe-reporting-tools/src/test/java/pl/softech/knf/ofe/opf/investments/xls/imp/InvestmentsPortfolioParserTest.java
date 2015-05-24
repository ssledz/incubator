package pl.softech.knf.ofe.opf.investments.xls.imp;

import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
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
    public void testParseSheet() {

        //dane1207_tcm75-6283.xls
        Sheet sheet = loadSheet("Portfel inwestycyjny OFE", "2013_01_tcm75-33446.xls");

        String[] header = {
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

        Set<String> parsedTotalOpf = new HashSet<>();
        Set<String> parsedTotalInst = new HashSet<>();

        final boolean[] parsedDate = new boolean[1];
        final boolean[] parsedHeader = new boolean[1];

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
                assertEquals(parseDate("2013-01-31"), date);
                parsedDate[0] = true;
            }

            @Override
            public void header(String[] columns) {
                assertArrayEquals(header, columns);
                parsedHeader[0] = true;
            }
        });

        parser.parseSheet(sheet);

        assertTrue(parsedDate[0]);
        assertTrue(parsedHeader[0]);
        assertTrue(parsedRecord.values().stream().reduce(new HashSet<>(), (res, set) -> {
            res.addAll(set);
            return set;
        }).isEmpty());
        assertTrue(parsedTotalInst.isEmpty());
        assertTrue(parsedTotalOpf.isEmpty());


    }

}