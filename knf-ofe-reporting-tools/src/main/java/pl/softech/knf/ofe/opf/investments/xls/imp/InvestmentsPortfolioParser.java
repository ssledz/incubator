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

        protected ParsingRowOfHeaderState(StateContext context) {
            super(context);
        }

        @Override
        public void parse(Row row) {

            List<String> allColumns = new LinkedList<>();
            List<String> opfNames = new LinkedList<>();

            final Iterator<Cell> it = row.iterator();
            int cellCnt = 0;
            while (it.hasNext()) {
                final Cell firstCell = it.next();
                if (firstColumnSpecification.isSatisfiedBy(firstCell) && it.hasNext()) {
                    final Cell secondCell = it.next();
                    allColumns.add(firstCell.getStringCellValue());
                    if (secondColumnSpecification.isSatisfiedBy(secondCell) && it.hasNext()) {
                        allColumns.add(secondCell.getStringCellValue());
                        while (it.hasNext()) {
                            final Cell cell = it.next();
                            allColumns.add(cell.getStringCellValue());
                            if (opfColumnSpecification.isSatisfiedBy(cell)) {
                                opfNames.add(cell.getStringCellValue());
                            } else if (lastColumnSpecification.isSatisfiedBy(cell)) {
                                String[] header = allColumns.toArray(new String[allColumns.size()]);
                                fireHeader(header);
                                context.setState(new ParsingRowOfRecordState(context, cellCnt, opfNames));
                            } else {
                                context.setParsingFailed(true);
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

    private class ParsingRowOfRecordState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellIsOfStringType()
                .and(new CellHasIgnoreCaseStringValue("Razem:").not());
        private final Specification<Cell> secondColumnSpecification = new CellIsOfStringType();

        private final Specification<Cell> remainingColumnsSpecification = new CellIsOfNumericType();

        private final int startCellIndex;
        private final String[] openPensionFundNames;

        protected ParsingRowOfRecordState(StateContext context, int startCellIndex, List<String> openPensionFundNames) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.openPensionFundNames = openPensionFundNames.toArray(new String[openPensionFundNames.size()]);
        }

        @Override
        public void parse(Row row) {

            int cellIt = startCellIndex;

            final Cell instrName = row.getCell(cellIt++);
            final Cell instrDesc = row.getCell(cellIt++);

            if (firstColumnSpecification.isSatisfiedBy(instrName) && secondColumnSpecification.isSatisfiedBy(instrDesc)) {

                Instrument instrument = instrumentFactory.create(instrName.getStringCellValue(), instrDesc.getStringCellValue());

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

                context.setState(new ParsingTotalState(context, startCellIndex, openPensionFundNames));
                context.parse(row);

            }

        }
    }

    private class ParsingTotalState extends AbstractState {

        private final Specification<Cell> firstColumnSpecification = new CellHasIgnoreCaseStringValue("Razem:");
        private final Specification<Cell> secondColumnSpecification = new CellIsEmpty();
        private final Specification<Cell> remainingColumnsSpecification = new CellIsOfNumericType();

        private final int startCellIndex;
        private final String[] openPensionFundNames;

        public ParsingTotalState(StateContext context, int startCellIndex, String[] openPensionFundNames) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.openPensionFundNames = openPensionFundNames;
        }

        @Override
        public void parse(Row row) {

            int cellIt = startCellIndex;

            final Cell total = row.getCell(cellIt++);
            final Cell empty = row.getCell(cellIt++);

            if (firstColumnSpecification.isSatisfiedBy(total) && secondColumnSpecification.isSatisfiedBy(empty)) {

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
