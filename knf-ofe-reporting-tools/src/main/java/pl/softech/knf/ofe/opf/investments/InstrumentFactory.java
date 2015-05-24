package pl.softech.knf.ofe.opf.investments;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface InstrumentFactory {

    Instrument create(String name, String description);

}
