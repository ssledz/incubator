package pl.softech.knf.ofe.opf.investments.jdbc;

import com.google.common.collect.Iterables;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.shared.jdbc.DataAccessException;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;
import pl.softech.knf.ofe.shared.jdbc.RowMapper;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InstrumentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public InstrumentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Instrument findByIdentifier(String identifier) {
        return Iterables.getOnlyElement(
                jdbcTemplate.query("SELECT * from instrument WHERE inst_identifier = ?",
                        new Object[]{identifier},
                        new MyRowMapper())
        );
    }

    public Instrument save(Instrument instrument) {

        if (instrument.getId() != null) {
            int updated = jdbcTemplate.update("UPDATE instrument set inst_identifier = ?, inst_name = ?, inst_description = ? WHERE id = ?",
                    instrument.getIdentifier(), instrument.getName(), instrument.getDescription(), instrument.getId());
            if (updated == 0) {
                throw new DataAccessException(String.format("There is no instrument record with id = %d, instrument = %s",
                        instrument.getId(), instrument));
            }
            return instrument;
        }

        int updated = jdbcTemplate.update("UPDATE instrument set inst_name = ?, inst_description = ? WHERE inst_identifier = ?",
                instrument.getIdentifier(), instrument.getName(), instrument.getDescription(), instrument.getId());

        if (updated == 0) {
            jdbcTemplate.update("INSERT INTO instrument (inst_identifier, inst_name, inst_description) VALUES (?,?,?)",
                    instrument.getIdentifier(), instrument.getName(), instrument.getDescription()
            );
        }

        return findByIdentifier(instrument.getIdentifier());
    }

    private static class MyRowMapper implements RowMapper<Instrument> {

        @Override
        public Instrument mapRow(ResultSet rs) throws SQLException {
            return new Instrument(
                    rs.getLong("id"),
                    rs.getString("inst_identifier"),
                    rs.getString("inst_name"),
                    rs.getString("inst_description")
            );
        }
    }
}
