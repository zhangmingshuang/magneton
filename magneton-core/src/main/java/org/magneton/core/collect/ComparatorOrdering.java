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
import java.util.Comparator;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;

/** An ordering for a pre-existing comparator. */
@ElementTypesAreNonnullByDefault
final class ComparatorOrdering<T> extends Ordering<T> implements Serializable {

	private static final long serialVersionUID = 0;

	final Comparator<T> comparator;

	ComparatorOrdering(Comparator<T> comparator) {
		this.comparator = Preconditions.checkNotNull(comparator);
	}

	@Override
	public int compare(@ParametricNullness T a, @ParametricNullness T b) {
		return comparator.compare(a, b);
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof ComparatorOrdering) {
			ComparatorOrdering<?> that = (ComparatorOrdering<?>) object;
			return comparator.equals(that.comparator);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return comparator.hashCode();
	}

	@Override
	public String toString() {
		return comparator.toString();
	}

}
