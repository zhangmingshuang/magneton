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

/** An ordering that treats {@code null} as less than all other values. */
@ElementTypesAreNonnullByDefault
final class NullsFirstOrdering<T> extends Ordering<T> implements Serializable {

	private static final long serialVersionUID = 0;

	final Ordering<? super T> ordering;

	NullsFirstOrdering(Ordering<? super T> ordering) {
		this.ordering = ordering;
	}

	@Override
	public int compare(@CheckForNull T left, @CheckForNull T right) {
		if (left == right) {
			return 0;
		}
		if (left == null) {
			return RIGHT_IS_GREATER;
		}
		if (right == null) {
			return LEFT_IS_GREATER;
		}
		return ordering.compare(left, right);
	}

	@Override
	// should be safe, but not sure if we can avoid the
	// warning
	public <S extends T> Ordering<S> reverse() {
		// ordering.reverse() might be optimized, so let it do its thing
		return ordering.reverse().nullsLast();
	}

	// still need the right way to explain this
	@Override
	public <S extends T> Ordering<S> nullsFirst() {
		return (Ordering<S>) this;
	}

	@Override
	public <S extends T> Ordering<S> nullsLast() {
		return ordering.<S>nullsLast();
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof NullsFirstOrdering) {
			NullsFirstOrdering<?> that = (NullsFirstOrdering<?>) object;
			return ordering.equals(that.ordering);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return ordering.hashCode() ^ 957692532; // meaningless
	}

	@Override
	public String toString() {
		return ordering + ".nullsFirst()";
	}

}
