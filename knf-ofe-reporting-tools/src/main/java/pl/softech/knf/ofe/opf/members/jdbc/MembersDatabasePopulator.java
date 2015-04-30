package pl.softech.knf.ofe.opf.members.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.jdbc.DatabasePopulator;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class MembersDatabasePopulator implements DatabasePopulator {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public MembersDatabasePopulator(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void populate(OpenPensionFund fund) {

        final int updated = jdbcTemplate.update("UPDATE open_pension_fund " +
                        "SET opf_number_of_members = ? " +
                        "WHERE opf_name = ? AND opf_date = ?",
                fund.getNumberOfMembers(),
                fund.getName(), fund.getDate());

        if (updated == 0) {
            jdbcTemplate.update("INSERT INTO open_pension_fund (opf_name, opf_date, opf_number_of_members) VALUES (?,?,?)",
                    fund.getName(), fund.getDate(), fund.getNumberOfMembers());
        }

    }
}
