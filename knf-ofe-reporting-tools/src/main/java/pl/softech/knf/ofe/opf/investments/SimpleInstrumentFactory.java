package pl.softech.knf.ofe.opf.investments;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class SimpleInstrumentFactory implements InstrumentFactory {

    @Override
    public Instrument create(String name, String description) {
        return new Instrument(createIdentifier(name), name, description);
    }

    private String createIdentifier(String name) {
        return name.trim().replaceAll("\\s+","_").replaceAll("\\W+", "x").toLowerCase();
    }
}
