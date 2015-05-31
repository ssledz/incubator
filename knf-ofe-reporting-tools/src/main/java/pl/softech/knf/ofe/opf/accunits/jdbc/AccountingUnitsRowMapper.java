package pl.softech.knf.ofe.opf.accunits.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountingUnitsRowMapper implements OpenPensionFundRowMapper {

    private static final String OPFNET_ASSETS_COLUMN_NAME = "opf_acc_unit";

    @Override
    public void mapRow(ResultSet rs, OpenPensionFund.Builder builder) throws SQLException {
        builder.withAccountingUnitValue(
                requireNonNull(rs.getLong(OPFNET_ASSETS_COLUMN_NAME))
        );
    }

}
