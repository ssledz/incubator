package pl.softech.knf.ofe.opf.event;

import org.slf4j.Logger;

import java.text.MessageFormat;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class DatabasePopulatorErrorEvent {

    private final String messageFormat;
    private final Exception exception;
    private final Object[] args;

    /**
     * @param messageFormat format accepted by java.text.MessageFormat
     */
    public DatabasePopulatorErrorEvent(Exception exception, String messageFormat, Object... args) {
        this.messageFormat = messageFormat;
        this.exception = exception;
        this.args = args;
    }

    public void log(Logger logger) {
        logger.error(MessageFormat.format(messageFormat, args), exception);
    }
}
