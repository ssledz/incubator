package pl.softech.knf.ofe.opf.contributions.xls.imp;

import com.google.common.eventbus.EventBus;
import pl.softech.knf.ofe.shared.xls.parser.AbstractXlsParser;
import pl.softech.knf.ofe.shared.xls.parser.State;
import pl.softech.knf.ofe.shared.xls.parser.StateBranch;
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
public class XlsContributionParser extends AbstractXlsParser<ContributionParsingEventListener> {

        public XlsContributionParser(EventBus eventBus) {
                super(eventBus);
        }

        @Override
    protected State createStartingState(StateContext context) {
        return new ParsingDateState(context,
                new StateBranch(context,
                        sixColumns(context),
                        fourColumns(context)
                )
        );
    }

    private State sixColumns(StateContext context) {
        return new ParsingHeaderState(context,
                Arrays.asList(
                        new CellHasIgnoreCaseStringValue("Open Pension Fund"),
                        new CellHasIgnoreCaseStringPatternValue("Amount of contribution.*"),
                        new CellHasIgnoreCaseStringPatternValue("Interests.*"),
                        new CellHasIgnoreCaseStringValue("Number of contributions"),
                        new CellHasIgnoreCaseStringPatternValue("Average contribution.*"),
                        new CellHasIgnoreCaseStringPatternValue("Average basis.*")
                ),
                new GenericParsingRecordsState(context,
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
                        new GenericParsingTotalState(context,
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

        );
    }

    private State fourColumns(StateContext context) {
        return new ParsingHeaderState(context,
                Arrays.asList(
                        new CellHasIgnoreCaseStringValue("Open Pension Fund"),
                        new CellHasIgnoreCaseStringPatternValue("Amount of contribution.*"),
                        new CellHasIgnoreCaseStringPatternValue("Interests.*"),
                        new CellHasIgnoreCaseStringValue("Number of contributions")
                ),
                new GenericParsingRecordsState(context,
                        cells -> fireRecord(
                                cells.get(0).getStringCellValue(),
                                getNumericValue(cells.get(1)),
                                getNumericValue(cells.get(2)),
                                getLongValue(cells.get(3)),
                                0
                        ),
                        Arrays.asList(
                                new CellIsOfStringType()
                                        .and(new CellHasIgnoreCaseStringValue("Total").not())
                                        .and(new CellHasIgnoreCaseStringValue("Razem").not()),
                                new CellIsOfNumericType(),
                                new CellIsOfNumericType(),
                                new CellIsOfNumericType()
                        ),
                        new GenericParsingTotalState(context,
                                cells -> fireTotal(
                                        getNumericValue(cells.get(1)),
                                        getNumericValue(cells.get(2)),
                                        getLongValue(cells.get(3)),
                                        0
                                ),
                                Arrays.asList(
                                        new CellIsOfStringType(),
                                        new CellIsOfNumericType().or(cellIsFormula),
                                        new CellIsOfNumericType().or(cellIsFormula),
                                        new CellIsOfNumericType().or(cellIsFormula)
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
