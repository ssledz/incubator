package pl.softech.knf.ofe.shared.xls;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

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

}
