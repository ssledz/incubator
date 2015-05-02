package pl.softech.knf.ofe.shared.xls;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsUtils {

    public static Sheet loadSheet(final String name, String filePath) {
        try (final InputStream inp = new FileInputStream(filePath)) {
            final Workbook wb = WorkbookFactory.create(inp);
            return wb.getSheet(name);
        } catch (final Exception e) {
            throw new PoiException(e);
        }
    }

    public static Sheet createSheet(Workbook wb, String sheetName) {
        Sheet sheet = wb.getSheet(sheetName);

        int it = 1;
        while (sheet != null) {
            sheetName = String.format("%s%d", sheetName, it++);
            sheet = wb.getSheet(sheetName);
        }

        return wb.createSheet(sheetName);
    }

    public static Workbook loadWorkbook(final File file) {
        try (final InputStream inp = new FileInputStream(file)) {
            return WorkbookFactory.create(inp);
        } catch (final Exception e) {
            throw new PoiException(e);
        }
    }

    public static Workbook loadOrCreateWorkbook(final File file) {
        if (file.exists()) {
            return loadWorkbook(file);
        }
        return new HSSFWorkbook();
    }

    public static Font createHeaderFont(final Workbook wb, final short size, String fontName) {
        final Font font = wb.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontName);
        font.setBold(true);
        return font;
    }

}
