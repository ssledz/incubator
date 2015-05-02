package pl.softech.knf.ofe.opf.accounts.xls.imp;

import com.google.common.collect.ImmutableMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import pl.softech.knf.ofe.opf.accounts.NumberOfAccounts;

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
public class XlsAccountsParserTest {

    @Test
    public void testParseSheet() {

        Sheet sheet = loadSheet("II Accounts", "dane0402_tcm75-4044.xls");

        XlsAccountsParser parser = new XlsAccountsParser();

        String[][] header = {
                {"Open Pension Fund", "Number of accounts"},
                {"", "total", "including \"inactive accounts\""},
                {"", "", "total", "%"}
        };

        Map<String, NumberOfAccounts> name2nof = new ImmutableMap.Builder<String, NumberOfAccounts>()
                .put("AIG OFE", new NumberOfAccounts(863_581L, 116_742L))
                .put("OFE Allianz Polska", new NumberOfAccounts(232_685L, 37_952L))
                .put("Bankowy OFE", new NumberOfAccounts(412_034L, 100_144L))
                .build();

        Set<String> names = new HashSet<>(name2nof.keySet());
        final boolean[] parsedDate = new boolean[1];
        final boolean[] parsedTotal = new boolean[1];
        final int[] parsedHeader = new int[1];

        parser.addParsingEventListener(new AccountsParsingEventListener() {
            @Override
            public void record(String name, long numberOfAccounts, long numberOfInactiveAccounts) {
                if (name2nof.containsKey(name)) {
                    assertEquals(name2nof.get(name), new NumberOfAccounts(numberOfAccounts, numberOfInactiveAccounts));
                    names.remove(name);
                }
            }

            @Override
            public void total(long totalNumberOfAccounts, long totalNumberOfInactiveAccounts) {
                assertEquals(11_205_461L, totalNumberOfAccounts);
                assertEquals(2_307_712, totalNumberOfInactiveAccounts);
                parsedTotal[0] = true;
            }


            @Override
            public void date(Date date) {
                assertEquals(parseDate("2002-04-30"), date);
                parsedDate[0] = true;
            }

            @Override
            public void header(String[] columns) {
                assertArrayEquals(header[parsedHeader[0]++], columns);
            }
        });

        parser.parseSheet(sheet);

        assertTrue(parsedDate[0]);
        assertEquals(3, parsedHeader[0]);
        assertTrue(names.isEmpty());
        assertTrue(parsedTotal[0]);

    }
}