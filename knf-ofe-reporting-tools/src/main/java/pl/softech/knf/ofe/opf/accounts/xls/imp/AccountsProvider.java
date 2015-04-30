package pl.softech.knf.ofe.opf.accounts.xls.imp;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.*;
import pl.softech.knf.ofe.opf.accounts.NumberOfAccount;

import javax.inject.Inject;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsProvider implements DataProvider {

    private static final String ACCOUNTS_SHEET_NAME = "II Accounts";

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;

    @Inject
    public AccountsProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        XlsAccountsParser parser = new XlsAccountsParser();

        List<OpenPensionFund> funds = new LinkedList<>();

        parser.addParsingEventListener(new AccountsParsingEventListenerAdapter() {

            private Date date;

            @Override
            public void date(Date date) {
                this.date = dateAdjuster.adjust(date);
            }

            @Override
            public void record(String name, long numberOfAccounts, long numberOfInactiveAccounts) {
                funds.add(new OpenPensionFund(new OpenPensionFund.Builder()
                        .withDate(date)
                        .withName(nameTranslator.translate(name))
                        .withNumberOfAccount(numberOfAccounts, numberOfInactiveAccounts)
                ));
            }

        });

        Sheet sheet = wb.getSheet(ACCOUNTS_SHEET_NAME);

        parser.parseSheet(sheet);

        return funds
                .stream()
                .map(f -> new AccountDataPopulator(f))
                .collect(Collectors.toList())
                .iterator();
    }

    private static class AccountDataPopulator extends AbstractDataPopulator {

        public AccountDataPopulator(OpenPensionFund fund) {
            super(fund);
        }

        @Override
        public void populate(OpenPensionFund.Builder builder) {
            NumberOfAccount numberOfAccount = fund.getNumberOfAccount();
            builder.withNumberOfAccount(numberOfAccount.getTotal(), numberOfAccount.getInactive());
        }
    }
}
