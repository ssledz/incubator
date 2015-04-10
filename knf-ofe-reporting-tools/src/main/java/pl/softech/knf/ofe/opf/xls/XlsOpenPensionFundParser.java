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
package pl.softech.knf.ofe.opf.xls;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
class XlsOpenPensionFundParser {

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

	void addParsingEventListener(final ParsingEventListener l) {
		listeners.add(l);
	}
	
	void parseSheet(final Sheet sheet) {
		final StateContext context = new StateContext();
		context.setState(new ParsingDateState(context));
		sheet.forEach(row -> context.parse(row));
	}

	private interface State {
		void parse(Row row);
	}

	private static class StateContext implements State {

		private State state;

		private XlsOpenPensionFundParser parser;
		
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

		}

	}

	private static class ParsingHeaderState extends AbstractState {

		ParsingHeaderState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {

		}

	}

	private static class ParsingRecordsState extends AbstractState {

		ParsingRecordsState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {

		}

	}

	private static class ParsingTotalState extends AbstractState {

		ParsingTotalState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {

		}

	}

	private static class FinishedState extends AbstractState {

		FinishedState(final StateContext context) {
			super(context);
		}

		@Override
		public void parse(final Row row) {

		}

	}

}
