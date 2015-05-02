package pl.softech.knf.ofe.opf.accounts.xls.imp;

import pl.softech.knf.ofe.shared.xls.parser.ParsingEventListener;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
interface AccountsParsingEventListener extends ParsingEventListener {

    void record(final String name, final long numberOfAccounts, final long numberOfInactiveAccounts);

    void total(final long totalNumberOfAccounts, final long totalNumberOfInactiveAccounts);

}
