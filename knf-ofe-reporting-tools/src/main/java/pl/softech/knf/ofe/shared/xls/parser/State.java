package pl.softech.knf.ofe.shared.xls.parser;

import org.apache.poi.ss.usermodel.Row;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface State {
    void parse(Row row);
}
