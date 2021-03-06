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
package pl.softech.knf.ofe.shared.spec;

/**
 * @author Sławomir Śledź <slawomir.sledz@gmail.com>
 * @since 1.0
 */
public interface Specification<T> {

	boolean isSatisfiedBy(T arg);

	default Specification<T> not() {
		return (arg) -> {
			return !isSatisfiedBy(arg);
		};
	}

	default Specification<T> and(final Specification<T> spec) {
		return (arg) -> {
			return isSatisfiedBy(arg) && spec.isSatisfiedBy(arg);
		};
	}
	
	default Specification<T> or(final Specification<T> spec) {
		return (arg) -> {
			return isSatisfiedBy(arg) || spec.isSatisfiedBy(arg);
		};
	}
}
