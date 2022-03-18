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

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;

import javax.annotation.CheckForNull;

/**
 * A sorted multiset which forwards all its method calls to another sorted multiset.
 * Subclasses should override one or more methods to modify the behavior of the backing
 * multiset as desired per the
 * <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p>
 * <b>Warning:</b> The methods of {@code ForwardingSortedMultiset} forward
 * <b>indiscriminately</b> to the methods of the delegate. For example, overriding
 * {@link #add(Object, int)} alone <b>will not</b> change the behavior of
 * {@link #add(Object)}, which can lead to unexpected behavior. In this case, you should
 * override {@code add(Object)} as well, either providing your own implementation, or
 * delegating to the provided {@code standardAdd} method.
 *
 * <p>
 * <b>{@code default} method warning:</b> This class does <i>not</i> forward calls to
 * {@code
 * default} methods. Instead, it inherits their default implementations. When those
 * implementations invoke methods, they invoke methods on the
 * {@code ForwardingSortedMultiset}.
 *
 * <p>
 * The {@code standard} methods and any collection views they return are not guaranteed to
 * be thread-safe, even when all of the methods that they depend on are thread-safe.
 *
 * @author Louis Wasserman
 * @since 15.0
 */
@ElementTypesAreNonnullByDefault
public abstract class ForwardingSortedMultiset<E extends Object> extends ForwardingMultiset<E>
		implements org.magneton.core.collect.SortedMultiset<E> {

	/** Constructor for use by subclasses. */
	protected ForwardingSortedMultiset() {
	}

	@Override
	protected abstract org.magneton.core.collect.SortedMultiset<E> delegate();

	@Override
	public NavigableSet<E> elementSet() {
		return delegate().elementSet();
	}

	@Override
	public Comparator<? super E> comparator() {
		return delegate().comparator();
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> descendingMultiset() {
		return delegate().descendingMultiset();
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> firstEntry() {
		return delegate().firstEntry();
	}

	/**
	 * A sensible definition of {@link #firstEntry()} in terms of
	 * {@code entrySet().iterator()}.
	 *
	 * <p>
	 * If you override {@link #entrySet()}, you may wish to override {@link #firstEntry()}
	 * to forward to this implementation.
	 */
	@CheckForNull
	protected Multiset.Entry<E> standardFirstEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
		if (!entryIterator.hasNext()) {
			return null;
		}
		Multiset.Entry<E> entry = entryIterator.next();
		return org.magneton.core.collect.Multisets.immutableEntry(entry.getElement(), entry.getCount());
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> lastEntry() {
		return delegate().lastEntry();
	}

	/**
	 * A sensible definition of {@link #lastEntry()} in terms of {@code
	 * descendingMultiset().entrySet().iterator()}.
	 *
	 * <p>
	 * If you override {@link #descendingMultiset} or {@link #entrySet()}, you may wish to
	 * override {@link #firstEntry()} to forward to this implementation.
	 */
	@CheckForNull
	protected Multiset.Entry<E> standardLastEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
		if (!entryIterator.hasNext()) {
			return null;
		}
		Multiset.Entry<E> entry = entryIterator.next();
		return org.magneton.core.collect.Multisets.immutableEntry(entry.getElement(), entry.getCount());
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollFirstEntry() {
		return delegate().pollFirstEntry();
	}

	/**
	 * A sensible definition of {@link #pollFirstEntry()} in terms of
	 * {@code entrySet().iterator()}.
	 *
	 * <p>
	 * If you override {@link #entrySet()}, you may wish to override
	 * {@link #pollFirstEntry()} to forward to this implementation.
	 */
	@CheckForNull
	protected Multiset.Entry<E> standardPollFirstEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
		if (!entryIterator.hasNext()) {
			return null;
		}
		Multiset.Entry<E> entry = entryIterator.next();
		entry = org.magneton.core.collect.Multisets.immutableEntry(entry.getElement(), entry.getCount());
		entryIterator.remove();
		return entry;
	}

	@Override
	@CheckForNull
	public Multiset.Entry<E> pollLastEntry() {
		return delegate().pollLastEntry();
	}

	/**
	 * A sensible definition of {@link #pollLastEntry()} in terms of {@code
	 * descendingMultiset().entrySet().iterator()}.
	 *
	 * <p>
	 * If you override {@link #descendingMultiset()} or {@link #entrySet()}, you may wish
	 * to override {@link #pollLastEntry()} to forward to this implementation.
	 */
	@CheckForNull
	protected Multiset.Entry<E> standardPollLastEntry() {
		Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
		if (!entryIterator.hasNext()) {
			return null;
		}
		Multiset.Entry<E> entry = entryIterator.next();
		entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
		entryIterator.remove();
		return entry;
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> headMultiset(@ParametricNullness E upperBound,
			org.magneton.core.collect.BoundType boundType) {
		return delegate().headMultiset(upperBound, boundType);
	}

	@Override
	public org.magneton.core.collect.SortedMultiset<E> subMultiset(@ParametricNullness E lowerBound,
			org.magneton.core.collect.BoundType lowerBoundType, @ParametricNullness E upperBound,
			org.magneton.core.collect.BoundType upperBoundType) {
		return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
	}

	/**
	 * A sensible definition of
	 * {@link #subMultiset(Object, org.magneton.core.collect.BoundType, Object, org.magneton.core.collect.BoundType)}
	 * in terms of {@link #headMultiset(Object, org.magneton.core.collect.BoundType)
	 * headMultiset} and {@link #tailMultiset(Object, org.magneton.core.collect.BoundType)
	 * tailMultiset}.
	 *
	 * <p>
	 * If you override either of these methods, you may wish to override
	 * {@link #subMultiset(Object, org.magneton.core.collect.BoundType, Object, org.magneton.core.collect.BoundType)}
	 * to forward to this implementation.
	 */
	protected org.magneton.core.collect.SortedMultiset<E> standardSubMultiset(@ParametricNullness E lowerBound,
			org.magneton.core.collect.BoundType lowerBoundType, @ParametricNullness E upperBound,
			org.magneton.core.collect.BoundType upperBoundType) {
		return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
	}

	@Override
	public SortedMultiset<E> tailMultiset(@ParametricNullness E lowerBound, BoundType boundType) {
		return delegate().tailMultiset(lowerBound, boundType);
	}

	/**
	 * A sensible implementation of
	 * {@link org.magneton.core.collect.SortedMultiset#elementSet} in terms of the
	 * following methods: {@link org.magneton.core.collect.SortedMultiset#clear},
	 * {@link org.magneton.core.collect.SortedMultiset#comparator},
	 * {@link org.magneton.core.collect.SortedMultiset#contains},
	 * {@link org.magneton.core.collect.SortedMultiset#containsAll},
	 * {@link org.magneton.core.collect.SortedMultiset#count},
	 * {@link org.magneton.core.collect.SortedMultiset#firstEntry}
	 * {@link org.magneton.core.collect.SortedMultiset#headMultiset},
	 * {@link org.magneton.core.collect.SortedMultiset#isEmpty},
	 * {@link org.magneton.core.collect.SortedMultiset#lastEntry},
	 * {@link org.magneton.core.collect.SortedMultiset#subMultiset},
	 * {@link org.magneton.core.collect.SortedMultiset#tailMultiset}, the {@code size()}
	 * and {@code iterator()} methods of
	 * {@link org.magneton.core.collect.SortedMultiset#entrySet}, and
	 * {@link org.magneton.core.collect.SortedMultiset#remove(Object, int)}. In many
	 * situations, you may wish to override
	 * {@link org.magneton.core.collect.SortedMultiset#elementSet} to forward to this
	 * implementation or a subclass thereof.
	 *
	 * @since 15.0
	 */
	protected class StandardElementSet extends org.magneton.core.collect.SortedMultisets.NavigableElementSet<E> {

		/** Constructor for use by subclasses. */
		public StandardElementSet() {
			super(ForwardingSortedMultiset.this);
		}

	}

	/**
	 * A skeleton implementation of a descending multiset view. Normally,
	 * {@link #descendingMultiset()} will not reflect any changes you make to the behavior
	 * of methods such as {@link #add(Object)} or {@link #pollFirstEntry}. This skeleton
	 * implementation correctly delegates each of its operations to the appropriate
	 * methods of this {@code
	 * ForwardingSortedMultiset}.
	 *
	 * <p>
	 * In many cases, you may wish to override {@link #descendingMultiset()} to return an
	 * instance of a subclass of {@code StandardDescendingMultiset}.
	 *
	 * @since 15.0
	 */
	protected abstract class StandardDescendingMultiset extends org.magneton.core.collect.DescendingMultiset<E> {

		/** Constructor for use by subclasses. */
		public StandardDescendingMultiset() {
		}

		@Override
		org.magneton.core.collect.SortedMultiset<E> forwardMultiset() {
			return ForwardingSortedMultiset.this;
		}

	}

}
