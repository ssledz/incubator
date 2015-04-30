package pl.softech.knf.ofe.opf.accounts.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsRowMapper implements OpenPensionFundRowMapper {

    private static final String OPF_NUMBER_OF_TOTAL_ACCOUNTS_COLUMN_NAME = "opf_total_number_of_accounts";
    private static final String OPF_NUMBER_OF_INACTIVE_ACCOUNTS_COLUMN_NAME = "opf_inactive_number_of_accounts";

    @Override
    public void mapRow(ResultSet rs, OpenPensionFund.Builder builder) throws SQLException {
        builder.withName(rs.getString(OPF_NAME_COLUMN_NAME))
                .withDate(rs.getDate(OPF_DATE_COLUMN_NAME))
                .withNumberOfAccount(requireNonNull(rs.getLong(OPF_NUMBER_OF_TOTAL_ACCOUNTS_COLUMN_NAME)),
                        requireNonNull(rs.getLong(OPF_NUMBER_OF_INACTIVE_ACCOUNTS_COLUMN_NAME)));
    }
}
