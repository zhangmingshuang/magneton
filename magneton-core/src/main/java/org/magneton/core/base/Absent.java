/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.base;

import java.util.Collections;
import java.util.Set;

import javax.annotation.CheckForNull;

/**
 * Implementation of an {@link org.magneton.core.base.Optional} not containing a
 * reference.
 */
@ElementTypesAreNonnullByDefault
final class Absent<T> extends org.magneton.core.base.Optional<T> {

	static final Absent<Object> INSTANCE = new Absent<>();

	private static final long serialVersionUID = 0;

	private Absent() {
	}

	// implementation is "fully variant"
	static <T> org.magneton.core.base.Optional<T> withType() {
		return (org.magneton.core.base.Optional<T>) INSTANCE;
	}

	@Override
	public boolean isPresent() {
		return false;
	}

	@Override
	public T get() {
		throw new IllegalStateException("Optional.get() cannot be called on an absent value");
	}

	@Override
	public T or(T defaultValue) {
		return Preconditions.checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
	}

	// safe covariant cast
	@Override
	public org.magneton.core.base.Optional<T> or(org.magneton.core.base.Optional<? extends T> secondChoice) {
		return (org.magneton.core.base.Optional<T>) Preconditions.checkNotNull(secondChoice);
	}

	@Override
	public T or(Supplier<? extends T> supplier) {
		return Preconditions.checkNotNull(supplier.get(),
				"use Optional.orNull() instead of a Supplier that returns null");
	}

	@Override
	@CheckForNull
	public T orNull() {
		return null;
	}

	@Override
	public Set<T> asSet() {
		return Collections.emptySet();
	}

	@Override
	public <V> org.magneton.core.base.Optional<V> transform(Function<? super T, V> function) {
		Preconditions.checkNotNull(function);
		return Optional.absent();
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		return object == this;
	}

	@Override
	public int hashCode() {
		return 0x79a31aac;
	}

	@Override
	public String toString() {
		return "Optional.absent()";
	}

	private Object readResolve() {
		return INSTANCE;
	}

}
