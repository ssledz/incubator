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
package pl.softech.knf.ofe.shared.xls.spec;

import org.apache.poi.ss.usermodel.Cell;
import pl.softech.knf.ofe.shared.spec.Specification;

import java.util.regex.Pattern;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class CellHasIgnoreCaseStringPatternValue implements Specification<Cell> {

	private final Pattern pattern;

	public CellHasIgnoreCaseStringPatternValue(final String pattern) {
		this.pattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	}

	@Override
	public boolean isSatisfiedBy(final Cell arg) {

		if (arg.getCellType() != Cell.CELL_TYPE_STRING) {
			return false;
		}

		return pattern.matcher(arg.getStringCellValue().trim()).matches();
	}

	public static void main(String[] args) {

		Pattern pattern = Pattern.compile("Amount of contribution.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

		System.out.println(pattern.matcher("Amount of contribution (w ZL)\n").matches());
	}

}
