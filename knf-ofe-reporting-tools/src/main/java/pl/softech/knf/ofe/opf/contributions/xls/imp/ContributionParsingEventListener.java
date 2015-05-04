package pl.softech.knf.ofe.opf.contributions.xls.imp;

import pl.softech.knf.ofe.shared.xls.parser.ParsingEventListener;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface ContributionParsingEventListener extends ParsingEventListener {

    void record(final String name, final double amount, final double interests, final long number, final double averageBasis);

    void total(final double amount, final double interests, final long number, final double averageBasis);

}
