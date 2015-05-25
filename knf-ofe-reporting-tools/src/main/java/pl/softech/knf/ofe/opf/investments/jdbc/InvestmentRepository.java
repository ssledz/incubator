package pl.softech.knf.ofe.opf.investments.jdbc;

import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.investments.Investment;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;
import pl.softech.knf.ofe.shared.jdbc.RowMapper;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final InstrumentRepository instrumentRepository;

    @Inject
    public InvestmentRepository(JdbcTemplate jdbcTemplate, InstrumentRepository instrumentRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.instrumentRepository = new CacheableInstrumentRepository(instrumentRepository);
    }

    public List<Investment> findByOpenPensionFundId(Long openPensionFundId) {
        return jdbcTemplate.query("SELECT * FROM investment WHERE inv_opf_id = ?",
                new Object[]{openPensionFundId},
                new MyRowMapper()
        );
    }

    private class MyRowMapper implements RowMapper<Investment> {

        @Override
        public Investment mapRow(ResultSet rs) throws SQLException {

            return new Investment(
                    instrumentRepository.findById(requireNonNull(rs.getLong("inv_instrument_id"))),
                    requireNonNull(rs.getLong("inv_value"))
            );
        }
    }

    private class CacheableInstrumentRepository implements InstrumentRepository {

        private final InstrumentRepository repository;

        private Map<Long, Instrument> id2instrument = new ConcurrentHashMap<>();

        public CacheableInstrumentRepository(InstrumentRepository repository) {
            this.repository = repository;
        }

        @Override
        public Instrument findByIdentifier(String identifier) {
            return repository.findByIdentifier(identifier);
        }

        @Override
        public Instrument findById(Long id) {

            if (id2instrument.containsKey(id)) {
                return id2instrument.get(id);
            }

            Instrument instrument = repository.findById(id);
            id2instrument.put(id, instrument);

            return instrument;
        }

        @Override
        public List<Instrument> findAll() {
            return repository.findAll();
        }

        @Override
        public Instrument save(Instrument instrument) {
            return repository.save(instrument);
        }
    }
}
