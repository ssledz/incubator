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
package pl.softech.knf.ofe.shared.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface PreparedStatementCallback<T> {
	/**
	 * Gets called by {@code JdbcTemplate.execute} with an active JDBC
	 * PreparedStatement. Does not need to care about closing the Statement
	 * or the Connection.
	 *
	 * <p><b>NOTE:</b> Any ResultSets opened should be closed in finally blocks
	 * within the callback implementation. 
	 *
	 * @param ps active JDBC PreparedStatement
	 * @return a result object, or {@code null} if none
	 * @throws SQLException if thrown by a JDBC method
	 */
	T doInPreparedStatement(PreparedStatement ps) throws SQLException;
}
