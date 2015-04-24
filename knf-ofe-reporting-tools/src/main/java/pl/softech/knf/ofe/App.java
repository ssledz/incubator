package pl.softech.knf.ofe;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.softech.knf.ofe.opf.members.MembersDbExportTaskProvider;
import pl.softech.knf.ofe.opf.members.MembersDbImportTaskProvider;
import pl.softech.knf.ofe.shared.task.Task;
import pl.softech.knf.ofe.shared.task.TaskExecutor;
import pl.softech.knf.ofe.shared.task.TaskExecutor.Payload;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class App {

	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

	private static final Injector INJECTOR = Guice.createInjector(new ApplicationModule());

	public static void main(final String[] args) {
		LOGGER.info("Starting...");

		final MembersDbImportTaskProvider opfFundImportTaskProvider = INJECTOR.getInstance(MembersDbImportTaskProvider.class);
		final MembersDbExportTaskProvider opfFundExportTaskProvider = INJECTOR.getInstance(MembersDbExportTaskProvider.class);
		final TaskExecutor executor = new TaskExecutor();

		final Option help = new Option("help", "print this message");
		final Option importOpfMembers = new Option("importOpfMembers", "import open pension fund members to db");
		final Option exportOpfMembers = OptionBuilder.withArgName("fileName").hasArg()
				.withDescription("exports open pension fund members from db to xls file").create("exportOpfMembers");

		final Options options = new Options();
		options.addOption(help);
		options.addOption(importOpfMembers);
		options.addOption(exportOpfMembers);

		Payload importOpfMembersPayload = TaskExecutor.EMPTY_PAYLOAD;

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
				importOpfMembersPayload = executor.addTask(opfFundImportTaskProvider.get());
			}

			if (line.hasOption("exportOpfMembers")) {
				final String fileName = line.getOptionValue("exportOpfMembers");
				if (fileName == null) {
					System.out.println("No fileName argument");
					return;
				}
				final Task task = opfFundExportTaskProvider.get();
				executor.addTask(task).addPayload(new File(fileName));
			}

			for (final String file : line.getArgs()) {
				LOGGER.debug("Adding file {} to {} payload", new File(file).getAbsoluteFile(), "importOpfMembersPayload");
				importOpfMembersPayload.addPayload(new File(file));
			}

		} catch (final Exception exp) {
			LOGGER.error("Parsing failed.  Reason: " + exp.getMessage());
		}

		try {
			executor.execute();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
