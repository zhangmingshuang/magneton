/*
 * Copyright (C) 2014 The Guava Authors
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

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.CheckForNull;

import org.jetbrains.annotations.Nullable;
import org.magneton.core.base.Preconditions;
import org.magneton.core.math.IntMath;

/**
 * An accumulator that selects the "top" {@code k} elements added to it, relative to a
 * provided comparator. "Top" can mean the greatest or the lowest elements, specified in
 * the factory used to create the {@code TopKSelector} instance.
 *
 * <p>
 * If your input data is available as a {@link Stream}, prefer passing
 * {@link Comparators#least(int)} to {@link Stream#collect(java.util.stream.Collector)}.
 * If it is available as an {@link Iterable} or {@link Iterator}, prefer
 * {@link Ordering#leastOf(Iterable, int)}.
 *
 * <p>
 * This uses the same efficient implementation as {@link Ordering#leastOf(Iterable, int)},
 * offering expected O(n + k log k) performance (worst case O(n log k)) for n calls to
 * {@link #offer} and a call to {@link #topK}, with O(k) memory. In comparison,
 * quickselect has the same asymptotics but requires O(n) memory, and a
 * {@code PriorityQueue} implementation takes O(n log k). In benchmarks, this
 * implementation performs at least as well as either implementation, and degrades more
 * gracefully for worst-case input.
 *
 * <p>
 * The implementation does not necessarily use a <i>stable</i> sorting algorithm; when
 * multiple equivalent elements are added to it, it is undefined which will come first in
 * the output.
 *
 * @author Louis Wasserman
 */

@ElementTypesAreNonnullByDefault
final class TopKSelector<T> {

	private final int k;

	private final Comparator<? super T> comparator;

	/*
	 * We are currently considering the elements in buffer in the range [0, bufferSize) as
	 * candidates for the top k elements. Whenever the buffer is filled, we quickselect
	 * the top k elements to the range [0, k) and ignore the remaining elements.
	 */
	private final @Nullable T[] buffer;

	private int bufferSize;

	/**
	 * The largest of the lowest k elements we've seen so far relative to this comparator.
	 * If bufferSize ≥ k, then we can ignore any elements greater than this value.
	 */
	@CheckForNull
	private T threshold;

	private TopKSelector(Comparator<? super T> comparator, int k) {
		this.comparator = Preconditions.checkNotNull(comparator, "comparator");
		this.k = k;
		Preconditions.checkArgument(k >= 0, "k (%s) must be >= 0", k);
		Preconditions.checkArgument(k <= Integer.MAX_VALUE / 2, "k (%s) must be <= Integer.MAX_VALUE / 2", k);
		this.buffer = (T[]) new Object[org.magneton.core.math.IntMath.checkedMultiply(k, 2)];
		this.bufferSize = 0;
		this.threshold = null;
	}

	/**
	 * Returns a {@code TopKSelector} that collects the lowest {@code k} elements added to
	 * it, relative to the natural ordering of the elements, and returns them via
	 * {@link #topK} in ascending order.
	 * @throws IllegalArgumentException if {@code k < 0} or
	 * {@code k > Integer.MAX_VALUE / 2}
	 */
	public static <T extends Comparable<? super T>> TopKSelector<T> least(int k) {
		return least(k, Ordering.natural());
	}

	/**
	 * Returns a {@code TopKSelector} that collects the lowest {@code k} elements added to
	 * it, relative to the specified comparator, and returns them via {@link #topK} in
	 * ascending order.
	 * @throws IllegalArgumentException if {@code k < 0} or
	 * {@code k > Integer.MAX_VALUE / 2}
	 */
	public static <T> TopKSelector<T> least(int k, Comparator<? super T> comparator) {
		return new TopKSelector<T>(comparator, k);
	}

	/**
	 * Returns a {@code TopKSelector} that collects the greatest {@code k} elements added
	 * to it, relative to the natural ordering of the elements, and returns them via
	 * {@link #topK} in descending order.
	 * @throws IllegalArgumentException if {@code k < 0} or
	 * {@code k > Integer.MAX_VALUE / 2}
	 */
	public static <T extends Comparable<? super T>> TopKSelector<T> greatest(int k) {
		return greatest(k, Ordering.natural());
	}

	/**
	 * Returns a {@code TopKSelector} that collects the greatest {@code k} elements added
	 * to it, relative to the specified comparator, and returns them via {@link #topK} in
	 * descending order.
	 * @throws IllegalArgumentException if {@code k < 0} or
	 * {@code k > Integer.MAX_VALUE / 2}
	 */
	public static <T> TopKSelector<T> greatest(int k, Comparator<? super T> comparator) {
		return new TopKSelector<T>(Ordering.from(comparator).reverse(), k);
	}

	/**
	 * Adds {@code elem} as a candidate for the top {@code k} elements. This operation
	 * takes amortized O(1) time.
	 */
	public void offer(@ParametricNullness T elem) {
		if (this.k == 0) {
			return;
		}
		else if (this.bufferSize == 0) {
			this.buffer[0] = elem;
			this.threshold = elem;
			this.bufferSize = 1;
		}
		else if (this.bufferSize < this.k) {
			this.buffer[this.bufferSize++] = elem;
			// uncheckedCastNullableTToT is safe because bufferSize > 0.
			if (this.comparator.compare(elem, NullnessCasts.uncheckedCastNullableTToT(this.threshold)) > 0) {
				this.threshold = elem;
			}
			// uncheckedCastNullableTToT is safe because bufferSize > 0.
		}
		else if (this.comparator.compare(elem, NullnessCasts.uncheckedCastNullableTToT(this.threshold)) < 0) {
			// Otherwise, we can ignore elem; we've seen k better elements.
			this.buffer[this.bufferSize++] = elem;
			if (this.bufferSize == 2 * this.k) {
				this.trim();
			}
		}
	}

	/**
	 * Quickselects the top k elements from the 2k elements in the buffer. O(k) expected
	 * time, O(k log k) worst case.
	 */
	private void trim() {
		int left = 0;
		int right = 2 * this.k - 1;

		int minThresholdPosition = 0;
		// The leftmost position at which the greatest of the k lower elements
		// -- the new value of threshold -- might be found.

		int iterations = 0;
		int maxIterations = IntMath.log2(right - left, RoundingMode.CEILING) * 3;
		while (left < right) {
			int pivotIndex = (left + right + 1) >>> 1;

			int pivotNewIndex = this.partition(left, right, pivotIndex);

			if (pivotNewIndex > this.k) {
				right = pivotNewIndex - 1;
			}
			else if (pivotNewIndex < this.k) {
				left = Math.max(pivotNewIndex, left + 1);
				minThresholdPosition = pivotNewIndex;
			}
			else {
				break;
			}
			iterations++;
			if (iterations >= maxIterations) {
				// We've already taken O(k log k), let's make sure we don't take longer
				// than O(k log k).
				Arrays.sort(this.buffer, left, right + 1, this.comparator);
				break;
			}
		}
		this.bufferSize = this.k;

		this.threshold = NullnessCasts.uncheckedCastNullableTToT(this.buffer[minThresholdPosition]);
		for (int i = minThresholdPosition + 1; i < this.k; i++) {
			if (this.comparator.compare(NullnessCasts.uncheckedCastNullableTToT(this.buffer[i]),
					NullnessCasts.uncheckedCastNullableTToT(this.threshold)) > 0) {
				this.threshold = this.buffer[i];
			}
		}
	}

	/**
	 * Partitions the contents of buffer in the range [left, right] around the pivot
	 * element previously stored in buffer[pivotValue]. Returns the new index of the pivot
	 * element, pivotNewIndex, so that everything in [left, pivotNewIndex] is ≤ pivotValue
	 * and everything in (pivotNewIndex, right] is greater than pivotValue.
	 */
	private int partition(int left, int right, int pivotIndex) {
		T pivotValue = NullnessCasts.uncheckedCastNullableTToT(this.buffer[pivotIndex]);
		this.buffer[pivotIndex] = this.buffer[right];

		int pivotNewIndex = left;
		for (int i = left; i < right; i++) {
			if (this.comparator.compare(NullnessCasts.uncheckedCastNullableTToT(this.buffer[i]), pivotValue) < 0) {
				this.swap(pivotNewIndex, i);
				pivotNewIndex++;
			}
		}
		this.buffer[right] = this.buffer[pivotNewIndex];
		this.buffer[pivotNewIndex] = pivotValue;
		return pivotNewIndex;
	}

	private void swap(int i, int j) {
		T tmp = this.buffer[i];
		this.buffer[i] = this.buffer[j];
		this.buffer[j] = tmp;
	}

	TopKSelector<T> combine(TopKSelector<T> other) {
		for (int i = 0; i < other.bufferSize; i++) {
			this.offer(NullnessCasts.uncheckedCastNullableTToT(other.buffer[i]));
		}
		return this;
	}

	/**
	 * Adds each member of {@code elements} as a candidate for the top {@code k} elements.
	 * This operation takes amortized linear time in the length of {@code elements}.
	 *
	 * <p>
	 * If all input data to this {@code TopKSelector} is in a single {@code Iterable},
	 * prefer {@link Ordering#leastOf(Iterable, int)}, which provides a simpler API for
	 * that use case.
	 */
	public void offerAll(Iterable<? extends T> elements) {
		this.offerAll(elements.iterator());
	}

	/**
	 * Adds each member of {@code elements} as a candidate for the top {@code k} elements.
	 * This operation takes amortized linear time in the length of {@code elements}. The
	 * iterator is consumed after this operation completes.
	 *
	 * <p>
	 * If all input data to this {@code TopKSelector} is in a single {@code Iterator},
	 * prefer {@link Ordering#leastOf(Iterator, int)}, which provides a simpler API for
	 * that use case.
	 */
	public void offerAll(Iterator<? extends T> elements) {
		while (elements.hasNext()) {
			this.offer(elements.next());
		}
	}

	/**
	 * Returns the top {@code k} elements offered to this {@code TopKSelector}, or all
	 * elements if fewer than {@code k} have been offered, in the order specified by the
	 * factory used to create this {@code TopKSelector}.
	 *
	 * <p>
	 * The returned list is an unmodifiable copy and will not be affected by further
	 * changes to this {@code TopKSelector}. This method returns in O(k log k) time.
	 */
	public List<T> topK() {
		Arrays.sort(this.buffer, 0, this.bufferSize, this.comparator);
		if (this.bufferSize > this.k) {
			Arrays.fill(this.buffer, this.k, this.buffer.length, null);
			this.bufferSize = this.k;
			this.threshold = this.buffer[this.k - 1];
		}
		// we have to support null elements, so no ImmutableList for us
		return Collections.unmodifiableList(Arrays.asList(Arrays.copyOf(this.buffer, this.bufferSize)));
	}

}
