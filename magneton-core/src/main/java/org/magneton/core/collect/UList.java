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
import java.math.RoundingMode;
import java.util.AbstractList;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

import javax.annotation.VisibleForTesting;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.magneton.core.lang.UAssert;
import org.magneton.core.primitives.UInt;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;
import static com.google.common.base.Preconditions.checkPositionIndexes;
import static com.google.common.base.Preconditions.checkState;

/**
 * Static utility methods pertaining to {@link List} instances. Also see this class's
 * counterparts {@link Sets}, {@link Maps} and {@link Queues}.
 *
 * <p>
 * See the Guava User Guide article on
 * <a href= "https://github.com/google/guava/wiki/CollectionUtilitiesExplained#lists">
 * {@code Lists}</a>.
 *
 * @author Kevin Bourrillion
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2.0
 */
public final class UList {

	private UList() {
	}

	// ArrayList

	/**
	 * Creates a <i>mutable</i>, empty {@code ArrayList} instance (for Java 6 and
	 * earlier).
	 *
	 * <p>
	 * <b>Note:</b> if mutability is not required, use {@link ImmutableList#of()} instead.
	 *
	 * <p>
	 * <b>Note for Java 7 and later:</b> this method is now unnecessary and should be
	 * treated as deprecated. Instead, use the {@code ArrayList}
	 * {@linkplain ArrayList#ArrayList() constructor} directly, taking advantage of the
	 * new <a href="http://goo.gl/iz2Wi">"diamond" syntax</a>.
	 */
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<>();
	}

	/**
	 * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements.
	 *
	 * <p>
	 * <b>Note:</b> essentially the only reason to use this method is when you will need
	 * to add or remove elements later. Otherwise, for non-null elements use
	 * {@link ImmutableList#of()} (for varargs) or {@link ImmutableList#copyOf(Object[])}
	 * (for an array) instead. If any elements might be null, or you need support for
	 * {@link List#set(int, Object)}, use {@link Arrays#asList}.
	 *
	 * <p>
	 * Note that even when you do need the ability to add or remove, this method provides
	 * only a tiny bit of syntactic sugar for {@code newArrayList(}{@link Arrays#asList
	 * asList}{@code
	 * (...))}, or for creating an empty list then calling {@link Collections#addAll}.
	 * This method is not actually very useful and will likely be deprecated in the
	 * future.
	 */
	@SafeVarargs
	public static <E> ArrayList<E> newArrayList(E... elements) {
		UAssert.notNull(elements);
		// Avoid integer overflow when a large array is passed in
		int capacity = computeArrayListCapacity(elements.length);
		ArrayList<E> list = new ArrayList<>(capacity);
		Collections.addAll(list, elements);
		return list;
	}

	/**
	 * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements;
	 * a very thin shortcut for creating an empty list then calling
	 * {@link Iterables#addAll}.
	 *
	 * <p>
	 * <b>Note:</b> if mutability is not required and the elements are non-null, use
	 * {@link ImmutableList#copyOf(Iterable)} instead. (Or, change {@code elements} to be
	 * a {@link FluentIterable} and call {@code elements.toList()}.)
	 *
	 * <p>
	 * <b>Note for Java 7 and later:</b> if {@code elements} is a {@link Collection}, you
	 * don't need this method. Use the {@code ArrayList}
	 * {@linkplain ArrayList#ArrayList(Collection) constructor} directly, taking advantage
	 * of the new <a href="http://goo.gl/iz2Wi">"diamond" syntax</a>.
	 */
	public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
		UAssert.notNull(elements);
		// Let ArrayList's sizing logic work, if possible
		return (elements instanceof Collection) ? new ArrayList<>((Collection<? extends E>) elements)
				: newArrayList(elements.iterator());
	}

	/**
	 * Creates a <i>mutable</i> {@code ArrayList} instance containing the given elements;
	 * a very thin shortcut for creating an empty list and then calling
	 * {@link Iterators#addAll}.
	 *
	 * <p>
	 * <b>Note:</b> if mutability is not required and the elements are non-null, use
	 * {@link ImmutableList#copyOf(Iterator)} instead.
	 */
	public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
		ArrayList<E> list = newArrayList();
		Iterators.addAll(list, elements);
		return list;
	}

	@VisibleForTesting
	static int computeArrayListCapacity(int arraySize) {
		UAssert.nonnegative(arraySize, "arraySize");
		// TODO(kevinb): Figure out the right behavior, and document it
		return UInt.saturatedCast(5L + arraySize + (arraySize / 10));
	}

	/**
	 * Creates an {@code ArrayList} instance backed by an array with the specified initial
	 * size; simply delegates to {@link ArrayList#ArrayList(int)}.
	 *
	 * <p>
	 * <b>Note for Java 7 and later:</b> this method is now unnecessary and should be
	 * treated as deprecated. Instead, use {@code new }{@link ArrayList#ArrayList(int)
	 * ArrayList}{@code <>(int)} directly, taking advantage of the new
	 * <a href="http://goo.gl/iz2Wi">"diamond" syntax</a>. (Unlike here, there is no risk
	 * of overload ambiguity, since the {@code ArrayList} constructors very wisely did not
	 * accept varargs.)
	 * @param initialArraySize the exact size of the initial backing array for the
	 * returned array list ({@code ArrayList} documentation calls this value the
	 * "capacity")
	 * @return a new, empty {@code ArrayList} which is guaranteed not to resize itself
	 * unless its size reaches {@code initialArraySize + 1}
	 * @throws IllegalArgumentException if {@code initialArraySize} is negative
	 */
	@GwtCompatible(serializable = true)
	public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
		UAssert.nonnegative(initialArraySize, "initialArraySize");
		return new ArrayList<>(initialArraySize);
	}

	/**
	 * Creates an {@code ArrayList} instance to hold {@code estimatedSize} elements,
	 * <i>plus</i> an unspecified amount of padding; you almost certainly mean to call
	 * {@link #newArrayListWithCapacity} (see that method for further advice on usage).
	 *
	 * <p>
	 * <b>Note:</b> This method will soon be deprecated. Even in the rare case that you do
	 * want some amount of padding, it's best if you choose your desired amount
	 * explicitly.
	 * @param estimatedSize an estimate of the eventual {@link List#size()} of the new
	 * list
	 * @return a new, empty {@code ArrayList}, sized appropriately to hold the estimated
	 * number of elements
	 * @throws IllegalArgumentException if {@code estimatedSize} is negative
	 */
	public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
		return new ArrayList<>(computeArrayListCapacity(estimatedSize));
	}

	// LinkedList

	/**
	 * Creates a <i>mutable</i>, empty {@code LinkedList} instance (for Java 6 and
	 * earlier).
	 *
	 * <p>
	 * <b>Note:</b> if you won't be adding any elements to the list, use
	 * {@link ImmutableList#of()} instead.
	 *
	 * <p>
	 * <b>Performance note:</b> {@link ArrayList} and {@link java.util.ArrayDeque}
	 * consistently outperform {@code LinkedList} except in certain rare and specific
	 * situations. Unless you have spent a lot of time benchmarking your specific needs,
	 * use one of those instead.
	 *
	 * <p>
	 * <b>Note for Java 7 and later:</b> this method is now unnecessary and should be
	 * treated as deprecated. Instead, use the {@code LinkedList}
	 * {@linkplain LinkedList#LinkedList() constructor} directly, taking advantage of the
	 * new <a href="http://goo.gl/iz2Wi">"diamond" syntax</a>.
	 */
	public static <E> LinkedList<E> newLinkedList() {
		return new LinkedList<>();
	}

	/**
	 * Creates a <i>mutable</i> {@code LinkedList} instance containing the given elements;
	 * a very thin shortcut for creating an empty list then calling
	 * {@link Iterables#addAll}.
	 *
	 * <p>
	 * <b>Note:</b> if mutability is not required and the elements are non-null, use
	 * {@link ImmutableList#copyOf(Iterable)} instead. (Or, change {@code elements} to be
	 * a {@link FluentIterable} and call {@code elements.toList()}.)
	 *
	 * <p>
	 * <b>Performance note:</b> {@link ArrayList} and {@link java.util.ArrayDeque}
	 * consistently outperform {@code LinkedList} except in certain rare and specific
	 * situations. Unless you have spent a lot of time benchmarking your specific needs,
	 * use one of those instead.
	 *
	 * <p>
	 * <b>Note for Java 7 and later:</b> if {@code elements} is a {@link Collection}, you
	 * don't need this method. Use the {@code LinkedList}
	 * {@linkplain LinkedList#LinkedList(Collection) constructor} directly, taking
	 * advantage of the new <a href="http://goo.gl/iz2Wi">"diamond" syntax</a>.
	 */
	public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
		LinkedList<E> list = newLinkedList();
		Iterables.addAll(list, elements);
		return list;
	}

	/**
	 * Creates an empty {@code CopyOnWriteArrayList} instance.
	 *
	 * <p>
	 * <b>Note:</b> if you need an immutable empty {@link List}, use
	 * {@link Collections#emptyList} instead.
	 * @return a new, empty {@code CopyOnWriteArrayList}
	 * @since 12.0
	 */
	public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
		return new CopyOnWriteArrayList<>();
	}

	/**
	 * Creates a {@code CopyOnWriteArrayList} instance containing the given elements.
	 * @param elements the elements that the list should contain, in order
	 * @return a new {@code CopyOnWriteArrayList} containing those elements
	 * @since 12.0
	 */
	public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
		// We copy elements to an ArrayList first, rather than incurring the
		// quadratic cost of adding them to the COWAL directly.
		Collection<? extends E> elementsCollection = (elements instanceof Collection)
				? (Collection<? extends E>) elements : newArrayList(elements);
		return new CopyOnWriteArrayList<>(elementsCollection);
	}

	/**
	 * Returns an unmodifiable list containing the specified first element and backed by
	 * the specified array of additional elements. Changes to the {@code rest} array will
	 * be reflected in the returned list. Unlike {@link Arrays#asList}, the returned list
	 * is unmodifiable.
	 *
	 * <p>
	 * This is useful when a varargs method needs to use a signature such as
	 * {@code (Foo firstFoo,
	 * Foo... moreFoos)}, in order to avoid overload ambiguity or to enforce a minimum
	 * argument count.
	 *
	 * <p>
	 * The returned list is serializable and implements {@link RandomAccess}.
	 * @param first the first element
	 * @param rest an array of additional elements, possibly empty
	 * @return an unmodifiable list containing the specified elements
	 */
	public static <E> List<E> asList(@Nullable E first, E[] rest) {
		return new OnePlusArrayList<>(first, rest);
	}

	/**
	 * Returns an unmodifiable list containing the specified first and second element, and
	 * backed by the specified array of additional elements. Changes to the {@code rest}
	 * array will be reflected in the returned list. Unlike {@link Arrays#asList}, the
	 * returned list is unmodifiable.
	 *
	 * <p>
	 * This is useful when a varargs method needs to use a signature such as
	 * {@code (Foo firstFoo,
	 * Foo secondFoo, Foo... moreFoos)}, in order to avoid overload ambiguity or to
	 * enforce a minimum argument count.
	 *
	 * <p>
	 * The returned list is serializable and implements {@link RandomAccess}.
	 * @param first the first element
	 * @param second the second element
	 * @param rest an array of additional elements, possibly empty
	 * @return an unmodifiable list containing the specified elements
	 */
	public static <E> List<E> asList(@Nullable E first, @Nullable E second, E[] rest) {
		return new TwoPlusArrayList<>(first, second, rest);
	}

	/**
	 * Returns a list that applies {@code function} to each element of {@code fromList}.
	 * The returned list is a transformed view of {@code fromList}; changes to
	 * {@code fromList} will be reflected in the returned list and vice versa.
	 *
	 * <p>
	 * Since functions are not reversible, the transform is one-way and new items cannot
	 * be stored in the returned list. The {@code add}, {@code addAll} and {@code set}
	 * methods are unsupported in the returned list.
	 *
	 * <p>
	 * The function is applied lazily, invoked when needed. This is necessary for the
	 * returned list to be a view, but it means that the function will be applied many
	 * times for bulk operations like {@link List#contains} and {@link List#hashCode}. For
	 * this to perform well, {@code
	 * function} should be fast. To avoid lazy evaluation when the returned list doesn't
	 * need to be a view, copy the returned list into a new list of your choosing.
	 *
	 * <p>
	 * If {@code fromList} implements {@link RandomAccess}, so will the returned list. The
	 * returned list is threadsafe if the supplied list and function are.
	 *
	 * <p>
	 * If only a {@code Collection} or {@code Iterable} input is available, use
	 * {@link Collections2#transform} or {@link Iterables#transform}.
	 *
	 * <p>
	 * <b>Note:</b> serializing the returned list is implemented by serializing
	 * {@code fromList}, its contents, and {@code function} -- <i>not</i> by serializing
	 * the transformed values. This can lead to surprising behavior, so serializing the
	 * returned list is <b>not recommended</b>. Instead, copy the list using
	 * {@link ImmutableList#copyOf(Collection)} (for example), then serialize the copy.
	 * Other methods similar to this do not implement serialization at all for this
	 * reason.
	 *
	 * <p>
	 * <b>Java 8 users:</b> many use cases for this method are better addressed by
	 * {@link java.util.stream.Stream#map}. This method is not being deprecated, but we
	 * gently encourage you to migrate to streams.
	 */
	public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
		return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList<>(fromList, function)
				: new TransformingSequentialList<>(fromList, function);
	}

	/**
	 * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list, each of
	 * the same size (the final list may be smaller). For example, partitioning a list
	 * containing {@code [a, b,
	 * c, d, e]} with a partition size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer
	 * list containing two inner lists of three and two elements, all in the original
	 * order.
	 *
	 * <p>
	 * The outer list is unmodifiable, but reflects the latest state of the source list.
	 * The inner lists are sublist views of the original list, produced on demand using
	 * {@link List#subList(int, int)}, and are subject to all the usual caveats about
	 * modification as explained in that API.
	 * @param list the list to return consecutive sublists of
	 * @param size the desired size of each sublist (the last may be smaller)
	 * @return a list of consecutive sublists
	 * @throws IllegalArgumentException if {@code partitionSize} is nonpositive
	 */
	public static <T> List<List<T>> partition(List<T> list, int size) {
		UAssert.notNull(list);
		UAssert.isTrue(size > 0);
		return (list instanceof RandomAccess) ? new RandomAccessPartition<>(list, size) : new Partition<>(list, size);
	}

	/**
	 * Returns a reversed view of the specified list. For example, {@code
	 * Lists.reverse(Arrays.asList(1, 2, 3))} returns a list containing {@code 3, 2, 1}.
	 * The returned list is backed by this list, so changes in the returned list are
	 * reflected in this list, and vice-versa. The returned list supports all of the
	 * optional list operations supported by this list.
	 *
	 * <p>
	 * The returned list is random-access if the specified list is random access.
	 *
	 * @since 7.0
	 */
	public static <T> List<T> reverse(List<T> list) {
		if (list instanceof ImmutableList) {
			return ((ImmutableList<T>) list).reverse();
		}
		else if (list instanceof ReverseList) {
			return ((ReverseList<T>) list).getForwardList();
		}
		else if (list instanceof RandomAccess) {
			return new RandomAccessReverseList<>(list);
		}
		else {
			return new ReverseList<>(list);
		}
	}

	/** An implementation of {@link List#hashCode()}. */
	static int hashCodeImpl(List<?> list) {
		// TODO(lowasser): worth optimizing for RandomAccess?
		int hashCode = 1;
		for (Object o : list) {
			hashCode = 31 * hashCode + (o == null ? 0 : o.hashCode());

			hashCode = ~~hashCode;
			// needed to deal with GWT integer overflow
		}
		return hashCode;
	}

	/** An implementation of {@link List#equals(Object)}. */
	static boolean equalsImpl(List<?> thisList, @Nullable Object other) {
		if (other == UAssert.notNull(thisList)) {
			return true;
		}
		if (!(other instanceof List)) {
			return false;
		}
		List<?> otherList = (List<?>) other;
		int size = thisList.size();
		if (size != otherList.size()) {
			return false;
		}
		if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
			// avoid allocation and use the faster loop
			for (int i = 0; i < size; i++) {
				if (!Objects.equal(thisList.get(i), otherList.get(i))) {
					return false;
				}
			}
			return true;
		}
		else {
			return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
		}
	}

	/** An implementation of {@link List#addAll(int, Collection)}. */
	static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
		boolean changed = false;
		ListIterator<E> listIterator = list.listIterator(index);
		for (E e : elements) {
			listIterator.add(e);
			changed = true;
		}
		return changed;
	}

	/** An implementation of {@link List#indexOf(Object)}. */
	static int indexOfImpl(List<?> list, @Nullable Object element) {
		if (list instanceof RandomAccess) {
			return indexOfRandomAccess(list, element);
		}
		else {
			ListIterator<?> listIterator = list.listIterator();
			while (listIterator.hasNext()) {
				if (Objects.equal(element, listIterator.next())) {
					return listIterator.previousIndex();
				}
			}
			return -1;
		}
	}

	private static int indexOfRandomAccess(List<?> list, @Nullable Object element) {
		int size = list.size();
		if (element == null) {
			for (int i = 0; i < size; i++) {
				if (list.get(i) == null) {
					return i;
				}
			}
		}
		else {
			for (int i = 0; i < size; i++) {
				if (element.equals(list.get(i))) {
					return i;
				}
			}
		}
		return -1;
	}

	/** An implementation of {@link List#lastIndexOf(Object)}. */
	static int lastIndexOfImpl(List<?> list, @Nullable Object element) {
		if (list instanceof RandomAccess) {
			return lastIndexOfRandomAccess(list, element);
		}
		else {
			ListIterator<?> listIterator = list.listIterator(list.size());
			while (listIterator.hasPrevious()) {
				if (Objects.equal(element, listIterator.previous())) {
					return listIterator.nextIndex();
				}
			}
			return -1;
		}
	}

	private static int lastIndexOfRandomAccess(List<?> list, @Nullable Object element) {
		if (element == null) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (list.get(i) == null) {
					return i;
				}
			}
		}
		else {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (element.equals(list.get(i))) {
					return i;
				}
			}
		}
		return -1;
	}

	/** Returns an implementation of {@link List#listIterator(int)}. */
	static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
		return new AbstractListWrapper<>(list).listIterator(index);
	}

	/** An implementation of {@link List#subList(int, int)}. */
	static <E> List<E> subListImpl(final List<E> list, int fromIndex, int toIndex) {
		List<E> wrapper;
		if (list instanceof RandomAccess) {
			wrapper = new RandomAccessListWrapper<E>(list) {
				private static final long serialVersionUID = 0;

				@Override
				public ListIterator<E> listIterator(int index) {
					return backingList.listIterator(index);
				}

			};
		}
		else {
			wrapper = new AbstractListWrapper<E>(list) {
				private static final long serialVersionUID = 0;

				@Override
				public ListIterator<E> listIterator(int index) {
					return backingList.listIterator(index);
				}

			};
		}
		return wrapper.subList(fromIndex, toIndex);
	}

	/** Used to avoid http://bugs.sun.com/view_bug.do?bug_id=6558557 */
	static <T> List<T> cast(Iterable<T> iterable) {
		return (List<T>) iterable;
	}

	/**
	 * @see UList#asList(Object, Object[])
	 */
	private static class OnePlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {

		private static final long serialVersionUID = 0;

		final @Nullable E first;

		final E[] rest;

		OnePlusArrayList(@Nullable E first, E[] rest) {
			this.first = first;
			this.rest = checkNotNull(rest);
		}

		@Override
		public int size() {
			return IntMath.saturatedAdd(rest.length, 1);
		}

		@Override
		public E get(int index) {
			// check explicitly so the IOOBE will have the right message
			checkElementIndex(index, size());
			return (index == 0) ? first : rest[index - 1];
		}

	}

	/**
	 * @see UList#asList(Object, Object, Object[])
	 */
	private static class TwoPlusArrayList<E> extends AbstractList<E> implements Serializable, RandomAccess {

		private static final long serialVersionUID = 0;

		final @Nullable E first;

		final @Nullable E second;

		final E[] rest;

		TwoPlusArrayList(@Nullable E first, @Nullable E second, E[] rest) {
			this.first = first;
			this.second = second;
			this.rest = checkNotNull(rest);
		}

		@Override
		public int size() {
			return IntMath.saturatedAdd(rest.length, 2);
		}

		@Override
		public E get(int index) {
			switch (index) {
			case 0:
				return first;
			case 1:
				return second;
			default:
				// check explicitly so the IOOBE will have the right message
				checkElementIndex(index, size());
				return rest[index - 2];
			}
		}

	}

	/**
	 * Implementation of a sequential transforming list.
	 *
	 * @see UList#transform
	 */
	private static class TransformingSequentialList<F, T> extends AbstractSequentialList<T> implements Serializable {

		private static final long serialVersionUID = 0;

		final List<F> fromList;

		final Function<? super F, ? extends T> function;

		TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
			this.fromList = checkNotNull(fromList);
			this.function = checkNotNull(function);
		}

		/**
		 * The default implementation inherited is based on iteration and removal of each
		 * element which can be overkill. That's why we forward this call directly to the
		 * backing list.
		 */
		@Override
		public void clear() {
			fromList.clear();
		}

		@Override
		public int size() {
			return fromList.size();
		}

		@Override
		public ListIterator<T> listIterator(final int index) {
			return new TransformedListIterator<F, T>(fromList.listIterator(index)) {
				@Override
				T transform(F from) {
					return function.apply(from);
				}
			};
		}

		@Override
		public boolean removeIf(Predicate<? super T> filter) {
			checkNotNull(filter);
			return fromList.removeIf(element -> filter.test(function.apply(element)));
		}

	}

	/**
	 * Implementation of a transforming random access list. We try to make as many of
	 * these methods pass-through to the source list as possible so that the performance
	 * characteristics of the source list and transformed list are similar.
	 *
	 * @see UList#transform
	 */
	private static class TransformingRandomAccessList<F, T> extends AbstractList<T>
			implements RandomAccess, Serializable {

		private static final long serialVersionUID = 0;

		final List<F> fromList;

		final Function<? super F, ? extends T> function;

		TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
			this.fromList = checkNotNull(fromList);
			this.function = checkNotNull(function);
		}

		@Override
		public void clear() {
			fromList.clear();
		}

		@Override
		public T get(int index) {
			return function.apply(fromList.get(index));
		}

		@Override
		public Iterator<T> iterator() {
			return listIterator();
		}

		@Override
		public ListIterator<T> listIterator(int index) {
			return new TransformedListIterator<F, T>(fromList.listIterator(index)) {
				@Override
				T transform(F from) {
					return function.apply(from);
				}
			};
		}

		@Override
		public boolean isEmpty() {
			return fromList.isEmpty();
		}

		@Override
		public boolean removeIf(Predicate<? super T> filter) {
			checkNotNull(filter);
			return fromList.removeIf(element -> filter.test(function.apply(element)));
		}

		@Override
		public T remove(int index) {
			return function.apply(fromList.remove(index));
		}

		@Override
		public int size() {
			return fromList.size();
		}

	}

	private static class Partition<T> extends AbstractList<List<T>> {

		final List<T> list;

		final int size;

		Partition(List<T> list, int size) {
			this.list = list;
			this.size = size;
		}

		@Override
		public List<T> get(int index) {
			checkElementIndex(index, size());
			int start = index * size;
			int end = Math.min(start + size, list.size());
			return list.subList(start, end);
		}

		@Override
		public int size() {
			return IntMath.divide(list.size(), size, RoundingMode.CEILING);
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}

	}

	private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {

		RandomAccessPartition(List<T> list, int size) {
			super(list, size);
		}

	}

	private static class ReverseList<T> extends AbstractList<T> {

		private final List<T> forwardList;

		ReverseList(List<T> forwardList) {
			this.forwardList = checkNotNull(forwardList);
		}

		List<T> getForwardList() {
			return forwardList;
		}

		private int reverseIndex(int index) {
			int size = size();
			checkElementIndex(index, size);
			return (size - 1) - index;
		}

		private int reversePosition(int index) {
			int size = size();
			checkPositionIndex(index, size);
			return size - index;
		}

		@Override
		public void add(int index, @Nullable T element) {
			forwardList.add(reversePosition(index), element);
		}

		@Override
		public void clear() {
			forwardList.clear();
		}

		@Override
		public T remove(int index) {
			return forwardList.remove(reverseIndex(index));
		}

		@Override
		protected void removeRange(int fromIndex, int toIndex) {
			subList(fromIndex, toIndex).clear();
		}

		@Override
		public T set(int index, @Nullable T element) {
			return forwardList.set(reverseIndex(index), element);
		}

		@Override
		public T get(int index) {
			return forwardList.get(reverseIndex(index));
		}

		@Override
		public int size() {
			return forwardList.size();
		}

		@Override
		public List<T> subList(int fromIndex, int toIndex) {
			checkPositionIndexes(fromIndex, toIndex, size());
			return reverse(forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
		}

		@Override
		public Iterator<T> iterator() {
			return listIterator();
		}

		@Override
		public ListIterator<T> listIterator(int index) {
			int start = reversePosition(index);
			final ListIterator<T> forwardIterator = forwardList.listIterator(start);
			return new ListIterator<T>() {

				boolean canRemoveOrSet;

				@Override
				public void add(T e) {
					forwardIterator.add(e);
					forwardIterator.previous();
					canRemoveOrSet = false;
				}

				@Override
				public boolean hasNext() {
					return forwardIterator.hasPrevious();
				}

				@Override
				public boolean hasPrevious() {
					return forwardIterator.hasNext();
				}

				@Override
				public T next() {
					if (!hasNext()) {
						throw new NoSuchElementException();
					}
					canRemoveOrSet = true;
					return forwardIterator.previous();
				}

				@Override
				public int nextIndex() {
					return reversePosition(forwardIterator.nextIndex());
				}

				@Override
				public T previous() {
					if (!hasPrevious()) {
						throw new NoSuchElementException();
					}
					canRemoveOrSet = true;
					return forwardIterator.next();
				}

				@Override
				public int previousIndex() {
					return nextIndex() - 1;
				}

				@Override
				public void remove() {
					checkRemove(canRemoveOrSet);
					forwardIterator.remove();
					canRemoveOrSet = false;
				}

				@Override
				public void set(T e) {
					checkState(canRemoveOrSet);
					forwardIterator.set(e);
				}
			};
		}

	}

	private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {

		RandomAccessReverseList(List<T> forwardList) {
			super(forwardList);
		}

	}

	private static class AbstractListWrapper<E> extends AbstractList<E> {

		final List<E> backingList;

		AbstractListWrapper(List<E> backingList) {
			this.backingList = checkNotNull(backingList);
		}

		@Override
		public void add(int index, E element) {
			backingList.add(index, element);
		}

		@Override
		public boolean addAll(int index, Collection<? extends E> c) {
			return backingList.addAll(index, c);
		}

		@Override
		public E get(int index) {
			return backingList.get(index);
		}

		@Override
		public E remove(int index) {
			return backingList.remove(index);
		}

		@Override
		public E set(int index, E element) {
			return backingList.set(index, element);
		}

		@Override
		public boolean contains(Object o) {
			return backingList.contains(o);
		}

		@Override
		public int size() {
			return backingList.size();
		}

	}

	private static class RandomAccessListWrapper<E> extends AbstractListWrapper<E> implements RandomAccess {

		RandomAccessListWrapper(List<E> backingList) {
			super(backingList);
		}

	}

}
