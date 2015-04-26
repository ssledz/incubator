package pl.softech.knf.ofe.shared.xls.parser;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface ParsingEventListener {

    void date(Date date);

    void header(String[] columns);
}
