package pl.softech.knf.ofe.opf.contributions.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.contributions.Contribution;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class ContributionRowMapper implements OpenPensionFundRowMapper {

    private static final String OPF_CONTR_AMOUNT_COLUMN_NAME = "opf_contr_amount";
    private static final String OPF_CONTR_INTEREST_COLUMN_NAME = "opf_contr_interests";
    private static final String OPF_CONTR_NUMBER_COLUMN_NAME = "opf_contr_number";
    private static final String OPF_CONTR_AVERAGE_BASIS_COLUMN_NAME = "opf_contr_average_basis";


    @Override
    public void mapRow(ResultSet rs, OpenPensionFund.Builder builder) throws SQLException {
        builder.withName(rs.getString(OPF_NAME_COLUMN_NAME))
                .withDate(rs.getDate(OPF_DATE_COLUMN_NAME))
                .withContribution(new Contribution.Builder()
                                .withAmount(rs.getLong(OPF_CONTR_AMOUNT_COLUMN_NAME))
                                .withInterests(rs.getLong(OPF_CONTR_INTEREST_COLUMN_NAME))
                                .withNumber(rs.getLong(OPF_CONTR_NUMBER_COLUMN_NAME))
                                .withAverageBasis(rs.getLong(OPF_CONTR_AVERAGE_BASIS_COLUMN_NAME))
                );
    }
}
