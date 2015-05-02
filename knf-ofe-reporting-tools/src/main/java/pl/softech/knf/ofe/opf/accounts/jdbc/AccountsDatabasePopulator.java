package pl.softech.knf.ofe.opf.accounts.jdbc;

import com.google.common.eventbus.EventBus;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.event.LackOfDataEvent;
import pl.softech.knf.ofe.opf.jdbc.DatabasePopulator;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsDatabasePopulator implements DatabasePopulator {

    private final EventBus eventBus;

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public AccountsDatabasePopulator(final DataSource dataSource, EventBus eventBus) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.eventBus = eventBus;
    }

    @Override
    public void populate(OpenPensionFund fund) {

        if(fund.getNumberOfAccounts() == null) {
            eventBus.post(new LackOfDataEvent("There is no numberOfAccounts in {0}", fund));
            return;
        }

        final int updated = jdbcTemplate.update("UPDATE open_pension_fund " +
                        "SET opf_total_number_of_accounts = ?, opf_inactive_number_of_accounts = ? " +
                        "WHERE opf_name = ? AND opf_date = ?",
                fund.getNumberOfAccounts().getTotal(),
                fund.getNumberOfAccounts().getInactive(),
                fund.getName(), fund.getDate());

        if (updated == 0) {
            jdbcTemplate.update("INSERT INTO open_pension_fund (opf_name, opf_date, opf_total_number_of_accounts, " +
                            "opf_inactive_number_of_accounts) VALUES (?,?,?, ?)",
                    fund.getName(), fund.getDate(), fund.getNumberOfAccounts().getTotal(), fund.getNumberOfAccounts().getInactive());
        }

    }
}
