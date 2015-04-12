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
package pl.softech.knf.ofe.opf.jdbc;

import java.util.List;

import javax.inject.Inject;
import javax.sql.DataSource;

import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.shared.jdbc.JdbcTemplate;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class JdbcOpenPensionFundRepository implements OpenPensionFundRepository {

	private final JdbcTemplate jdbcTemplate;

	@Inject
	public JdbcOpenPensionFundRepository(final DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<OpenPensionFund> findAll() {
		return jdbcTemplate.query("SELECT * FROM open_pension_fund", new OpenPensionFundRowMapper());
	}

	@Override
	public void save(final OpenPensionFund opf) {
		final int updated = jdbcTemplate.update("UPDATE open_pension_fund "
				+ "SET opf_name = ?, opf_number_of_members = ?, opf_date = ? WHERE opf_name = ? AND opf_date = ?", opf.getName(),
				opf.getNumberOfMembers(), opf.getDate(), opf.getName(), opf.getDate());

		if (updated == 0) {
			jdbcTemplate.update("INSERT INTO open_pension_fund (opf_name, opf_number_of_members, opf_date) VALUES (?,?,?)", opf.getName(),
					opf.getNumberOfMembers(), opf.getDate());
		}
	}
}
