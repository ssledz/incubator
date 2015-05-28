package pl.softech.knf.ofe.opf.investments;

import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class SimpleInstrumentFactory implements InstrumentFactory {

    private MessageDigest md;

    public SimpleInstrumentFactory() {
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Instrument create(String name, String description) {
        return new Instrument(createIdentifier(name.trim()), name.trim(), description);
    }

    private String createIdentifier(String name) {

        byte[] md5;

        try {
            md5 = md.digest(name.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        return new String(Hex.encodeHex(md5));
    }
}
