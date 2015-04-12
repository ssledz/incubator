package pl.softech.knf.ofe;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.softech.knf.ofe.opf.OpenPensionFundDbImportTaskProvider;
import pl.softech.knf.ofe.shared.task.TaskExecutor;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static final Injector INJECTOR = Guice.createInjector(new ApplicationModule());

	public static void main(final String[] args) {
		LOGGER.info("Starting...");

		final OpenPensionFundDbImportTaskProvider provider = INJECTOR.getInstance(OpenPensionFundDbImportTaskProvider.class);
		final TaskExecutor executor = new TaskExecutor();
		
		executor.addPayload(new File("/home/ssledz/knf-ofe-work-dir/work/dane0402_tcm75-4044.xls"));
		executor.addTask(provider.get());
		
		executor.execute();
		
	}
}
