package pl.softech.knf.ofe.opf;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import pl.softech.knf.ofe.shared.xls.PoiException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class TestUtils {

    public static void syso(final String template, final Object... args) {
        System.out.println(String.format(template, args));
    }

    public static Sheet loadSheet(final String sheetName, String xlsFile) {
        String file = TestUtils.class.getClassLoader().getResource(xlsFile).getFile();
        try (final InputStream inp = new FileInputStream(file)) {
            final Workbook wb = WorkbookFactory.create(inp);
            return wb.getSheet(sheetName);
        } catch (final Exception e) {
            throw new PoiException(e);
        }
    }

    public static final Date parseDate(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
