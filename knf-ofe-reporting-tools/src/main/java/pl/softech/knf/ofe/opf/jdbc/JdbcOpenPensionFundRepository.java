package pl.softech.knf.ofe.opf.jdbc;

import com.google.common.eventbus.EventBus;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.event.DatabasePopulatorErrorEvent;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;
import pl.softech.knf.ofe.shared.jdbc.RowMapper;

import static pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper.*;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class JdbcOpenPensionFundRepository implements OpenPensionFundRepository {

    private final Set<DatabasePopulator> populators;
    private final Set<OpenPensionFundRowMapper> rowMappers;

    private final JdbcTemplate jdbcTemplate;

    private final EventBus eventBus;

    @Inject
    public JdbcOpenPensionFundRepository(Set<DatabasePopulator> populators, Set<OpenPensionFundRowMapper> rowMappers, JdbcTemplate
            jdbcTemplate, EventBus eventBus) {
        this.populators = populators;
        this.rowMappers = rowMappers;
        this.jdbcTemplate = jdbcTemplate;
        this.eventBus = eventBus;
    }

    @Override
    public List<OpenPensionFund> findAll() {
        return jdbcTemplate.query("SELECT * FROM open_pension_fund", new MyRowMapper());
    }

    @Override
    public void save(List<OpenPensionFund> opfs) {
        for (OpenPensionFund fund : opfs) {
            for (DatabasePopulator populator : populators) {
                try {
                    populator.populate(fund);
                } catch (Exception e) {
                    eventBus.post(new DatabasePopulatorErrorEvent(e, "Error during populating {0}. Trying keep going", fund));
                }
            }
        }
    }


    private class MyRowMapper implements RowMapper<OpenPensionFund> {

        @Override
        public OpenPensionFund mapRow(ResultSet rs) throws SQLException {

            OpenPensionFund.Builder builder = new OpenPensionFund.Builder();

            builder.withName(rs.getString(OPF_NAME_COLUMN_NAME))
                    .withDate(rs.getDate(OPF_DATE_COLUMN_NAME));

            for (OpenPensionFundRowMapper mapper : rowMappers) {
                mapper.mapRow(rs, builder);
            }

            return new OpenPensionFund(builder);
        }
    }
}
