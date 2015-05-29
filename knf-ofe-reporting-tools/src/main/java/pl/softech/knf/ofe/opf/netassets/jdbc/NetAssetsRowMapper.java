package pl.softech.knf.ofe.opf.netassets.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class NetAssetsRowMapper implements OpenPensionFundRowMapper {

    private static final String OPFNET_ASSETS_COLUMN_NAME = "opf_net_assets";

    @Override
    public void mapRow(ResultSet rs, OpenPensionFund.Builder builder) throws SQLException {
        builder.withNetAssets(
                requireNonNull(rs.getLong(OPFNET_ASSETS_COLUMN_NAME))
        );
    }

}
