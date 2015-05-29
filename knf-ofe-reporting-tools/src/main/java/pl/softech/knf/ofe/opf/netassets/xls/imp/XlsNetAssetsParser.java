package pl.softech.knf.ofe.opf.netassets.xls.imp;

import com.google.common.eventbus.EventBus;
import pl.softech.knf.ofe.shared.xls.parser.AbstractXlsParser;
import pl.softech.knf.ofe.shared.xls.parser.State;
import pl.softech.knf.ofe.shared.xls.parser.StateContext;
import pl.softech.knf.ofe.shared.xls.spec.CellHasIgnoreCaseStringPatternValue;
import pl.softech.knf.ofe.shared.xls.spec.CellHasIgnoreCaseStringValue;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfNumericType;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfStringType;

import java.util.Arrays;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsNetAssetsParser extends AbstractXlsParser<NetAssetsParsingEventListener> {

    public XlsNetAssetsParser(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected State createStartingState(StateContext context) {
        return new ParsingDateState(context,
                new ParsingHeaderState(context,
                        Arrays.asList(
                                new CellHasIgnoreCaseStringPatternValue("Open.* pension.* fund.*"),
                                new CellHasIgnoreCaseStringPatternValue("Net assets value.*")
                        ),
                        new GenericParsingRecordsState(context,
                                cells -> fireRecord(
                                        cells.get(0).getStringCellValue(),
                                        getNumericValue(cells.get(1))
                                ),
                                Arrays.asList(
                                        new CellIsOfStringType()
                                                .and(new CellHasIgnoreCaseStringPatternValue("Total.*").not())
                                                .and(new CellHasIgnoreCaseStringPatternValue("Razem.*").not()),
                                        new CellIsOfNumericType()
                                ),
                                new GenericParsingTotalState(context,
                                        cells -> fireTotal(getNumericValue(cells.get(1))),
                                        Arrays.asList(
                                                new CellIsOfStringType(),
                                                new CellIsOfNumericType().or(cellIsFormula)
                                        )
                                )
                        )

                )
        );
    }

    private void fireRecord(final String name, final double amount) {
        listeners.forEach(l -> l.record(name, amount));
    }

    private void fireTotal(final double amount) {
        listeners.forEach(l -> l.total(amount));
    }
}
