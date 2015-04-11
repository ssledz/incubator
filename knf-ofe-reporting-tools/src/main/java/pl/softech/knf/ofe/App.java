package pl.softech.knf.ofe;

import java.io.File;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.softech.knf.ofe.opf.jdbc.JdbcOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static DataSource dataSource() {
		final ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setJdbcUrl("jdbc:mysql://localhost:3306/knf_ofe?createDatabaseIfNotExist=true&sessionVariables=sql_mode=NO_BACKSLASH_ESCAPES&useUnicode=yes&characterEncoding=UTF-8&characterSetResults=utf8&connectionCollation=utf8_unicode_ci");
		cpds.setUser("test");
		cpds.setPassword("test");
		return cpds;
	}

	public static void main(final String[] args) {
		LOGGER.info("Starting...");

		final XlsOpenPensionFundRepository xlsRepository = new XlsOpenPensionFundRepository(new File(
				"/home/ssledz/knf-ofe-work-dir/work/dane0402_tcm75-4044.xls"));

		final JdbcOpenPensionFundRepository jdbcRepository = new JdbcOpenPensionFundRepository(dataSource());

		xlsRepository.findAll().stream().forEach(fund -> {
			jdbcRepository.save(fund);
		});

	}
}
