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
package pl.softech.knf.ofe.shared.xls.parser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.softech.knf.ofe.shared.DateParser;
import pl.softech.knf.ofe.shared.xls.XlsParsingException;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class DateParserUtil {

	private enum Parser implements DateParser {

		DATE1(Pattern.compile("\\s*Data as of:\\s+(\\d{2}.\\d{2}.\\d{4}).*"), new SimpleDateFormat("dd.MM.yyyy")),
		DATE2(Pattern.compile("\\s*Data as of:\\s+(\\d{4}-\\d{2}-\\d{2})"), new SimpleDateFormat("yyyy-MM-dd")),
		DATE3(Pattern.compile("\\s*Month:\\s+(\\w+\\s\\d{4})"), new SimpleDateFormat("MMMMM yyyy", Locale.US));

		private final Pattern pattern;
		private final DateFormat dateFormat;

		private Parser(final Pattern pattern, final DateFormat dateFormat) {
			this.pattern = pattern;
			this.dateFormat = dateFormat;
		}

		@Override
		public Date parse(final String date) {
			final Matcher m = pattern.matcher(date);
			if (m.matches()) {
				try {
					return dateFormat.parse(m.group(1));
				} catch (final Exception e) {
					throw new XlsParsingException(e);
				}
			}
			return null;
		}

	}

	/**
	 * @return null if date doesn't match
	 */
	public static Date parse(final String date) {

		for (final Parser parser : Parser.values()) {
			final Date d = parser.parse(date);
			if (d != null) {
				return d;
			}
		}

		return null;
	}

	public static void main(String[] args) {
		System.out.println(DateParserUtil.parse("Month: April 2002"));
	}

}
