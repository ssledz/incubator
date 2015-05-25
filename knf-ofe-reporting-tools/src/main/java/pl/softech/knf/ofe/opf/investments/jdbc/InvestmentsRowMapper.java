package pl.softech.knf.ofe.opf.investments.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.jdbc.OpenPensionFundRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsRowMapper implements OpenPensionFundRowMapper {

    @Override
    public void mapRow(ResultSet rs, OpenPensionFund.Builder builder) throws SQLException {

    }
}
