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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class JdbcUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(JdbcUtils.class);

	private static void close(final AutoCloseable closable, final String task, final String errorMessage,
			final SQLExceptionTranslator exceptionTranslator) {

		if (closable == null) {
			return;
		}

		try {
			closable.close();
		} catch (final SQLException e) {
			if (exceptionTranslator != null) {
				throw exceptionTranslator.translate(task, null, e);
			} else {
				LOGGER.error(errorMessage, e);
			}

		} catch (final Exception e) {
			LOGGER.error(errorMessage, e);
		}
	}

	/**
	 * Close the given JDBC Statement
	 * 
	 * @param stmt
	 *            the JDBC Statement to close (may be {@code null})
	 */
	public static void closeStatement(final Statement stmt, final SQLExceptionTranslator exceptionTranslator) {
		close(stmt, "Closing statement", "Could not close JDBC Statement", exceptionTranslator);
	}

	/**
	 * Close the given JDBC Connection
	 * 
	 * @param stmt
	 *            the JDBC Connection to close (may be {@code null})
	 */
	public static void closeConnection(final Connection con, final SQLExceptionTranslator exceptionTranslator) {
		close(con, "Closing connection", "Could not close JDBC Connection", exceptionTranslator);
	}
	
	/**
	 * Close the given JDBC ResultSet
	 * 
	 * @param stmt
	 *            the JDBC ResultSet to close (may be {@code null})
	 */
	public static void closeResultSet(final ResultSet rs, final SQLExceptionTranslator exceptionTranslator) {
		close(rs, "Closing resultSet", "Could not close JDBC ResultSet", exceptionTranslator);
	}
}
