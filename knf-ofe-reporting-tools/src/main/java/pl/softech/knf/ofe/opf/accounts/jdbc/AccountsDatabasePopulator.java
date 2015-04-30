package pl.softech.knf.ofe.opf.accounts.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.jdbc.DatabasePopulator;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsDatabasePopulator implements DatabasePopulator {

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public AccountsDatabasePopulator(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void populate(OpenPensionFund fund) {

        final int updated = jdbcTemplate.update("UPDATE open_pension_fund " +
                        "SET opf_total_number_of_accounts = ?, opf_inactive_number_of_accounts = ? " +
                        "WHERE opf_name = ? AND opf_date = ?",
                fund.getNumberOfAccount().getTotal(),
                fund.getNumberOfAccount().getInactive(),
                fund.getName(), fund.getDate());

        if (updated == 0) {
            jdbcTemplate.update("INSERT INTO open_pension_fund (opf_name, opf_date, opf_total_number_of_accounts, " +
                            "opf_inactive_number_of_accounts) VALUES (?,?,?, ?)",
                    fund.getName(), fund.getDate(), fund.getNumberOfAccount().getTotal(), fund.getNumberOfAccount().getInactive());
        }

    }
}
