package pl.softech.knf.ofe;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.softech.knf.ofe.opf.OpenPensionFundDbExportTask;
import pl.softech.knf.ofe.opf.OpenPensionFundDbImportTask;
import pl.softech.knf.ofe.shared.task.TaskExecutor;
import pl.softech.knf.ofe.shared.task.TaskExecutor.Payload;

import java.io.File;

public class App {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private static final Injector INJECTOR = Guice.createInjector(new ApplicationModule());

    private static OpenPensionFundDbImportTask newDbImportTask() {
        return INJECTOR.getInstance(OpenPensionFundDbImportTask.class);
    }

    private static OpenPensionFundDbExportTask newDbExportTask() {
        return INJECTOR.getInstance(OpenPensionFundDbExportTask.class);
    }

    public static void main(final String[] args) {
        LOGGER.info("Starting...");

        final TaskExecutor executor = new TaskExecutor();

        final Option help = new Option("help", "print this message");
        final Option importOpfMembers = new Option("import", "import open pension fund data to db");
        final Option exportOpfMembers = OptionBuilder.withArgName("fileName").hasArg()
                .withDescription("exports open pension fund data from db to xls file").create("export");

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

            if (line.hasOption("import")) {
                importOpfMembersPayload = executor.addTask(newDbImportTask());
            }

            if (line.hasOption("export")) {
                final String fileName = line.getOptionValue("export");
                if (fileName == null) {
                    System.out.println("No fileName argument");
                    return;
                }
                executor.addTask(newDbExportTask()).addPayload(new File(fileName));
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
