package pl.softech.knf.ofe.opf.contributions.jdbc;

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
public class ContributionDatabasePopulator implements DatabasePopulator {

    private final EventBus eventBus;

    private final JdbcTemplate jdbcTemplate;

    @Inject
    public ContributionDatabasePopulator(final DataSource dataSource, EventBus eventBus) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.eventBus = eventBus;
    }

    @Override
    public void populate(OpenPensionFund fund) {

        if (fund.getContribution() == null) {
            eventBus.post(new LackOfDataEvent("There is no contribution in {0}", fund));
            return;
        }

        final int updated = jdbcTemplate.update("UPDATE open_pension_fund " +
                        "SET opf_contr_amount = ?, opf_contr_interests = ?, opf_contr_number = ?, opf_contr_average_basis = ? " +
                        "WHERE opf_name = ? AND opf_date = ?",
                fund.getContribution().getAmount(),
                fund.getContribution().getInterests(),
                fund.getContribution().getNumber(),
                fund.getContribution().getAverageBasis(),
                fund.getName(), fund.getDate());

        if (updated == 0) {
            jdbcTemplate.update("INSERT INTO open_pension_fund (opf_name, opf_date, opf_contr_amount, " +
                            "opf_contr_interests, opf_contr_number, opf_contr_average_basis) VALUES (?,?,?,?,?,?)",
                    fund.getName(), fund.getDate(), fund.getContribution().getAmount(), fund.getContribution().getInterests(),
                    fund.getContribution().getNumber(), fund.getContribution().getAverageBasis());
        }

    }
}
