package pl.softech.knf.ofe;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
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

		final Option help = new Option("help", "print this message");
		final Option importOpfMembers = new Option("importOpfMembers", "import open pension fund members to db");

		final Options options = new Options();
		options.addOption(help);
		options.addOption(importOpfMembers);

		final CommandLineParser parser = new PosixParser();
		try {
			final CommandLine line = parser.parse(options, args);

			if (line.hasOption("help") || line.getOptions().length == 0) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("ofe-tool", options);
				System.out.println();
				return;
			}

			if (line.hasOption("importOpfMembers")) {
				executor.addTask(provider.get());
			}

			for (final String file : line.getArgs()) {
				LOGGER.debug("Adding file {} to executor", new File(file).getAbsoluteFile());
				executor.addPayload(new File(file));
			}

		} catch (final Exception exp) {
			LOGGER.error("Parsing failed.  Reason: " + exp.getMessage());
		}

		executor.execute();

	}
}
