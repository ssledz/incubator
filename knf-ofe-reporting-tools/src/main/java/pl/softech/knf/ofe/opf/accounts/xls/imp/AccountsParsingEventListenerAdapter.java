package pl.softech.knf.ofe.opf.accounts.xls.imp;

import java.util.Date;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
class AccountsParsingEventListenerAdapter implements AccountsParsingEventListener {
    @Override
    public void record(String name, long numberOfAccounts, long numberOfInactiveAccounts) {
    }

    @Override
    public void total(long totalNumberOfAccounts, long totalNumberOfInactiveAccounts) {
    }

    @Override
    public void date(Date date) {
    }

    @Override
    public void header(String[] columns) {
    }
}
