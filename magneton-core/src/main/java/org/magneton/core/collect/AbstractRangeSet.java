/*
 * Copyright (C) 2011 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.magneton.core.collect;

import javax.annotation.CheckForNull;

/**
 * A skeletal implementation of {@code RangeSet}.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
abstract class AbstractRangeSet<C extends Comparable> implements org.magneton.core.collect.RangeSet<C> {

	AbstractRangeSet() {
	}

	@Override
	public boolean contains(C value) {
		return rangeContaining(value) != null;
	}

	@Override
	@CheckForNull
	public abstract Range<C> rangeContaining(C value);

	@Override
	public boolean isEmpty() {
		return asRanges().isEmpty();
	}

	@Override
	public void add(Range<C> range) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove(Range<C> range) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		remove(Range.<C>all());
	}

	@Override
	public boolean enclosesAll(org.magneton.core.collect.RangeSet<C> other) {
		return enclosesAll(other.asRanges());
	}

	@Override
	public void addAll(org.magneton.core.collect.RangeSet<C> other) {
		addAll(other.asRanges());
	}

	@Override
	public void removeAll(org.magneton.core.collect.RangeSet<C> other) {
		removeAll(other.asRanges());
	}

	@Override
	public boolean intersects(Range<C> otherRange) {
		return !subRangeSet(otherRange).isEmpty();
	}

	@Override
	public abstract boolean encloses(Range<C> otherRange);

	@Override
	public boolean equals(@CheckForNull Object obj) {
		if (obj == this) {
			return true;
		}
		else if (obj instanceof org.magneton.core.collect.RangeSet) {
			org.magneton.core.collect.RangeSet<?> other = (RangeSet<?>) obj;
			return asRanges().equals(other.asRanges());
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return asRanges().hashCode();
	}

	@Override
	public final String toString() {
		return asRanges().toString();
	}

}