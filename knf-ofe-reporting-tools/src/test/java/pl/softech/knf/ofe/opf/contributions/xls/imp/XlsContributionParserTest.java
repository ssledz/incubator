package pl.softech.knf.ofe.opf.contributions.xls.imp;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import pl.softech.knf.ofe.opf.contributions.Contribution;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static pl.softech.knf.ofe.opf.TestUtils.loadSheet;
import static pl.softech.knf.ofe.opf.TestUtils.parseDate;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsContributionParserTest {

    @Test
    public void testParseSheet() {
        Sheet sheet = loadSheet("III Contributions", "dane0402_tcm75-4044.xls");

        String[] header = {
                "Open Pension Fund",
                "amount of contribution\n(PLN)",
                "interests\n(PLN)",
                "number of contributions",
                "average contribution\n(PLN)",
                "average basis\n(PLN)"
        };

        Map<String, Contribution> name2contr = new ImmutableMap.Builder<String, Contribution>()
                .put("AIG OFE", new Contribution(new Contribution.Builder()
                        .withAmount(51_206_226_08L)
                        .withInterests(29_473_99L)
                        .withNumber(371_654L)
                        .withAverageBasis(1_887_39L)
                ))
                .put("OFE Allianz Polska", new Contribution(new Contribution.Builder()
                        .withAmount(15_410_327_58L)
                        .withInterests(10_828_91L)
                        .withNumber(101_344L)
                        .withAverageBasis(2_083_01L)
                ))
                .put("Bankowy OFE", new Contribution(new Contribution.Builder()
                        .withAmount(16_414_335_72L)
                        .withInterests(16_079_32L)
                        .withNumber(134_579L)
                        .withAverageBasis(1_670_79L)
                ))
                .build();

        Set<String> names = new HashSet<>(name2contr.keySet());
        final boolean[] parsedDate = new boolean[1];
        final boolean[] parsedTotal = new boolean[1];
        final boolean[] parsedHeader = new boolean[1];

        XlsContributionParser parser = new XlsContributionParser(new EventBus());

        parser.addParsingEventListener(new ContributionParsingEventListener() {
            @Override
            public void record(String name, double amount, double interests, long number, double averageBasis) {
                if (name2contr.containsKey(name)) {
                    assertEquals(name2contr.get(name), new Contribution(new Contribution.Builder()
                            .withAmount((long) (amount * 100))
                            .withInterests((long) (interests * 100))
                            .withNumber(number)
                            .withAverageBasis((long) (averageBasis * 100))
                    ));
                    names.remove(name);
                }
            }

            @Override
            public void total(double amount, double interests, long number, double averageBasis) {
                assertEquals(531_349_747.13, amount, 0.001);
                assertEquals(340_956.57, interests, 0.001);
                assertEquals(4_127_878L, number);
                assertEquals(1_763.32, averageBasis, 0.001);
                parsedTotal[0] = true;
            }

            @Override
            public void date(Date date) {
                assertEquals(parseDate("2002-04-01"), date);
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
        assertTrue(names.isEmpty());
        assertTrue(parsedTotal[0]);

    }
}