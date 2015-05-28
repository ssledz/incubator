package pl.softech.knf.ofe.opf.event;

import org.slf4j.Logger;

import java.io.File;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class ParserNotInEndingStateAfterFinish {

    private final Class<?> source;

    public ParserNotInEndingStateAfterFinish(Class<?> source) {
        this.source = source;
    }

    public void log(Logger logger, File file) {
        logger.warn("Parser {} not in ending state after parsing file {} ", source.getName(), file.getAbsolutePath
                ());
    }
}
