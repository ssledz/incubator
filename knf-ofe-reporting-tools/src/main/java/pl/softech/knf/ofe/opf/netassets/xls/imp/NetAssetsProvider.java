package pl.softech.knf.ofe.opf.netassets.xls.imp;

import com.google.common.eventbus.EventBus;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import pl.softech.knf.ofe.opf.OpenPensionFund;
import pl.softech.knf.ofe.opf.OpenPensionFundDateAdjuster;
import pl.softech.knf.ofe.opf.OpenPensionFundNameTranslator;
import pl.softech.knf.ofe.opf.xls.AbstractDataPopulator;
import pl.softech.knf.ofe.opf.xls.DataPopulator;
import pl.softech.knf.ofe.opf.xls.DataProvider;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static pl.softech.knf.ofe.shared.xls.XlsUtils.getSheetByPattern;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class NetAssetsProvider implements DataProvider {

    private static final Pattern SHEET_NAME_PATTERN = Pattern.compile(".*\\s?Net assets\\s?.*", Pattern.CASE_INSENSITIVE);

    private final OpenPensionFundNameTranslator nameTranslator;
    private final OpenPensionFundDateAdjuster dateAdjuster;
    private final EventBus eventBus;

    @Inject
    public NetAssetsProvider(OpenPensionFundNameTranslator nameTranslator, OpenPensionFundDateAdjuster dateAdjuster, EventBus eventBus) {
        this.nameTranslator = nameTranslator;
        this.dateAdjuster = dateAdjuster;
        this.eventBus = eventBus;
    }

    @Override
    public Iterator<? extends DataPopulator> iterator(Workbook wb) {

        XlsNetAssetsParser parser = new XlsNetAssetsParser(eventBus);

        List<OpenPensionFund> funds = new LinkedList<>();

        parser.addParsingEventListener(new NetAssetsParsingEventListenerAdapter() {
            private Date date;

            @Override
            public void date(Date date) {
                this.date = dateAdjuster.adjust(date);
            }

            @Override
            public void record(String name, double amount) {
                funds.add(new OpenPensionFund(new OpenPensionFund.Builder()
                        .withDate(date)
                        .withName(nameTranslator.translate(name))
                        .withNetAssets(amount)
                ));
            }
        });

        Sheet sheet = getSheetByPattern(wb, SHEET_NAME_PATTERN);

        if (sheet == null) {
            return new ArrayList<DataPopulator>().iterator();
        }

        parser.parseSheet(sheet);

        return funds
                .stream()
                .map(f -> new MyDataPopulator(f))
                .collect(Collectors.toList())
                .iterator();
    }

    private static class MyDataPopulator extends AbstractDataPopulator {

        public MyDataPopulator(OpenPensionFund fund) {
            super(fund);
        }

        @Override
        public void populate(OpenPensionFund.Builder builder) {
            builder.withNetAssets(fund.getNetAssets());
        }
    }
}
