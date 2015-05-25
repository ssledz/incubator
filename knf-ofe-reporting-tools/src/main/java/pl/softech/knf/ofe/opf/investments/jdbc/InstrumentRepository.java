package pl.softech.knf.ofe.opf.investments.jdbc;

import pl.softech.knf.ofe.opf.investments.Instrument;

import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface InstrumentRepository {

    Instrument findByIdentifier(String identifier);

    Instrument findById(Long id);

    List<Instrument> findAll();

    Instrument save(Instrument instrument);

}
