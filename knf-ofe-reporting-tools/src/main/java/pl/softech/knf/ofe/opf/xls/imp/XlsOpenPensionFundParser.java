/*
 * Copyright 2015 Sławomir Śledź <slawomir.sledz@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.softech.knf.ofe.opf.xls.imp;

import static java.util.Objects.requireNonNull;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import pl.softech.knf.ofe.shared.spec.Specification;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class XlsOpenPensionFundParser {

	private final List<ParsingEventListener> listeners = new LinkedList<>();

	private void fireDate(final Date date) {
		listeners.forEach(l -> l.date(date));
	}

	private void fireHeader(final String... columns) {
		listeners.forEach(l -> l.header(columns));
	}

	private void fireRecord(final String name, final long numberOfMembers) {
		listeners.forEach(l -> l.record(name, numberOfMembers));
	}

	private void fireTotal(final long total) {
		listeners.forEach(l -> l.total(total));
	}

	public void addParsingEventListener(final ParsingEventListener l) {
		listeners.add(l);
	}

	public void parseSheet(final Sheet sheet) {
		requireNonNull(sheet, "Sheet can't be null");
		final StateContext context = new StateContext(this);
		context.setState(new ParsingDateState(context));
		sheet.forEach(row -> context.parse(row));
	}

	private interface State {
		void parse(Row row);
	}

	private static class StateContext implements State {

		private State state;

		private final XlsOpenPensionFundParser parser;

		StateContext(final XlsOpenPensionFundParser parser) {
			this.parser = parser;
		}

		@Override
		public void parse(final Row row) {
			state.parse(row);
		}

		void setState(final State state) {
			this.state = state;
		}

		XlsOpenPensionFundParser getParser() {
			return parser;
		}

	}

	private static abstract class AbstractState implements State {

		protected final StateContext context;

		protected AbstractState(final StateContext context) {
			this.context = context;
		}

	}

	private static class ParsingDateState extends AbstractState {

		ParsingDateState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {

			for (final Cell cell : row) {

				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

					final Date date = DateParserUtil.parse(cell.getStringCellValue());
					if (date != null) {
						context.getParser().fireDate(date);
						context.setState(new ParsingHeaderState(context));
					}

				}

			}
		}

	}

	private static class ParsingHeaderState extends AbstractState {

		private final Specification<Cell> firstColumnSpecification = new CellHasIgnoreCaseStringValue("Open Pension Fund");
		private final Specification<Cell> secondColumnSpecification = new CellHasIgnoreCaseStringValue("Number of members");

		ParsingHeaderState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {

			final Iterator<Cell> it = row.iterator();
			int cellCnt = 0;
			while (it.hasNext()) {
				final Cell firstCell = it.next();
				if (firstColumnSpecification.isSatisfiedBy(firstCell) && it.hasNext()) {
					final Cell secondCell = it.next();
					if (secondColumnSpecification.isSatisfiedBy(secondCell)) {
						context.getParser().fireHeader(firstCell.getStringCellValue(), secondCell.getStringCellValue());
						context.setState(new ParsingRecordsState(context, cellCnt));
						break;
					}
				}
				cellCnt++;
			}
		}

	}

	private static class ParsingRecordsState extends AbstractState {

		private final Specification<Cell> firstColumnSpecification = new CellIsOfStringType().and(
				new CellHasIgnoreCaseStringValue("Total").not()).and(new CellHasIgnoreCaseStringValue("Razem").not());
		private final Specification<Cell> secondColumnSpecification = new CellIsOfNumericType();

		private final int startCellIndex;

		ParsingRecordsState(final StateContext context, final int startCellIndex) {
			super(context);
			this.startCellIndex = startCellIndex;
		}

		@Override
		public void parse(final Row row) {

			final Cell fundCell = row.getCell(startCellIndex);
			final Cell memberCell = row.getCell(startCellIndex + 1);

			if (firstColumnSpecification.isSatisfiedBy(fundCell) && secondColumnSpecification.isSatisfiedBy(memberCell)) {
				context.getParser().fireRecord(fundCell.getStringCellValue(), (long) memberCell.getNumericCellValue());
			} else {
				context.setState(new ParsingTotalState(startCellIndex, context));
				context.parse(row);
			}

		}

	}

	private static class ParsingTotalState extends AbstractState {

		private final Specification<Cell> firstColumnSpecification = new CellIsOfStringType();
		private final Specification<Cell> cellIsFormula = new CellIsOfFormulaType();
		private final Specification<Cell> secondColumnSpecification = new CellIsOfNumericType().or(cellIsFormula);

		private final int startCellIndex;

		ParsingTotalState(final int startCellIndex, final StateContext context) {
			super(context);
			this.startCellIndex = startCellIndex;
		}

		@Override
		public void parse(final Row row) {
			final Cell totalStrCell = row.getCell(startCellIndex);
			final Cell totalNumberCell = row.getCell(startCellIndex + 1);

			if (firstColumnSpecification.isSatisfiedBy(totalStrCell) && secondColumnSpecification.isSatisfiedBy(totalNumberCell)) {
				if (cellIsFormula.isSatisfiedBy(totalNumberCell)) {
					final Workbook wb = row.getSheet().getWorkbook();
					final FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
					final CellValue value = evaluator.evaluate(totalNumberCell);
					context.getParser().fireTotal((long) value.getNumberValue());
				} else {
					context.getParser().fireTotal((long) totalNumberCell.getNumericCellValue());
				}
				context.setState(new FinishedState(context));
			}
		}

	}

	private static class FinishedState extends AbstractState {

		FinishedState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {
			// do nothing
		}

	}

}
