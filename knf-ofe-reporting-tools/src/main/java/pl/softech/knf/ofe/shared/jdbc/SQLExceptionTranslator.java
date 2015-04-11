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

import java.sql.SQLException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface SQLExceptionTranslator {

	/**
	 * Translate the given {@link SQLException} into a generic
	 * {@link DataAccessException}.
	 * <p>
	 * The returned DataAccessException is supposed to contain the original
	 * {@code SQLException} as root cause.
	 * 
	 * @param task
	 *            readable text describing the task being attempted
	 * @param sql
	 *            SQL query or update that caused the problem (may be
	 *            {@code null})
	 * @param exception
	 *            the offending {@code SQLException}
	 * @return the DataAccessException, wrapping the {@code SQLException}
	 */
	DataAccessException translate(String task, String sql, SQLException exception);
}
