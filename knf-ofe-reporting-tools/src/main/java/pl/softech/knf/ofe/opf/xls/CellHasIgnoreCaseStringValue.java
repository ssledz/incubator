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

import org.apache.poi.ss.usermodel.Cell;

import pl.softech.knf.ofe.shared.spec.Specification;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
class CellHasIgnoreCaseStringValue implements Specification<Cell> {

	private final String value;

	CellHasIgnoreCaseStringValue(final String value) {
		this.value = value;
	}

	@Override
	public boolean isSatisfiedBy(final Cell arg) {

		if (arg.getCellType() != Cell.CELL_TYPE_STRING) {
			return false;
		}

		return value.equalsIgnoreCase(arg.getStringCellValue().trim());

	}

}
