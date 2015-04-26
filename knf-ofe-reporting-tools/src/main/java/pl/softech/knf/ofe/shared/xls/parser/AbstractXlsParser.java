package pl.softech.knf.ofe.shared.xls.parser;

import org.apache.poi.ss.usermodel.*;
import pl.softech.knf.ofe.shared.spec.Specification;
import pl.softech.knf.ofe.shared.xls.spec.CellIsOfFormulaType;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

}
