package pl.softech.knf.ofe.opf.investments.jdbc;

import com.google.common.eventbus.EventBus;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.event.LackOfDataEvent;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.jdbc.DatabasePopulator;
import pl.softech.knf.ofe.shared.jdbc.DataAccessException;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;
import pl.softech.knf.ofe.shared.jdbc.ResultSetExtractor;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsDatabasePopulator implements DatabasePopulator {

    private final EventBus eventBus;

    private final JdbcTemplate jdbcTemplate;

    private final InstrumentRepository instrumentRepository;

    private Map<String, Instrument> identifier2Instrument = new HashMap<>();

    @Inject
    public InvestmentsDatabasePopulator(EventBus eventBus, JdbcTemplate jdbcTemplate, InstrumentRepository instrumentRepository) {
        this.eventBus = eventBus;
        this.jdbcTemplate = jdbcTemplate;
        this.instrumentRepository = instrumentRepository;
    }

    private Long queryForFundId(OpenPensionFund fund) {
        return jdbcTemplate.query("SELECT id from open_pension_fund WHERE opf_name = ? AND opf_date = ?",
                new Object[]{fund.getName(), fund.getDate()},
                new LongResultSetExtractor());
    }

    @Override
    public void populate(OpenPensionFund fund) {

        if (fund.getInvestmens().isEmpty()) {
            eventBus.post(new LackOfDataEvent("There is no contribution in {0}", fund));
        }

        final Long fundId[] = new Long[1];

        fundId[0] = queryForFundId(fund);

        if (fundId[0] == null) {
            jdbcTemplate.update("INSERT INTO open_pension_fund (opf_name, opf_date) VALUES (?,?)",
                    fund.getName(), fund.getDate());

            fundId[0] = queryForFundId(fund);
        }

        requireNonNull(fundId[0]);

        fund.getInvestmens().forEach(investment -> {

            Long instrId = queryForInstrumentId(investment.getInstrument());

            requireNonNull(instrId);

            jdbcTemplate.update("INSERT INTO investment (inv_opf_id, inv_instrument_id, inv_value) values (?,?,?)",
                    fundId[0], instrId, investment.getValue());

        });

    }

    private Long queryForInstrumentId(Instrument instrument) {

        String identifier = instrument.getIdentifier();

        if (!identifier2Instrument.containsKey(identifier)) {
            Instrument tmp = instrumentRepository.findByIdentifier(identifier);

            if (tmp == null) {
                tmp = instrumentRepository.save(instrument);
            }

            identifier2Instrument.put(identifier, requireNonNull(tmp));
        }

        return identifier2Instrument.get(identifier).getId();
    }

    private static class LongResultSetExtractor implements ResultSetExtractor<Long> {

        @Override
        public Long extractData(ResultSet rs) throws SQLException, DataAccessException {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return null;
        }
    }
}
