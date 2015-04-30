package pl.softech.knf.ofe.opf.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;
import pl.softech.knf.ofe.shared.jdbc.RowMapper;

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

    @Inject
    public JdbcOpenPensionFundRepository(Set<DatabasePopulator> populators, Set<OpenPensionFundRowMapper> rowMappers, JdbcTemplate jdbcTemplate) {
        this.populators = populators;
        this.rowMappers = rowMappers;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OpenPensionFund> findAll() {
        return jdbcTemplate.query("SELECT * FROM open_pension_fund", new MyRowMapper());
    }

    @Override
    public void save(List<OpenPensionFund> opfs) {
        for (OpenPensionFund fund : opfs) {
            for (DatabasePopulator populator : populators) {
                populator.populate(fund);
            }
        }
    }


    private class MyRowMapper implements RowMapper<OpenPensionFund> {

        @Override
        public OpenPensionFund mapRow(ResultSet rs) throws SQLException {

            OpenPensionFund.Builder builder = new OpenPensionFund.Builder();

            for (OpenPensionFundRowMapper mapper : rowMappers) {
                mapper.mapRow(rs, builder);
            }

            return new OpenPensionFund(builder);
        }
    }
}
