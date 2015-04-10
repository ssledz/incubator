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
package pl.softech.knf.ofe.opf;

import static java.util.Objects.requireNonNull;

import java.util.Date;

/**
 * <class>OpenPensionFund is fully immutable</class>
 * 
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public class OpenPensionFund {

	private String name;
	private long numberOfMembers;
	private Date date;

	public OpenPensionFund(final String name, final long numberOfMembers, final Date date) {
		this.name = requireNonNull(name);
		this.numberOfMembers = requireNonNull(numberOfMembers);
		this.date = new Date(requireNonNull(date).getTime());
	}

	public String getName() {
		return name;
	}

	public long getNumberOfMembers() {
		return numberOfMembers;
	}

	public Date getDate() {
		return new Date(date.getTime());
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("OpenPensionFund [name=").append(name).append(", numberOfMembers=").append(numberOfMembers).append(", date=")
				.append(date).append("]");
		return builder.toString();
	}

}
