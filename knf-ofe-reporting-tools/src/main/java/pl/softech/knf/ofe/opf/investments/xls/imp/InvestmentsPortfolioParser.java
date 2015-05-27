package pl.softech.knf.ofe.opf.investments.xls.imp;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import pl.softech.knf.ofe.opf.investments.Instrument;
import pl.softech.knf.ofe.opf.investments.InstrumentFactory;
import pl.softech.knf.ofe.shared.spec.Specification;
import pl.softech.knf.ofe.shared.xls.parser.*;
import pl.softech.knf.ofe.shared.xls.spec.CellHasIgnoreCaseStringValue;
import pl.softech.knf.ofe.shared.xls.spec.CellIsEmpty;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfNumericType;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfStringType;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class InvestmentsPortfolioParser extends AbstractXlsParser<InvestmentsParsingEventListener> {

    private final InstrumentFactory instrumentFactory;

    public InvestmentsPortfolioParser(InstrumentFactory instrumentFactory) {
        this.instrumentFactory = instrumentFactory;
    }

    private void fireRecord(Instrument instrument, String openPensionFundName, final double investmentValue) {
        listeners.forEach(l -> l.record(instrument, openPensionFundName.trim(), investmentValue));
    }

    private void fireTotal(Instrument instrument, final double totalInvestmentValue) {
        listeners.forEach(l -> l.total(instrument, totalInvestmentValue));
    }

    private void fireTotal(String openPensionFundName, final double totalInvestmentValue) {
        listeners.forEach(l -> l.total(openPensionFundName.trim(), totalInvestmentValue));
    }

    @Override
    protected State createStartingState(StateContext context) {
        return new ParsingDateState(context, new ParsingRowOfHeaderState(context));
    }

    private class ParsingRowOfHeaderState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellHasIgnoreCaseStringValue("Kategoria lokat");
        private final Specification<Cell> secondColumnSpecification = new CellHasIgnoreCaseStringValue("Opis kategorii lokat");
        private final Specification<Cell> lastColumnSpecification = new CellHasIgnoreCaseStringValue("Razem:")
                .or(new CellHasIgnoreCaseStringValue("Portfel razem"));

        private final Specification<Cell> opfColumnSpecification = new CellIsOfStringType().and(lastColumnSpecification.not());

        private boolean instrumentDescriptionAvailable;

        private List<String> allColumns;
        private List<String> opfNames;
        private int cellCnt;

        protected ParsingRowOfHeaderState(StateContext context) {
            super(context);
        }

        private boolean processNextCell(Cell cell) {

            allColumns.add(cell.getStringCellValue());

            if (opfColumnSpecification.isSatisfiedBy(cell)) {

                opfNames.add(cell.getStringCellValue());
                return true;

            } else if (lastColumnSpecification.isSatisfiedBy(cell)) {

                String[] header = allColumns.toArray(new String[allColumns.size()]);
                fireHeader(header);
                State state = new ParsingRowOfRecordState(context, cellCnt, opfNames, instrumentDescriptionAvailable);
                context.setState(new EatEmptyRowsState(context, cellCnt, state));
                return false;

            }

            context.setParsingFailed(true);
            return false;
        }

        @Override
        public void parse(Row row) {

            allColumns = new LinkedList<>();
            opfNames = new LinkedList<>();
            cellCnt = 0;
            instrumentDescriptionAvailable = false;

            final Iterator<Cell> it = row.iterator();
            while (it.hasNext()) {
                final Cell firstCell = it.next();
                if (firstColumnSpecification.isSatisfiedBy(firstCell) && it.hasNext()) {
                    allColumns.add(firstCell.getStringCellValue());

                    final Cell secondCell = it.next();

                    if (secondColumnSpecification.isSatisfiedBy(secondCell)) {

                        instrumentDescriptionAvailable = true;
                        allColumns.add(secondCell.getStringCellValue());

                    } else {

                        if (!processNextCell(secondCell)) {
                            return;
                        }

                    }

                    if (it.hasNext()) {
                        while (it.hasNext()) {
                            final Cell cell = it.next();
                            if (!processNextCell(cell)) {
                                return;
                            }
                        }

                    } else {
                        context.setParsingFailed(true);
                        return;
                    }
                }
                cellCnt++;
            }

        }
    }

    private class EatEmptyRowsState extends AbstractState {

        private final int startCellIndex;

        private final State nextState;

        public EatEmptyRowsState(StateContext context, int startCellIndex, State nextState) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.nextState = nextState;
        }

        @Override
        public void parse(Row row) {

            int cellIt = startCellIndex;

            Cell cell = row.getCell(cellIt);
            CellIsEmpty spec = new CellIsEmpty();

            if (!spec.isSatisfiedBy(cell)) {
                context.setState(nextState);
                context.parse(row);
            }

        }
    }

    private class ParsingRowOfRecordState extends AbstractState {

        private final Specification<Cell> totalFirstColumnSpecification = new CellHasIgnoreCaseStringValue("Razem:")
                .or(new CellHasIgnoreCaseStringValue("Portfel razem"));

        private final Specification<Cell> firstColumnSpecification = new CellIsOfStringType()
                .and(totalFirstColumnSpecification.not());

        private final Specification<Cell> secondColumnSpecification = new CellIsOfStringType();

        private final Specification<Cell> remainingColumnsSpecification = new CellIsOfNumericType();

        private final int startCellIndex;
        private final String[] openPensionFundNames;
        private final boolean instrumentDescriptionAvailable;

        protected ParsingRowOfRecordState(StateContext context, int startCellIndex, List<String> openPensionFundNames, boolean
                instrumentDescriptionAvailable) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.openPensionFundNames = openPensionFundNames.toArray(new String[openPensionFundNames.size()]);
            this.instrumentDescriptionAvailable = instrumentDescriptionAvailable;
        }

        @Override
        public void parse(Row row) {

            int cellIt = startCellIndex;
            String instrumentDescription = null;

            final Cell instrName = row.getCell(cellIt++);

            if (firstColumnSpecification.isSatisfiedBy(instrName)) {

                if (instrumentDescriptionAvailable) {
                    final Cell instrDesc = row.getCell(cellIt++);

                    if (secondColumnSpecification.isSatisfiedBy(instrDesc)) {
                        instrumentDescription = instrDesc.getStringCellValue();
                    } else {
                        return;
                    }

                }

                Instrument instrument = instrumentFactory.create(instrName.getStringCellValue(), instrumentDescription);

                for (int i = 0; i < openPensionFundNames.length; i++, cellIt++) {
                    Cell cell = row.getCell(cellIt);
                    if (remainingColumnsSpecification.isSatisfiedBy(cell)) {
                        fireRecord(instrument, openPensionFundNames[i], cell.getNumericCellValue());
                    }
                }

                Cell cell = row.getCell(cellIt);
                if (remainingColumnsSpecification.isSatisfiedBy(cell)) {
                    fireTotal(instrument, cell.getNumericCellValue());
                }

            } else {

                context.setState(new ParsingTotalState(context, startCellIndex, openPensionFundNames, instrumentDescriptionAvailable,
                        totalFirstColumnSpecification));
                context.parse(row);

            }

        }
    }

    private class ParsingTotalState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification;
        private final Specification<Cell> secondColumnSpecification = new CellIsEmpty();
        private final Specification<Cell> remainingColumnsSpecification = new CellIsOfNumericType();

        private final int startCellIndex;
        private final String[] openPensionFundNames;
        private final boolean instrumentDescriptionAvailable;

        public ParsingTotalState(StateContext context, int startCellIndex, String[] openPensionFundNames, boolean
                instrumentDescriptionAvailable, Specification<Cell> firstColumnSpecification) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.openPensionFundNames = openPensionFundNames;
            this.instrumentDescriptionAvailable = instrumentDescriptionAvailable;
            this.firstColumnSpecification = firstColumnSpecification;
        }

        @Override
        public void parse(Row row) {

            int cellIt = startCellIndex;

            final Cell total = row.getCell(cellIt++);

            if (firstColumnSpecification.isSatisfiedBy(total)) {

                if (instrumentDescriptionAvailable) {
                    final Cell empty = row.getCell(cellIt++);
                    if (!secondColumnSpecification.isSatisfiedBy(empty)) {
                        return;
                    }
                }

                for (int i = 0; i < openPensionFundNames.length; i++, cellIt++) {
                    Cell cell = row.getCell(cellIt);
                    if (remainingColumnsSpecification.isSatisfiedBy(cell)) {
                        fireTotal(openPensionFundNames[i], cell.getNumericCellValue());
                    }
                }

                context.setState(new EndingState());

            }

        }
    }

}
