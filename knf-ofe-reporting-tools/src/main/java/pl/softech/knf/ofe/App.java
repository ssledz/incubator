package pl.softech.knf.ofe;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.softech.knf.ofe.opf.OpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepository;
import pl.softech.knf.ofe.opf.xls.XlsOpenPensionFundRepositoryFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static final Injector INJECTOR = Guice.createInjector(new ApplicationModule());

	public static void main(final String[] args) {
		LOGGER.info("Starting...");

		final OpenPensionFundRepository jdbcRepository = INJECTOR.getInstance(Key.get(OpenPensionFundRepository.class, Jdbc.class));
		final XlsOpenPensionFundRepositoryFactory xlsRepositoryFactory = INJECTOR.getInstance(XlsOpenPensionFundRepositoryFactory.class);

		final XlsOpenPensionFundRepository xlsRepository = xlsRepositoryFactory.create(new File(
				"/home/ssledz/knf-ofe-work-dir/work/dane0402_tcm75-4044.xls"));
		
		xlsRepository.findAll().stream().forEach(fund -> {
			jdbcRepository.save(fund);
		});

	}
}
