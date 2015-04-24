/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe.opf.members.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Objects.*;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.shared.jdbc.RowMapper;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class MembersRowMapper implements RowMapper<OpenPensionFund> {

    private static final String OPF_DATE_COLUMN_NAME = "opf_date";
    private static final String OPF_NUMBER_OF_MEMBERS_COLUMN_NAME = "opf_number_of_members";
    private static final String OPF_NAME_COLUMN_NAME = "opf_name";

    @Override
    public OpenPensionFund mapRow(final ResultSet rs) throws SQLException {
        return new OpenPensionFund(new OpenPensionFund.Builder()
                .withName(rs.getString(OPF_NAME_COLUMN_NAME))
                .withDate(rs.getDate(OPF_DATE_COLUMN_NAME))
                .withNumberOfMembers(requireNonNull(rs.getLong(OPF_NUMBER_OF_MEMBERS_COLUMN_NAME)))
        );
    }

}