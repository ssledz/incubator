package pl.softech.knf.ofe.opf.xls;

import org.apache.poi.ss.usermodel.Workbook;

import java.util.Iterator;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface DataProvider {

    Iterator<? extends DataPopulator> iterator(Workbook wb);

}
