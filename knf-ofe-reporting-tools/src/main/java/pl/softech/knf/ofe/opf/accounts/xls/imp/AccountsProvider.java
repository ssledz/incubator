package pl.softech.knf.ofe.opf.accounts.xls.imp;

import com.google.common.eventbus.EventBus;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.*;
import pl.softech.knf.ofe.opf.accounts.NumberOfAccounts;
import pl.softech.knf.ofe.opf.xls.AbstractDataPopulator;
import pl.softech.knf.ofe.opf.xls.DataPopulator;
import pl.softech.knf.ofe.opf.xls.DataProvider;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class AccountsProvider implements DataProvider {

    private static final String ACCOUNTS_SHEET_NAME = "Accounts";
    private static final String ACCOUNTS_SHEET_NAME2 = "II Accounts";

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;
    private final EventBus eventBus;

    @Inject
    public AccountsProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster, EventBus eventBus) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
        this.eventBus = eventBus;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        XlsAccountsParser parser = new XlsAccountsParser(eventBus);

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

        if (sheet == null) {
            sheet = wb.getSheet(ACCOUNTS_SHEET_NAME2);
        }

        if (sheet == null) {
            return new ArrayList<DataPopulator>().iterator();
        }

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
            NumberOfAccounts numberOfAccounts = fund.getNumberOfAccounts();
            builder.withNumberOfAccount(numberOfAccounts.getTotal(), numberOfAccounts.getInactive());
        }
    }
}
