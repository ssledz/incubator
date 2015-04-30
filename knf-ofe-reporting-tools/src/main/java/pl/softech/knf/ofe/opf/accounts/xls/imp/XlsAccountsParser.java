package pl.softech.knf.ofe.opf.accounts.xls.imp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import pl.softech.knf.ofe.shared.spec.Specification;
import pl.softech.knf.ofe.shared.xls.parser.AbstractState;
import pl.softech.knf.ofe.shared.xls.parser.AbstractXlsParser;
import pl.softech.knf.ofe.shared.xls.parser.State;
import pl.softech.knf.ofe.shared.xls.parser.StateContext;
import pl.softech.knf.ofe.shared.xls.spec.*;

import java.util.Iterator;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
class XlsAccountsParser extends AbstractXlsParser<AccountsParsingEventListener> {

    @Override
    protected State createStartingState(final StateContext context) {
        return new ParsingDateState(context, new ParsingFirstRowOfHeaderState(context));
    }

    private void fireRecord(final String name, final long numberOfAccounts, final long numberOfInactiveAccounts) {
        listeners.forEach(l -> l.record(name.trim(), numberOfAccounts, numberOfInactiveAccounts));
    }

    private void fireTotal(final long totalNumberOfAccounts, final long totalNumberOfInactiveAccounts) {
        listeners.forEach(l -> l.total(totalNumberOfAccounts, totalNumberOfInactiveAccounts));
    }

    private class ParsingFirstRowOfHeaderState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellHasIgnoreCaseStringValue("Open Pension Fund");
        private final Specification<Cell> secondColumnSpecification = new CellHasIgnoreCaseStringValue("Number of accounts");

        protected ParsingFirstRowOfHeaderState(StateContext context) {
            super(context);
        }

        @Override
        public void parse(Row row) {
            final Iterator<Cell> it = row.iterator();
            int cellCnt = 0;
            while (it.hasNext()) {
                final Cell firstCell = it.next();
                if (firstColumnSpecification.isSatisfiedBy(firstCell) && it.hasNext()) {
                    final Cell secondCell = it.next();
                    if (secondColumnSpecification.isSatisfiedBy(secondCell)) {
                        fireHeader(firstCell.getStringCellValue(), secondCell.getStringCellValue());
                        context.setState(new ParsingSecondRowOfHeaderState(context, cellCnt));
                        break;
                    }
                }
                cellCnt++;
            }
        }
    }

    private class ParsingSecondRowOfHeaderState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellIsEmpty();
        private final Specification<Cell> secondColumnSpecification = new CellHasIgnoreCaseStringValue("total");
        private final Specification<Cell> thirdColumnSpecification = new CellHasIgnoreCaseStringValue("including \"inactive accounts\"");

        private final int startCellIndex;

        protected ParsingSecondRowOfHeaderState(StateContext context, int startCellIndex) {
            super(context);
            this.startCellIndex = startCellIndex;
        }

        @Override
        public void parse(Row row) {
            final Cell empty = row.getCell(startCellIndex);
            final Cell total = row.getCell(startCellIndex + 1);
            final Cell inactive = row.getCell(startCellIndex + 2);

            if (firstColumnSpecification.isSatisfiedBy(empty) && secondColumnSpecification.isSatisfiedBy(total) &&
                    thirdColumnSpecification.isSatisfiedBy(inactive)) {
                fireHeader(empty.getStringCellValue(), total.getStringCellValue(), inactive.getStringCellValue());
                context.setState(new ParsingThirdRowOfHeaderState(context, startCellIndex));
            }

        }
    }

    private class ParsingThirdRowOfHeaderState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellIsEmpty();
        private final Specification<Cell> secondColumnSpecification = new CellIsEmpty();
        private final Specification<Cell> thirdColumnSpecification = new CellHasIgnoreCaseStringValue("total");
        private final Specification<Cell> fourthColumnSpecification = new CellHasIgnoreCaseStringValue("%");

        private final int startCellIndex;

        protected ParsingThirdRowOfHeaderState(StateContext context, int startCellIndex) {
            super(context);
            this.startCellIndex = startCellIndex;
        }

        @Override
        public void parse(Row row) {

            final Cell empty = row.getCell(startCellIndex);
            final Cell empty2 = row.getCell(startCellIndex + 1);
            final Cell inactive = row.getCell(startCellIndex + 2);
            final Cell inactiveRatio = row.getCell(startCellIndex + 3);

            if (firstColumnSpecification.isSatisfiedBy(empty) && secondColumnSpecification.isSatisfiedBy(empty2) &&
                    thirdColumnSpecification.isSatisfiedBy(inactive) &&
                    fourthColumnSpecification.isSatisfiedBy(inactiveRatio)) {
                fireHeader(empty.getStringCellValue(), empty2.getStringCellValue(), inactive.getStringCellValue(),
                        inactiveRatio.getStringCellValue());
                context.setState(new ParsingRecordsState(context, startCellIndex));
            }

        }
    }

    private class ParsingRecordsState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellIsOfStringType().and(
                new CellHasIgnoreCaseStringValue("Total").not()).and(new CellHasIgnoreCaseStringValue("Razem").not());
        private final Specification<Cell> secondColumnSpecification = new CellIsOfNumericType();
        private final Specification<Cell> thirdColumnSpecification = new CellIsOfNumericType();
        private final Specification<Cell> fourthColumnSpecification = new CellIsOfNumericType();

        private final int startCellIndex;

        protected ParsingRecordsState(StateContext context, final int startCellIndex) {
            super(context);
            this.startCellIndex = startCellIndex;
        }

        @Override
        public void parse(Row row) {
            final Cell fund = row.getCell(startCellIndex);
            final Cell total = row.getCell(startCellIndex + 1);
            final Cell inactive = row.getCell(startCellIndex + 2);
            final Cell inactiveRatio = row.getCell(startCellIndex + 3);

            if (firstColumnSpecification.isSatisfiedBy(fund) && secondColumnSpecification.isSatisfiedBy(total)
                    && thirdColumnSpecification.isSatisfiedBy(inactive) && fourthColumnSpecification.isSatisfiedBy(inactiveRatio)) {
                fireRecord(fund.getStringCellValue(), (long) total.getNumericCellValue(), (long) inactive.getNumericCellValue());
            } else {
                context.setState(new ParsingTotalState(context, startCellIndex));
                context.parse(row);
            }

        }
    }

    private class ParsingTotalState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellIsOfStringType();
        private final Specification<Cell> secondColumnSpecification = new CellIsOfNumericType().or(cellIsFormula);
        private final Specification<Cell> thirdColumnSpecification = new CellIsOfNumericType().or(cellIsFormula);
        private final Specification<Cell> fourthColumnSpecification = new CellIsOfNumericType().or(cellIsFormula);

        private final Specification<Cell>[] allSpec = new Specification[]{
                firstColumnSpecification,
                secondColumnSpecification,
                thirdColumnSpecification,
                fourthColumnSpecification
        };

        private final int startCellIndex;

        protected ParsingTotalState(StateContext context, final int startCellIndex) {
            super(context);
            this.startCellIndex = startCellIndex;
        }

        private long getLongValue(Cell cell) {
            return (long) getNumericValue(cell);
        }

        @Override
        public void parse(Row row) {

            final Cell totalStr = row.getCell(startCellIndex);
            final Cell total = row.getCell(startCellIndex + 1);
            final Cell inactive = row.getCell(startCellIndex + 2);
            final Cell inactiveRatio = row.getCell(startCellIndex + 3);

            final Cell[] cells = {totalStr, total, inactive, inactiveRatio};

            for (int i = 0; i < cells.length; i++) {
                if (!allSpec[i].isSatisfiedBy(cells[i])) {
                    return;
                }
            }

            fireTotal(getLongValue(total), getLongValue(inactive));

        }
    }
}
