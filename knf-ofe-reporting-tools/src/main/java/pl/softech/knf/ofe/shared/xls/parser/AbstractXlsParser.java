package pl.softech.knf.ofe.shared.xls.parser;

import org.apache.poi.ss.usermodel.*;
import pl.softech.knf.ofe.shared.spec.Specification;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfFormulaType;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public abstract class AbstractXlsParser<T extends ParsingEventListener> {

    protected final Specification<Cell> cellIsFormula = new CellIsOfFormulaType();

    protected final List<T> listeners = new LinkedList<>();

    protected void fireDate(final Date date) {
        listeners.forEach(l -> l.date(date));
    }


    private static String[] trim(final String... columns) {
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columns[i].trim();
        }
        return columns;
    }

    protected void fireHeader(final String... columns) {
        listeners.forEach(l -> l.header(trim(columns)));
    }

    protected static CellValue evaluateFormula(Cell formula) {
        Row row = formula.getRow();
        final Workbook wb = row.getSheet().getWorkbook();
        final FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        return evaluator.evaluate(formula);
    }

    protected double getNumericValue(Cell cell) {
        if (cellIsFormula.isSatisfiedBy(cell)) {
            return evaluateFormula(cell).getNumberValue();
        }
        return cell.getNumericCellValue();
    }

    protected long getLongValue(Cell cell) {
        return (long) getNumericValue(cell);
    }

    public void addParsingEventListener(final T l) {
        listeners.add(l);
    }

    protected abstract State createStartingState(StateContext context);

    public void parseSheet(final Sheet sheet) {
        requireNonNull(sheet, "Sheet can't be null");
        final StateContext context = new StateContext();
        context.setState(createStartingState(context));
        sheet.forEach(row -> context.parse(row));
    }

    protected class ParsingDateState extends AbstractState {

        private final State nextState;

        public ParsingDateState(final StateContext context, final State nextState) {
            super(context);
            this.nextState = nextState;
        }

        @Override
        public void parse(final Row row) {

            for (final Cell cell : row) {

                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

                    final Date date = DateParserUtil.parse(cell.getStringCellValue());
                    if (date != null) {
                        fireDate(date);
                        context.setState(nextState);
                    }

                }

            }
        }

    }

    protected interface StateFactory {
        State create(int startingCell);
    }

    protected class ParsingHeaderState extends AbstractState {

        private final StateFactory nextStateFactory;
        private final List<Specification<Cell>> specifications;

        public ParsingHeaderState(StateContext context, List<Specification<Cell>> specifications, final StateFactory nextStateFactory) {
            super(context);
            this.specifications = specifications;
            this.nextStateFactory = nextStateFactory;
        }

        @Override
        public void parse(Row row) {
            final Iterator<Cell> cellIt = row.iterator();
            int cellCnt = 0;
            while (cellIt.hasNext()) {
                final Iterator<Specification<Cell>> specIt = specifications.iterator();
                List<String> columns = new ArrayList<>();
                while (specIt.hasNext()) {
                    Specification<Cell> spec = specIt.next();
                    final Cell cell = cellIt.next();
                    if (spec.isSatisfiedBy(cell)) {
                        columns.add(cell.getStringCellValue());
                        if (!specIt.hasNext()) {
                            fireHeader(columns.toArray(new String[columns.size()]));
                            context.setState(nextStateFactory.create(cellCnt));
                        }
                    } else {
                        break;
                    }

                    if (!specIt.hasNext() || !cellIt.hasNext()) {
                        break;
                    }
                }

                cellCnt++;
            }
        }

    }

    protected interface NewRecordListener {
        void record(List<Cell> cells);
    }

    protected static class GenericParsingRecordsState extends AbstractState {

        private final int startCellIndex;
        private final StateFactory nextStateFactory;
        private final List<Specification<Cell>> specifications;
        private final NewRecordListener newRecordListener;

        public GenericParsingRecordsState(StateContext context, final int startCellIndex, NewRecordListener newRecordListener,
                                          List<Specification<Cell>> specifications, final StateFactory nextStateFactory) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.specifications = specifications;
            this.newRecordListener = newRecordListener;
            this.nextStateFactory = nextStateFactory;
        }

        @Override
        public void parse(Row row) {

            List<Cell> cells = new ArrayList<>();
            int cellIdx = startCellIndex;
            for (Specification<Cell> spec : specifications) {

                Cell cell = row.getCell(cellIdx++);
                cells.add(cell);
                if (!spec.isSatisfiedBy(cell)) {
                    context.setState(nextStateFactory.create(startCellIndex));
                    context.parse(row);
                    return;
                }

            }

            newRecordListener.record(cells);
        }
    }

    protected static class GenericParsingTotalState extends AbstractState {

        private final int startCellIndex;
        private final List<Specification<Cell>> specifications;
        private final NewRecordListener newRecordListener;

        public GenericParsingTotalState(StateContext context, final int startCellIndex, NewRecordListener newRecordListener,
                                           List<Specification<Cell>> specifications) {
            super(context);
            this.startCellIndex = startCellIndex;
            this.newRecordListener = newRecordListener;
            this.specifications = specifications;
        }

        @Override
        public void parse(Row row) {
            List<Cell> cells = new ArrayList<>();
            int cellIdx = startCellIndex;
            for (Specification<Cell> spec : specifications) {
                Cell cell = row.getCell(cellIdx++);
                cells.add(cell);
                if (!spec.isSatisfiedBy(cell)) {
                    return;
                }
            }

            newRecordListener.record(cells);
            context.setState(new EndingState());
        }
    }

}
