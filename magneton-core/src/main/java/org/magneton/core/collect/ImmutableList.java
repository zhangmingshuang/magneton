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

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import javax.annotations.DoNotCall;
import javax.annotation.Nullable;
import javax.annotations.VisibleForTesting;

import org.magneton.core.base.Preconditions;

import static java.util.Objects.requireNonNull;

/**
 * A {@link List} whose contents will never change, with many other important properties
 * detailed at {@link ImmutableCollection}.
 *
 * <p>
 * See the Guava User Guide article on
 * <a href= "https://github.com/google/guava/wiki/ImmutableCollectionsExplained">
 * immutable collections</a>.
 *
 * @see ImmutableMap
 * @see ImmutableSet
 * @author Kevin Bourrillion
 * @since 2.0
 */
// we're overriding default serialization
@ElementTypesAreNonnullByDefault
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {

	ImmutableList() {
	}

	/**
	 * Returns a {@code Collector} that accumulates the input elements into a new {@code
	 * ImmutableList}, in encounter order.
	 *
	 * @since 21.0
	 */
	public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
		return CollectCollectors.toImmutableList();
	}

	/**
	 * Returns the empty immutable list. This list behaves and performs comparably to
	 * {@link Collections#emptyList}, and is preferable mainly for consistency and
	 * maintainability of your code.
	 *
	 * <p>
	 * <b>Performance note:</b> the instance returned is a singleton.
	 */
	// Casting to any type is safe because the list will never hold any elements.
	public static <E> ImmutableList<E> of() {
		return (ImmutableList<E>) RegularImmutableList.EMPTY;
	}

	/**
	 * Returns an immutable list containing a single element. This list behaves and
	 * performs comparably to {@link Collections#singletonList}, but will not accept a
	 * null element. It is preferable mainly for consistency and maintainability of your
	 * code.
	 * @throws NullPointerException if {@code element} is null
	 */
	public static <E> ImmutableList<E> of(E element) {
		return new SingletonImmutableList<E>(element);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2) {
		return construct(e1, e2);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
		return construct(e1, e2, e3);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
		return construct(e1, e2, e3, e4);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
		return construct(e1, e2, e3, e4, e5);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
		return construct(e1, e2, e3, e4, e5, e6);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
		return construct(e1, e2, e3, e4, e5, e6, e7);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
		return construct(e1, e2, e3, e4, e5, e6, e7, e8);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
		return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
		return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
	}

	// These go up to eleven. After that, you just get the varargs form, and
	// whatever warnings might come along with it. :(

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if any element is null
	 */
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
		return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11);
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 *
	 * <p>
	 * The array {@code others} must not be longer than {@code Integer.MAX_VALUE - 12}.
	 * @throws NullPointerException if any element is null
	 * @since 3.0 (source-compatible since 2.0)
	 */
	@SafeVarargs // For Eclipse. For internal javac we have disabled this pointless type
					// of warning.
	public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12,
			E... others) {
		Preconditions.checkArgument(others.length <= Integer.MAX_VALUE - 12,
				"the total number of elements must fit in an int");
		Object[] array = new Object[12 + others.length];
		array[0] = e1;
		array[1] = e2;
		array[2] = e3;
		array[3] = e4;
		array[4] = e5;
		array[5] = e6;
		array[6] = e7;
		array[7] = e8;
		array[8] = e9;
		array[9] = e10;
		array[10] = e11;
		array[11] = e12;
		System.arraycopy(others, 0, array, 12, others.length);
		return construct(array);
	}

	/**
	 * Returns an immutable list containing the given elements, in order. If
	 * {@code elements} is a {@link Collection}, this method behaves exactly as
	 * {@link #copyOf(Collection)}; otherwise, it behaves exactly as
	 * {@code copyOf(elements.iterator()}.
	 * @throws NullPointerException if {@code elements} contains a null element
	 */
	public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
		Preconditions.checkNotNull(elements); // TODO(kevinb): is this here only for GWT?
		return (elements instanceof Collection) ? copyOf((Collection<? extends E>) elements)
				: copyOf(elements.iterator());
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 *
	 * <p>
	 * Despite the method name, this method attempts to avoid actually copying the data
	 * when it is safe to do so. The exact circumstances under which a copy will or will
	 * not be performed are undocumented and subject to change.
	 *
	 * <p>
	 * Note that if {@code list} is a {@code List<String>}, then
	 * {@code ImmutableList.copyOf(list)} returns an {@code ImmutableList<String>}
	 * containing each of the strings in {@code list}, while ImmutableList.of(list)}
	 * returns an {@code ImmutableList<List<String>>} containing one element (the given
	 * list itself).
	 *
	 * <p>
	 * This method is safe to use even when {@code elements} is a synchronized or
	 * concurrent collection that is currently being modified by another thread.
	 * @throws NullPointerException if {@code elements} contains a null element
	 */
	public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
		if (elements instanceof ImmutableCollection) {
			// all supported methods are covariant
			ImmutableList<E> list = ((ImmutableCollection<E>) elements).asList();
			return list.isPartialView() ? ImmutableList.<E>asImmutableList(list.toArray()) : list;
		}
		return construct(elements.toArray());
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if {@code elements} contains a null element
	 */
	public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
		// We special-case for 0 or 1 elements, but going further is madness.
		if (!elements.hasNext()) {
			return of();
		}
		E first = elements.next();
		if (!elements.hasNext()) {
			return of(first);
		}
		else {
			return new Builder<E>().add(first).addAll(elements).build();
		}
	}

	/**
	 * Returns an immutable list containing the given elements, in order.
	 * @throws NullPointerException if {@code elements} contains a null element
	 * @since 3.0
	 */
	public static <E> ImmutableList<E> copyOf(E[] elements) {
		switch (elements.length) {
		case 0:
			return of();
		case 1:
			return of(elements[0]);
		default:
			return construct(elements.clone());
		}
	}

	/**
	 * Returns an immutable list containing the given elements, sorted according to their
	 * natural order. The sorting algorithm used is stable, so elements that compare as
	 * equal will stay in the order in which they appear in the input.
	 *
	 * <p>
	 * If your data has no duplicates, or you wish to deduplicate elements, use {@code
	 * ImmutableSortedSet.copyOf(elements)}; if you want a {@code List} you can use its
	 * {@code
	 * asList()} view.
	 *
	 * <p>
	 * <b>Java 8 users:</b> If you want to convert a {@link java.util.stream.Stream} to a
	 * sorted {@code ImmutableList}, use
	 * {@code stream.sorted().collect(toImmutableList())}.
	 * @throws NullPointerException if any element in the input is null
	 * @since 21.0
	 */
	public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(Iterable<? extends E> elements) {
		Comparable<?>[] array = org.magneton.core.collect.Iterables.toArray(elements, new Comparable<?>[0]);
		ObjectArrays.checkElementsNotNull((Object[]) array);
		Arrays.sort(array);
		return asImmutableList(array);
	}

	/**
	 * Returns an immutable list containing the given elements, in sorted order relative
	 * to the specified comparator. The sorting algorithm used is stable, so elements that
	 * compare as equal will stay in the order in which they appear in the input.
	 *
	 * <p>
	 * If your data has no duplicates, or you wish to deduplicate elements, use {@code
	 * ImmutableSortedSet.copyOf(comparator, elements)}; if you want a {@code List} you
	 * can use its {@code asList()} view.
	 *
	 * <p>
	 * <b>Java 8 users:</b> If you want to convert a {@link java.util.stream.Stream} to a
	 * sorted {@code ImmutableList}, use
	 * {@code stream.sorted(comparator).collect(toImmutableList())}.
	 * @throws NullPointerException if any element in the input is null
	 * @since 21.0
	 */
	public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
		Preconditions.checkNotNull(comparator);
		// all supported methods are covariant
		E[] array = (E[]) Iterables.toArray(elements);
		ObjectArrays.checkElementsNotNull(array);
		Arrays.sort(array, comparator);
		return asImmutableList(array);
	}

	/** Views the array as an immutable list. Checks for nulls; does not copy. */
	private static <E> ImmutableList<E> construct(Object... elements) {
		return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
	}

	/**
	 * Views the array as an immutable list. Does not check for nulls; does not copy.
	 *
	 * <p>
	 * The array must be internally created.
	 */
	static <E> ImmutableList<E> asImmutableList(Object[] elements) {
		return asImmutableList(elements, elements.length);
	}

	/**
	 * Views the array as an immutable list. Copies if the specified range does not cover
	 * the complete array. Does not check for nulls.
	 */
	static <E> ImmutableList<E> asImmutableList(@Nullable Object[] elements, int length) {
		switch (length) {
		case 0:
			return of();
		case 1:
			/*
			 * requireNonNull is safe because the callers promise to put non-null objects
			 * in the first `length` array elements.
			 */
			// our callers put only E instances into the
			// array
			E onlyElement = (E) requireNonNull(elements[0]);
			return of(onlyElement);
		default:
			/*
			 * The suppression is safe because the callers promise to put non-null objects
			 * in the first `length` array elements.
			 */
			Object[] elementsWithoutTrailingNulls = length < elements.length ? Arrays.copyOf(elements, length)
					: elements;
			return new RegularImmutableList<E>(elementsWithoutTrailingNulls);
		}
	}

	/**
	 * Returns a new builder. The generated builder is equivalent to the builder created
	 * by the {@link Builder} constructor.
	 */
	public static <E> Builder<E> builder() {
		return new Builder<E>();
	}

	/**
	 * Returns a new builder, expecting the specified number of elements to be added.
	 *
	 * <p>
	 * If {@code expectedSize} is exactly the number of elements added to the builder
	 * before {@link Builder#build} is called, the builder is likely to perform better
	 * than an unsized {@link #builder()} would have.
	 *
	 * <p>
	 * It is not specified if any performance benefits apply if {@code expectedSize} is
	 * close to, but not exactly, the number of elements added to the builder.
	 *
	 * @since 23.1
	 */
	public static <E> Builder<E> builderWithExpectedSize(int expectedSize) {
		CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
		return new Builder<E>(expectedSize);
	}

	// This declaration is needed to make List.iterator() and
	// ImmutableCollection.iterator() consistent.
	@Override
	public UnmodifiableIterator<E> iterator() {
		return listIterator();
	}

	@Override
	public UnmodifiableListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public UnmodifiableListIterator<E> listIterator(int index) {
		return new AbstractIndexedListIterator<E>(size(), index) {
			@Override
			protected E get(int index) {
				return ImmutableList.this.get(index);
			}
		};
	}

	@Override
	public void forEach(Consumer<? super E> consumer) {
		Preconditions.checkNotNull(consumer);
		int n = size();
		for (int i = 0; i < n; i++) {
			consumer.accept(get(i));
		}
	}

	@Override
	public int indexOf(@CheckForNull Object object) {
		return (object == null) ? -1 : Lists.indexOfImpl(this, object);
	}

	// constrain the return type to ImmutableList<E>

	@Override
	public int lastIndexOf(@CheckForNull Object object) {
		return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
	}

	@Override
	public boolean contains(@CheckForNull Object object) {
		return indexOf(object) >= 0;
	}

	/**
	 * Returns an immutable list of the elements between the specified {@code fromIndex},
	 * inclusive, and {@code toIndex}, exclusive. (If {@code fromIndex} and
	 * {@code toIndex} are equal, the empty immutable list is returned.)
	 */
	@Override
	public ImmutableList<E> subList(int fromIndex, int toIndex) {
		Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
		int length = toIndex - fromIndex;
		if (length == size()) {
			return this;
		}
		else if (length == 0) {
			return of();
		}
		else if (length == 1) {
			return of(get(fromIndex));
		}
		else {
			return subListUnchecked(fromIndex, toIndex);
		}
	}

	/**
	 * Called by the default implementation of {@link #subList} when
	 * {@code toIndex - fromIndex > 1}, after index validation has already been performed.
	 */
	ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
		return new SubList(fromIndex, toIndex - fromIndex);
	}

	/**
	 * Guaranteed to throw an exception and leave the list unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final boolean addAll(int index, Collection<? extends E> newElements) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the list unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the list unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the list unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final E remove(int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the list unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the list unmodified.
	 * @throws UnsupportedOperationException always
	 * @deprecated Unsupported operation.
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public final void sort(Comparator<? super E> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns this list instance.
	 *
	 * @since 2.0
	 * @deprecated There is no reason to use this; it always returns {@code this}.
	 */
	@Deprecated
	@Override
	public final ImmutableList<E> asList() {
		return this;
	}

	@Override
	public Spliterator<E> spliterator() {
		return CollectSpliterators.indexed(size(), SPLITERATOR_CHARACTERISTICS, this::get);
	}

	@Override
	int copyIntoArray(@Nullable Object[] dst, int offset) {
		// this loop is faster for RandomAccess instances, which ImmutableLists are
		int size = size();
		for (int i = 0; i < size; i++) {
			dst[offset + i] = get(i);
		}
		return offset + size;
	}

	/**
	 * Returns a view of this immutable list in reverse order. For example,
	 * {@code ImmutableList.of(1,
	 * 2, 3).reverse()} is equivalent to {@code ImmutableList.of(3, 2, 1)}.
	 * @return a view of this immutable list in reverse order
	 * @since 7.0
	 */
	public ImmutableList<E> reverse() {
		return (size() <= 1) ? this : new ReverseImmutableList<E>(this);
	}

	@Override
	public boolean equals(@CheckForNull Object obj) {
		return Lists.equalsImpl(this, obj);
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		int n = size();
		for (int i = 0; i < n; i++) {
			hashCode = 31 * hashCode + get(i).hashCode();

			hashCode = ~~hashCode;
			// needed to deal with GWT integer overflow
		}
		return hashCode;
	}

	private void readObject(ObjectInputStream stream) throws InvalidObjectException {
		throw new InvalidObjectException("Use SerializedForm");
	}

	@Override
	Object writeReplace() {
		return new SerializedForm(toArray());
	}

	private static class ReverseImmutableList<E> extends ImmutableList<E> {

		private final transient ImmutableList<E> forwardList;

		ReverseImmutableList(ImmutableList<E> backingList) {
			forwardList = backingList;
		}

		private int reverseIndex(int index) {
			return (size() - 1) - index;
		}

		private int reversePosition(int index) {
			return size() - index;
		}

		@Override
		public ImmutableList<E> reverse() {
			return forwardList;
		}

		@Override
		public boolean contains(@CheckForNull Object object) {
			return forwardList.contains(object);
		}

		@Override
		public int indexOf(@CheckForNull Object object) {
			int index = forwardList.lastIndexOf(object);
			return (index >= 0) ? reverseIndex(index) : -1;
		}

		@Override
		public int lastIndexOf(@CheckForNull Object object) {
			int index = forwardList.indexOf(object);
			return (index >= 0) ? reverseIndex(index) : -1;
		}

		@Override
		public ImmutableList<E> subList(int fromIndex, int toIndex) {
			Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
			return forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
		}

		@Override
		public E get(int index) {
			Preconditions.checkElementIndex(index, size());
			return forwardList.get(reverseIndex(index));
		}

		@Override
		public int size() {
			return forwardList.size();
		}

		@Override
		boolean isPartialView() {
			return forwardList.isPartialView();
		}

	}

	/*
	 * Serializes ImmutableLists as their logical contents. This ensures that
	 * implementation types do not leak into the serialized representation.
	 */
	static class SerializedForm implements Serializable {

		private static final long serialVersionUID = 0;

		final Object[] elements;

		SerializedForm(Object[] elements) {
			this.elements = elements;
		}

		Object readResolve() {
			return copyOf(elements);
		}

	}

	/**
	 * A builder for creating immutable list instances, especially
	 * {@code public static final} lists ("constant lists"). Example:
	 *
	 * <pre>{@code
	 * public static final ImmutableList<Color> GOOGLE_COLORS
	 *     = new ImmutableList.Builder<Color>()
	 *         .addAll(WEBSAFE_COLORS)
	 *         .add(new Color(0, 191, 255))
	 *         .build();
	 * }</pre>
	 *
	 * <p>
	 * Elements appear in the resulting list in the same order they were added to the
	 * builder.
	 *
	 * <p>
	 * Builder instances can be reused; it is safe to call {@link #build} multiple times
	 * to build multiple lists in series. Each new list contains all the elements of the
	 * ones created before it.
	 *
	 * @since 2.0
	 */
	public static final class Builder<E> extends ImmutableCollection.Builder<E> {

		// The first `size` elements are non-null.
		@VisibleForTesting
		@Nullable
		Object[] contents;

		private int size;

		private boolean forceCopy;

		/**
		 * Creates a new builder. The returned builder is equivalent to the builder
		 * generated by {@link ImmutableList#builder}.
		 */
		public Builder() {
			this(DEFAULT_INITIAL_CAPACITY);
		}

		Builder(int capacity) {
			contents = new Object[capacity];
			size = 0;
		}

		private void getReadyToExpandTo(int minCapacity) {
			if (contents.length < minCapacity) {
				contents = Arrays.copyOf(contents, expandedCapacity(contents.length, minCapacity));
				forceCopy = false;
			}
			else if (forceCopy) {
				contents = Arrays.copyOf(contents, contents.length);
				forceCopy = false;
			}
		}

		/**
		 * Adds {@code element} to the {@code ImmutableList}.
		 * @param element the element to add
		 * @return this {@code Builder} object
		 * @throws NullPointerException if {@code element} is null
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<E> add(E element) {
			Preconditions.checkNotNull(element);
			getReadyToExpandTo(size + 1);
			contents[size++] = element;
			return this;
		}

		/**
		 * Adds each element of {@code elements} to the {@code ImmutableList}.
		 * @param elements the {@code Iterable} to add to the {@code ImmutableList}
		 * @return this {@code Builder} object
		 * @throws NullPointerException if {@code elements} is null or contains a null
		 * element
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<E> add(E... elements) {
			ObjectArrays.checkElementsNotNull(elements);
			add(elements, elements.length);
			return this;
		}

		private void add(@Nullable Object[] elements, int n) {
			getReadyToExpandTo(size + n);
			/*
			 * The following call is not statically checked, since arraycopy accepts plain
			 * Object for its parameters. If it were statically checked, the checker would
			 * still be OK with it, since we're copying into a `contents` array whose type
			 * allows it to contain nulls. Still, it's worth noting that we promise not to
			 * put nulls into the array in the first `size` elements. We uphold that
			 * promise here because our callers promise that `elements` will not contain
			 * nulls in its first `n` elements.
			 */
			System.arraycopy(elements, 0, contents, size, n);
			size += n;
		}

		/**
		 * Adds each element of {@code elements} to the {@code ImmutableList}.
		 * @param elements the {@code Iterable} to add to the {@code ImmutableList}
		 * @return this {@code Builder} object
		 * @throws NullPointerException if {@code elements} is null or contains a null
		 * element
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<E> addAll(Iterable<? extends E> elements) {
			Preconditions.checkNotNull(elements);
			if (elements instanceof Collection) {
				Collection<?> collection = (Collection<?>) elements;
				getReadyToExpandTo(size + collection.size());
				if (collection instanceof ImmutableCollection) {
					ImmutableCollection<?> immutableCollection = (ImmutableCollection<?>) collection;
					size = immutableCollection.copyIntoArray(contents, size);
					return this;
				}
			}
			super.addAll(elements);
			return this;
		}

		/**
		 * Adds each element of {@code elements} to the {@code ImmutableList}.
		 * @param elements the {@code Iterator} to add to the {@code ImmutableList}
		 * @return this {@code Builder} object
		 * @throws NullPointerException if {@code elements} is null or contains a null
		 * element
		 */
		@CanIgnoreReturnValue
		@Override
		public Builder<E> addAll(Iterator<? extends E> elements) {
			super.addAll(elements);
			return this;
		}

		@CanIgnoreReturnValue
		Builder<E> combine(Builder<E> builder) {
			Preconditions.checkNotNull(builder);
			add(builder.contents, builder.size);
			return this;
		}

		/**
		 * Returns a newly-created {@code ImmutableList} based on the contents of the
		 * {@code Builder}.
		 */
		@Override
		public ImmutableList<E> build() {
			forceCopy = true;
			return asImmutableList(contents, size);
		}

	}

	class SubList extends ImmutableList<E> {

		final transient int offset;

		final transient int length;

		SubList(int offset, int length) {
			this.offset = offset;
			this.length = length;
		}

		@Override
		public int size() {
			return length;
		}

		@Override
		public E get(int index) {
			Preconditions.checkElementIndex(index, length);
			return ImmutableList.this.get(index + offset);
		}

		@Override
		public ImmutableList<E> subList(int fromIndex, int toIndex) {
			Preconditions.checkPositionIndexes(fromIndex, toIndex, length);
			return ImmutableList.this.subList(fromIndex + offset, toIndex + offset);
		}

		@Override
		boolean isPartialView() {
			return true;
		}

	}

}