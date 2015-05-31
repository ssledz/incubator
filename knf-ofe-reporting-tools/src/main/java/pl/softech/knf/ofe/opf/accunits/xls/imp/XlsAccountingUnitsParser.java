package pl.softech.knf.ofe.opf.accunits.xls.imp;

import com.google.common.eventbus.EventBus;
import pl.softech.knf.ofe.shared.xls.parser.AbstractXlsParser;
import pl.softech.knf.ofe.shared.xls.parser.State;
import pl.softech.knf.ofe.shared.xls.parser.StateContext;
import pl.softech.knf.ofe.shared.xls.spec.CellHasIgnoreCaseStringPatternValue;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfNumericType;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfStringType;

import java.util.Arrays;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsAccountingUnitsParser extends AbstractXlsParser<AccountingUnitsParsingEventListener> {

    public XlsAccountingUnitsParser(EventBus eventBus) {
        super(eventBus);
    }

    @Override
    protected State createStartingState(StateContext context) {
        return new ParsingDateState(context,
                new ParsingHeaderState(context,
                        Arrays.asList(
                                new CellHasIgnoreCaseStringPatternValue("Open.* pension.* fund.*"),
                                new CellHasIgnoreCaseStringPatternValue(".*Wartość.* .*jednostki.* .*rozrachunkowej.*")
                                        .or(new CellHasIgnoreCaseStringPatternValue(".*Accounting.* unit.* .*value.*"))
                        ),
                        new GenericParsingRecordsState(context,
                                cells -> fireRecord(
                                        cells.get(0).getStringCellValue(),
                                        getNumericValue(cells.get(1))
                                ),
                                Arrays.asList(
                                        new CellIsOfStringType()
                                                .and(new CellHasIgnoreCaseStringPatternValue(".*Średnia.* .*ważona.*").not())
                                                .and(new CellHasIgnoreCaseStringPatternValue(".*Total.*").not())
                                                .and(new CellHasIgnoreCaseStringPatternValue(".*Razem.*").not())
                                                .and(new CellHasIgnoreCaseStringPatternValue(".*Weighted.* .*Average.*").not()),
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

    private void fireRecord(final String name, final double accountingUnitValue) {
        listeners.forEach(l -> l.record(name, accountingUnitValue));
    }

    private void fireTotal(final double accountingUnitValue) {
        listeners.forEach(l -> l.total(accountingUnitValue));
    }
}
