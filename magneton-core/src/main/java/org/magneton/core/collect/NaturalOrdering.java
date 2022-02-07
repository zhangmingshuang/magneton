/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.magneton.core.collect;

import java.io.Serializable;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;

/** An ordering that uses the natural order of the values. */
// TODO(kevinb): the right way to explain
// this??
@ElementTypesAreNonnullByDefault
final class NaturalOrdering extends Ordering<Comparable<?>> implements Serializable {

	static final NaturalOrdering INSTANCE = new NaturalOrdering();

	private static final long serialVersionUID = 0;

	@CheckForNull
	private transient Ordering<Comparable<?>> nullsFirst;

	@CheckForNull
	private transient Ordering<Comparable<?>> nullsLast;

	private NaturalOrdering() {
	}

	@Override
	public int compare(Comparable<?> left, Comparable<?> right) {
		Preconditions.checkNotNull(left); // for GWT
		Preconditions.checkNotNull(right);
		return ((Comparable<Object>) left).compareTo(right);
	}

	@Override
	public <S extends Comparable<?>> Ordering<S> nullsFirst() {
		Ordering<Comparable<?>> result = nullsFirst;
		if (result == null) {
			result = nullsFirst = super.<Comparable<?>>nullsFirst();
		}
		return (Ordering<S>) result;
	}

	@Override
	public <S extends Comparable<?>> Ordering<S> nullsLast() {
		Ordering<Comparable<?>> result = nullsLast;
		if (result == null) {
			result = nullsLast = super.<Comparable<?>>nullsLast();
		}
		return (Ordering<S>) result;
	}

	@Override
	public <S extends Comparable<?>> Ordering<S> reverse() {
		return (Ordering<S>) ReverseNaturalOrdering.INSTANCE;
	}

	// preserving singleton-ness gives equals()/hashCode() for free
	private Object readResolve() {
		return INSTANCE;
	}

	@Override
	public String toString() {
		return "Ordering.natural()";
	}

}
