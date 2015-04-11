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

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class JdbcTemplate {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

	private final DataSource dataSource;

	/** If this variable is false, we will throw exceptions on SQL warnings */
	private boolean ignoreWarnings = true;

	/**
	 * If this variable is set to a non-zero value, it will be used for setting
	 * the fetchSize property on statements used for query processing.
	 */
	private int fetchSize = 0;

	/**
	 * If this variable is set to a non-zero value, it will be used for setting
	 * the maxRows property on statements used for query processing.
	 */
	private int maxRows = 0;

	private SQLExceptionTranslator exceptionTranslator = new DefaultSQLExceptionTranslator();

	public JdbcTemplate(final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @throws DataAccessException
	 */
	public <T> T execute(final ConnectionCallback<T> action) {

		requireNonNull(action);

		try (Connection con = dataSource.getConnection()) {
			applyConnectionSettings(con);
			return action.doInConnection(createConnectionProxy(con));
		} catch (final SQLException ex) {
			throw exceptionTranslator.translate("ConnectionCallback", getSql(action), ex);
		}
	}

	public <T> T execute(final StatementCallback<T> action) {

		requireNonNull(action);

		class ExecuteConnectionCallback implements ConnectionCallback<T>, SqlProvider {

			@Override
			public String getSql() {
				return JdbcTemplate.getSql(action);
			}

			@Override
			public T doInConnection(final Connection con) throws SQLException {

				try (Statement stmt = con.createStatement()) {

					final T result = action.doInStatement(stmt);
					handleWarnings(stmt);
					return result;
				} catch (final SQLException ex) {

					throw exceptionTranslator.translate("StatementCallback", JdbcTemplate.getSql(action), ex);
				}
			}
		}

		return execute(new ExecuteConnectionCallback());

	}

	public void execute(final String sql) {
		requireNonNull(sql);
		LOGGER.debug("Executing SQL statement [{}]", sql);
		class ExecuteStatementCallback implements StatementCallback<Object>, SqlProvider {
			@Override
			public Object doInStatement(final Statement stmt) throws SQLException {
				stmt.execute(sql);
				return null;
			}

			@Override
			public String getSql() {
				return sql;
			}
		}
		execute(new ExecuteStatementCallback());
	}

	public int update(final String sql) {
		requireNonNull(sql);
		LOGGER.debug("Executing SQL update [{}]", sql);

		class UpdateStatementCallback implements StatementCallback<Integer>, SqlProvider {
			@Override
			public Integer doInStatement(final Statement stmt) throws SQLException {
				final int rows = stmt.executeUpdate(sql);
				LOGGER.debug("SQL update affected {} rows", rows);
				return rows;
			}

			@Override
			public String getSql() {
				return sql;
			}
		}
		return execute(new UpdateStatementCallback());
	}

	public <T> T query(final String sql, final ResultSetExtractor<T> rse) {

		requireNonNull(sql, "SQL must not be null");
		requireNonNull(rse, "ResultSetExtractor must not be null");
		LOGGER.debug("Executing SQL query [{}]", sql);

		class QueryStatementCallback implements StatementCallback<T>, SqlProvider {
			@Override
			public T doInStatement(final Statement stmt) throws SQLException {
				try (ResultSet rs = stmt.executeQuery(sql)) {
					return rse.extractData(rs);
				}
			}

			@Override
			public String getSql() {
				return sql;
			}
		}
		return execute(new QueryStatementCallback());
	}

	public <T> List<T> query(final String sql, final RowMapper<T> rowMapper) {
		return query(sql, new RowMapperResultSetExtractor<T>(rowMapper));
	}

	private <T> T execute(final PreparedStatementCreator psc, final PreparedStatementCallback<T> action) {

		requireNonNull(psc, "PreparedStatementCreator must not be null");
		requireNonNull(action, "Callback object must not be null");
		LOGGER.debug("Executing prepared SQL statement [{}]", getSql(action));

		class ExecuteConnectionCallback implements ConnectionCallback<T>, SqlProvider {

			@Override
			public String getSql() {
				return JdbcTemplate.getSql(psc);
			}

			@Override
			public T doInConnection(final Connection con) throws SQLException {

				try (PreparedStatement ps = psc.createPreparedStatement(con)) {

					final T result = action.doInPreparedStatement(ps);
					handleWarnings(ps);
					return result;
				} catch (final SQLException ex) {
					throw exceptionTranslator.translate("StatementCallback", JdbcTemplate.getSql(psc), ex);
				}
			}
		}

		return execute(new ExecuteConnectionCallback());
	}

	public <T> T execute(final String sql, final PreparedStatementCallback<T> action) {
		return execute(new SimplePreparedStatementCreator(sql), action);
	}

	public <T> T query(final String sql, final PreparedStatementParameterSetter setter, final ResultSetExtractor<T> rse) {
		requireNonNull(setter, "PreparedStatementParameterSetter must not be null");
		requireNonNull(rse, "ResultSetExtractor must not be null");

		return execute(new SimplePreparedStatementCreator(sql), ps -> {

			setter.setParametters(ps);

			try (ResultSet rs = ps.executeQuery(sql)) {
				return rse.extractData(rs);
			}

		});
	}

	public <T> T query(final String sql, final Object[] params, final ResultSetExtractor<T> rse) {
		requireNonNull(params, "Parameters must not be null");
		return query(sql, ps -> {
			for (int i = 0; i < params.length; i++) {
				ps.setObject(i + 1, params[i]);
			}
		}, rse);
	}

	public <T> List<T> query(final String sql, final Object[] params, final RowMapper<T> rowMapper) {
		return query(sql, params, new RowMapperResultSetExtractor<T>(rowMapper));
	}

	public int update(final String sql, final PreparedStatementParameterSetter setter) {
		requireNonNull(setter, "PreparedStatementParameterSetter must not be null");
		return execute(new SimplePreparedStatementCreator(sql), new PreparedStatementCallback<Integer>() {

			@Override
			public Integer doInPreparedStatement(final PreparedStatement ps) throws SQLException {
				setter.setParametters(ps);
				return ps.executeUpdate();
			}
		});
	}

	public int update(final String sql, final Object... args) {
		return update(sql, ps -> {
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
		});
	}

	private static Object[] args(final Object... args) {
		return args;
	}

	/**
	 * @param stmt
	 * @throws SQLException
	 */
	private void handleWarnings(final Statement stmt) throws SQLException {
		if (!ignoreWarnings) {
			throw exceptionTranslator.translate("Warnings during executing Statement", null, stmt.getWarnings());
		}
		SQLWarning warningToLog = stmt.getWarnings();
		while (warningToLog != null) {
			LOGGER.debug("SQLWarning ignored: SQL state '{}', error code '{}', message [{}]",
					args(warningToLog.getSQLState(), warningToLog.getErrorCode(), warningToLog.getMessage()));
			warningToLog = warningToLog.getNextWarning();
		}

	}

	private Connection createConnectionProxy(final Connection con) {
		return (Connection) Proxy.newProxyInstance(JdbcTemplate.class.getClassLoader(), new Class<?>[] { Connection.class },
				new ConnectionInvocationHandler(con));
	}

	private void applyStatementSettings(final Statement stmt) throws SQLException {
		if (fetchSize > 0) {
			stmt.setFetchSize(fetchSize);
		}
		if (maxRows > 0) {
			stmt.setMaxRows(maxRows);
		}
	}

	private void applyConnectionSettings(final Connection con) {
	}

	/**
	 * Determine SQL from potential provider object.
	 * 
	 * @param sqlProvider
	 *            object that's potentially a SqlProvider
	 * @return the SQL string, or {@code null}
	 * @see SqlProvider
	 */
	private static String getSql(final Object sqlProvider) {
		if (sqlProvider instanceof SqlProvider) {
			return ((SqlProvider) sqlProvider).getSql();
		} else {
			return null;
		}
	}

	private class ConnectionInvocationHandler implements InvocationHandler {

		private final Connection target;

		public ConnectionInvocationHandler(final Connection target) {
			this.target = target;
		}

		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

			// Invoke method on target Connection.
			try {
				final Object retVal = method.invoke(this.target, args);

				if (retVal instanceof Statement) {
					applyStatementSettings(((Statement) retVal));
				}

				return retVal;
			} catch (final InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
	}

	private static class DefaultSQLExceptionTranslator implements SQLExceptionTranslator {
		@Override
		public DataAccessException translate(final String task, final String sql, final SQLException exception) {

			final StringBuilder builder = new StringBuilder();
			builder.append(task);
			if (sql != null) {
				builder.append(" [").append(sql).append("]");
			}

			return new DataAccessException(builder.toString(), exception);
		}

	}

	private static class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

		private final String sql;

		public SimplePreparedStatementCreator(final String sql) {
			requireNonNull(sql, "SQL must not be null");
			this.sql = sql;
		}

		@Override
		public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
			return con.prepareStatement(this.sql);
		}

		@Override
		public String getSql() {
			return this.sql;
		}
	}

}
