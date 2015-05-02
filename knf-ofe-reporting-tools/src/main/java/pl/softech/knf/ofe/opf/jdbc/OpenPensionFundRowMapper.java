package pl.softech.knf.ofe.opf.jdbc;

import pl.softech.knf.ofe.opf.OpenPensionFund;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface OpenPensionFundRowMapper {

    static final String OPF_DATE_COLUMN_NAME = "opf_date";
    static final String OPF_NAME_COLUMN_NAME = "opf_name";

    void mapRow(final ResultSet rs, OpenPensionFund.Builder builder) throws SQLException;

}
