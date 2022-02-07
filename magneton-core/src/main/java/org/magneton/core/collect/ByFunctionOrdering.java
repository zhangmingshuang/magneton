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

import org.magneton.core.base.Function;
import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;

/**
 * An ordering that orders elements by applying an order to the result of a function on
 * those elements.
 */
@ElementTypesAreNonnullByDefault
final class ByFunctionOrdering<F extends Object, T extends Object> extends Ordering<F> implements Serializable {

	private static final long serialVersionUID = 0;

	final org.magneton.core.base.Function<F, ? extends T> function;

	final Ordering<T> ordering;

	ByFunctionOrdering(Function<F, ? extends T> function, Ordering<T> ordering) {
		this.function = Preconditions.checkNotNull(function);
		this.ordering = Preconditions.checkNotNull(ordering);
	}

	@Override
	public int compare(@ParametricNullness F left, @ParametricNullness F right) {
		return ordering.compare(function.apply(left), function.apply(right));
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object == this) {
			return true;
		}
		if (object instanceof ByFunctionOrdering) {
			ByFunctionOrdering<?, ?> that = (ByFunctionOrdering<?, ?>) object;
			return function.equals(that.function) && ordering.equals(that.ordering);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(function, ordering);
	}

	@Override
	public String toString() {
		return ordering + ".onResultOf(" + function + ")";
	}

}
