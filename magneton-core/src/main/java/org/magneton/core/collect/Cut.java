/*
 * Copyright (C) 2009 The Guava Authors
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

package org.magneton.core.collect;

import java.io.Serializable;
import java.util.NoSuchElementException;

import javax.annotation.CheckForNull;

import org.magneton.core.primitives.Booleans;

import static org.magneton.core.base.Preconditions.checkNotNull;

/**
 * Implementation detail for the internal structure of {@link Range} instances. Represents
 * a unique way of "cutting" a "number line" (actually of instances of type {@code C}, not
 * necessarily "numbers") into two sections; this can be done below a certain value, above
 * a certain value, below all values or above all values. With this object defined in this
 * way, an interval can always be represented by a pair of {@code Cut} instances.
 *
 * @author Kevin Bourrillion
 */
@ElementTypesAreNonnullByDefault
abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {

	private static final long serialVersionUID = 0;

	final C endpoint;

	Cut(C endpoint) {
		this.endpoint = endpoint;
	}

	/*
	 * The implementation neither produces nor consumes any non-null instance of type C,
	 * so casting the type parameter is safe.
	 */
	static <C extends Comparable> Cut<C> belowAll() {
		return (Cut<C>) BelowAll.INSTANCE;
	}

	/*
	 * The implementation neither produces nor consumes any non-null instance of type C,
	 * so casting the type parameter is safe.
	 */
	static <C extends Comparable> Cut<C> aboveAll() {
		return (Cut<C>) AboveAll.INSTANCE;
	}

	static <C extends Comparable> Cut<C> belowValue(C endpoint) {
		return new BelowValue<>(endpoint);
	}

	static <C extends Comparable> Cut<C> aboveValue(C endpoint) {
		return new AboveValue<>(endpoint);
	}

	abstract boolean isLessThan(C value);

	abstract org.magneton.core.collect.BoundType typeAsLowerBound();

	abstract org.magneton.core.collect.BoundType typeAsUpperBound();

	abstract Cut<C> withLowerBoundType(org.magneton.core.collect.BoundType boundType,
			org.magneton.core.collect.DiscreteDomain<C> domain);

	abstract Cut<C> withUpperBoundType(org.magneton.core.collect.BoundType boundType,
			org.magneton.core.collect.DiscreteDomain<C> domain);

	abstract void describeAsLowerBound(StringBuilder sb);

	abstract void describeAsUpperBound(StringBuilder sb);

	@CheckForNull
	abstract C leastValueAbove(org.magneton.core.collect.DiscreteDomain<C> domain);

	@CheckForNull
	abstract C greatestValueBelow(org.magneton.core.collect.DiscreteDomain<C> domain);

	/*
	 * The canonical form is a BelowValue cut whenever possible, otherwise ABOVE_ALL, or
	 * (only in the case of types that are unbounded below) BELOW_ALL.
	 */
	Cut<C> canonical(org.magneton.core.collect.DiscreteDomain<C> domain) {
		return this;
	}

	// note: overridden by {BELOW,ABOVE}_ALL
	@Override
	public int compareTo(Cut<C> that) {
		if (that == belowAll()) {
			return 1;
		}
		if (that == aboveAll()) {
			return -1;
		}
		int result = Range.compareOrThrow(endpoint, that.endpoint);
		if (result != 0) {
			return result;
		}
		// same value. below comes before above
		return Booleans.compare(this instanceof AboveValue, that instanceof AboveValue);
	}

	C endpoint() {
		return endpoint;
	}

	// catching CCE
	@Override
	public boolean equals(@CheckForNull Object obj) {
		if (obj instanceof Cut) {
			// It might not really be a Cut<C>, but we'll catch a CCE if it's not
			Cut<C> that = (Cut<C>) obj;
			try {
				int compareResult = compareTo(that);
				return compareResult == 0;
			}
			catch (ClassCastException wastNotComparableToOurType) {
				return false;
			}
		}
		return false;
	}

	// Prevent "missing hashCode" warning by explicitly forcing subclasses implement it
	@Override
	public abstract int hashCode();

	private static final class BelowAll extends Cut<Comparable<?>> {

		private static final BelowAll INSTANCE = new BelowAll();

		private static final long serialVersionUID = 0;

		private BelowAll() {
			/*
			 * No code ever sees this bogus value for `endpoint`: This class overrides
			 * both methods that use the `endpoint` field, compareTo() and endpoint().
			 * Additionally, the main implementation of Cut.compareTo checks for belowAll
			 * before reading accessing `endpoint` on another Cut instance.
			 */
			super("");
		}

		@Override
		Comparable<?> endpoint() {
			throw new IllegalStateException("range unbounded on this side");
		}

		@Override
		boolean isLessThan(Comparable<?> value) {
			return true;
		}

		@Override
		org.magneton.core.collect.BoundType typeAsLowerBound() {
			throw new IllegalStateException();
		}

		@Override
		org.magneton.core.collect.BoundType typeAsUpperBound() {
			throw new AssertionError("this statement should be unreachable");
		}

		@Override
		Cut<Comparable<?>> withLowerBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			throw new IllegalStateException();
		}

		@Override
		Cut<Comparable<?>> withUpperBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			throw new AssertionError("this statement should be unreachable");
		}

		@Override
		void describeAsLowerBound(StringBuilder sb) {
			sb.append("(-\u221e");
		}

		@Override
		void describeAsUpperBound(StringBuilder sb) {
			throw new AssertionError();
		}

		@Override
		Comparable<?> leastValueAbove(org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			return domain.minValue();
		}

		@Override
		Comparable<?> greatestValueBelow(org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			throw new AssertionError();
		}

		@Override
		Cut<Comparable<?>> canonical(org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			try {
				return Cut.<Comparable<?>>belowValue(domain.minValue());
			}
			catch (NoSuchElementException e) {
				return this;
			}
		}

		@Override
		public int compareTo(Cut<Comparable<?>> o) {
			return (o == this) ? 0 : -1;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(this);
		}

		@Override
		public String toString() {
			return "-\u221e";
		}

		private Object readResolve() {
			return INSTANCE;
		}

	}

	private static final class AboveAll extends Cut<Comparable<?>> {

		private static final AboveAll INSTANCE = new AboveAll();

		private static final long serialVersionUID = 0;

		private AboveAll() {
			// For discussion of "", see BelowAll.
			super("");
		}

		@Override
		Comparable<?> endpoint() {
			throw new IllegalStateException("range unbounded on this side");
		}

		@Override
		boolean isLessThan(Comparable<?> value) {
			return false;
		}

		@Override
		org.magneton.core.collect.BoundType typeAsLowerBound() {
			throw new AssertionError("this statement should be unreachable");
		}

		@Override
		org.magneton.core.collect.BoundType typeAsUpperBound() {
			throw new IllegalStateException();
		}

		@Override
		Cut<Comparable<?>> withLowerBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			throw new AssertionError("this statement should be unreachable");
		}

		@Override
		Cut<Comparable<?>> withUpperBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			throw new IllegalStateException();
		}

		@Override
		void describeAsLowerBound(StringBuilder sb) {
			throw new AssertionError();
		}

		@Override
		void describeAsUpperBound(StringBuilder sb) {
			sb.append("+\u221e)");
		}

		@Override
		Comparable<?> leastValueAbove(org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			throw new AssertionError();
		}

		@Override
		Comparable<?> greatestValueBelow(org.magneton.core.collect.DiscreteDomain<Comparable<?>> domain) {
			return domain.maxValue();
		}

		@Override
		public int compareTo(Cut<Comparable<?>> o) {
			return (o == this) ? 0 : 1;
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(this);
		}

		@Override
		public String toString() {
			return "+\u221e";
		}

		private Object readResolve() {
			return INSTANCE;
		}

	}

	private static final class BelowValue<C extends Comparable> extends Cut<C> {

		private static final long serialVersionUID = 0;

		BelowValue(C endpoint) {
			super(checkNotNull(endpoint));
		}

		@Override
		boolean isLessThan(C value) {
			return Range.compareOrThrow(endpoint, value) <= 0;
		}

		@Override
		org.magneton.core.collect.BoundType typeAsLowerBound() {
			return org.magneton.core.collect.BoundType.CLOSED;
		}

		@Override
		org.magneton.core.collect.BoundType typeAsUpperBound() {
			return org.magneton.core.collect.BoundType.OPEN;
		}

		@Override
		Cut<C> withLowerBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<C> domain) {
			switch (boundType) {
			case CLOSED:
				return this;
			case OPEN:
				C previous = domain.previous(endpoint);
				return (previous == null) ? Cut.<C>belowAll() : new AboveValue<C>(previous);
			default:
				throw new AssertionError();
			}
		}

		@Override
		Cut<C> withUpperBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<C> domain) {
			switch (boundType) {
			case CLOSED:
				C previous = domain.previous(endpoint);
				return (previous == null) ? Cut.<C>aboveAll() : new AboveValue<C>(previous);
			case OPEN:
				return this;
			default:
				throw new AssertionError();
			}
		}

		@Override
		void describeAsLowerBound(StringBuilder sb) {
			sb.append('[').append(endpoint);
		}

		@Override
		void describeAsUpperBound(StringBuilder sb) {
			sb.append(endpoint).append(')');
		}

		@Override
		C leastValueAbove(org.magneton.core.collect.DiscreteDomain<C> domain) {
			return endpoint;
		}

		@Override
		@CheckForNull
		C greatestValueBelow(org.magneton.core.collect.DiscreteDomain<C> domain) {
			return domain.previous(endpoint);
		}

		@Override
		public int hashCode() {
			return endpoint.hashCode();
		}

		@Override
		public String toString() {
			return "\\" + endpoint + "/";
		}

	}

	private static final class AboveValue<C extends Comparable> extends Cut<C> {

		private static final long serialVersionUID = 0;

		AboveValue(C endpoint) {
			super(checkNotNull(endpoint));
		}

		@Override
		boolean isLessThan(C value) {
			return Range.compareOrThrow(endpoint, value) < 0;
		}

		@Override
		org.magneton.core.collect.BoundType typeAsLowerBound() {
			return org.magneton.core.collect.BoundType.OPEN;
		}

		@Override
		org.magneton.core.collect.BoundType typeAsUpperBound() {
			return org.magneton.core.collect.BoundType.CLOSED;
		}

		@Override
		Cut<C> withLowerBoundType(org.magneton.core.collect.BoundType boundType,
				org.magneton.core.collect.DiscreteDomain<C> domain) {
			switch (boundType) {
			case OPEN:
				return this;
			case CLOSED:
				C next = domain.next(endpoint);
				return (next == null) ? Cut.<C>belowAll() : belowValue(next);
			default:
				throw new AssertionError();
			}
		}

		@Override
		Cut<C> withUpperBoundType(BoundType boundType, org.magneton.core.collect.DiscreteDomain<C> domain) {
			switch (boundType) {
			case OPEN:
				C next = domain.next(endpoint);
				return (next == null) ? Cut.<C>aboveAll() : belowValue(next);
			case CLOSED:
				return this;
			default:
				throw new AssertionError();
			}
		}

		@Override
		void describeAsLowerBound(StringBuilder sb) {
			sb.append('(').append(endpoint);
		}

		@Override
		void describeAsUpperBound(StringBuilder sb) {
			sb.append(endpoint).append(']');
		}

		@Override
		@CheckForNull
		C leastValueAbove(org.magneton.core.collect.DiscreteDomain<C> domain) {
			return domain.next(endpoint);
		}

		@Override
		C greatestValueBelow(org.magneton.core.collect.DiscreteDomain<C> domain) {
			return endpoint;
		}

		@Override
		Cut<C> canonical(DiscreteDomain<C> domain) {
			C next = leastValueAbove(domain);
			return (next != null) ? belowValue(next) : Cut.<C>aboveAll();
		}

		@Override
		public int hashCode() {
			return ~endpoint.hashCode();
		}

		@Override
		public String toString() {
			return "/" + endpoint + "\\";
		}

	}

}
