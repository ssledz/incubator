package pl.softech.knf.ofe.opf.xls;

import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;

import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface XlsWritter {

    void write(final List<OpenPensionFund> funds, final Workbook wb);

}
