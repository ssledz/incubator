package pl.softech.knf.ofe.opf.netassets.xls.imp;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class NetAssetsParsingEventListenerAdapter implements NetAssetsParsingEventListener {

    @Override
    public void record(String name, double amount) {

    }

    @Override
    public void total(double amount) {

    }

    @Override
    public void date(Date date) {

    }

    @Override
    public void header(String[] columns) {

    }
}
