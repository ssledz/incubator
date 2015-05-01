package pl.softech.knf.ofe.opf.event;

import org.slf4j.Logger;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class LackOfDataEvent {

    private final String messageFormat;
    private final Object[] args;

    /**
     * @param messageFormat format accepted by java.text.MessageFormat
     */
    public LackOfDataEvent(String messageFormat, Object... args) {
        this.messageFormat = messageFormat;
        this.args = args;
    }

    public void log(Logger logger, File file) {
        logger.warn("Lack of data in {}. " + MessageFormat.format(messageFormat, args), file.getAbsolutePath());
    }
}
