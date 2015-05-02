package pl.softech.knf.ofe.opf;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface OpenPensionFundDateAdjuster {

    Date adjust(Date date);

}
