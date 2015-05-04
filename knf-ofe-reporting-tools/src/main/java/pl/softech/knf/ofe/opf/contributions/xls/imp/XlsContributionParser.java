package pl.softech.knf.ofe.opf.contributions.xls.imp;

import pl.softech.knf.ofe.shared.xls.parser.AbstractXlsParser;
import pl.softech.knf.ofe.shared.xls.parser.State;
import pl.softech.knf.ofe.shared.xls.parser.StateContext;
import pl.softech.knf.ofe.shared.xls.spec.CellHasIgnoreCaseStringValue;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfNumericType;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfStringType;

import java.util.Arrays;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsContributionParser extends AbstractXlsParser<ContributionParsingEventListener> {

    @Override
    protected State createStartingState(StateContext context) {
        return new ParsingDateState(context,
                new ParsingHeaderState(context,
                        Arrays.asList(
                                new CellHasIgnoreCaseStringValue("Open Pension Fund"),
                                new CellHasIgnoreCaseStringValue("Amount of contribution\n(in ZL)")
                                        .or(new CellHasIgnoreCaseStringValue("amount of contribution\n(PLN)")),
                                new CellHasIgnoreCaseStringValue("Interests\n(in ZL)")
                                        .or(new CellHasIgnoreCaseStringValue("interests\n(PLN)")),
                                new CellHasIgnoreCaseStringValue("Number of contributions"),
                                new CellHasIgnoreCaseStringValue("Average contribution\n(in ZL)")
                                        .or(new CellHasIgnoreCaseStringValue("average contribution\n(PLN)")),
                                new CellHasIgnoreCaseStringValue("Average basis\n(in ZL)")
                                        .or(new CellHasIgnoreCaseStringValue("average basis\n(PLN)"))
                        ),
                        startingCell -> new GenericParsingRecordsState(context, startingCell,
                                cells -> fireRecord(
                                        cells.get(0).getStringCellValue(),
                                        getNumericValue(cells.get(1)),
                                        getNumericValue(cells.get(2)),
                                        getLongValue(cells.get(3)),
                                        getNumericValue(cells.get(5))
                                ),
                                Arrays.asList(
                                        new CellIsOfStringType()
                                                .and(new CellHasIgnoreCaseStringValue("Total").not())
                                                .and(new CellHasIgnoreCaseStringValue("Razem").not()),
                                        new CellIsOfNumericType(),
                                        new CellIsOfNumericType(),
                                        new CellIsOfNumericType(),
                                        new CellIsOfNumericType(),
                                        new CellIsOfNumericType()
                                ),
                                startingCell2 -> new GenericParsingTotalState(context, startingCell2,
                                        cells -> fireTotal(
                                                getNumericValue(cells.get(1)),
                                                getNumericValue(cells.get(2)),
                                                getLongValue(cells.get(3)),
                                                getNumericValue(cells.get(5))
                                        ),
                                        Arrays.asList(
                                                new CellIsOfStringType(),
                                                new CellIsOfNumericType().or(cellIsFormula),
                                                new CellIsOfNumericType().or(cellIsFormula),
                                                new CellIsOfNumericType().or(cellIsFormula),
                                                new CellIsOfNumericType().or(cellIsFormula),
                                                new CellIsOfNumericType().or(cellIsFormula)
                                        )
                                )
                        )

                )
        );
    }

    private void fireRecord(final String name, final double amount, final double interests, final long number, final double averageBasis) {
        listeners.forEach(l -> l.record(name.trim(), amount, interests, number, averageBasis));
    }

    private void fireTotal(final double amount, final double interests, final long number, final double averageBasis) {
        listeners.forEach(l -> l.total(amount, interests, number, averageBasis));
    }

}
