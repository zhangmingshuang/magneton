/*
 * Copyright (C) 2016 The Guava Authors
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

package org.magneton.core.graph;

import java.util.Comparator;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.MoreObjects.ToStringHelper;
import org.magneton.core.base.Objects;
import org.magneton.core.collect.Maps;
import org.magneton.core.collect.Ordering;

import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkState;

/**
 * Used to represent the order of elements in a data structure that supports different
 * options for iteration order guarantees.
 *
 * <p>
 * Example usage:
 *
 * <pre>{@code
 * MutableGraph<Integer> graph =
 *     GraphBuilder.directed().nodeOrder(ElementOrder.<Integer>natural()).build();
 * }</pre>
 *
 * @author Joshua O'Madadhain
 * @since 20.0
 */

@Immutable
@ElementTypesAreNonnullByDefault
public final class ElementOrder<T> {

	private final Type type;

	// Hopefully the comparator provided is immutable!
	@CheckForNull
	private final Comparator<T> comparator;

	private ElementOrder(Type type, @CheckForNull Comparator<T> comparator) {
		this.type = checkNotNull(type);
		this.comparator = comparator;
		checkState((type == Type.SORTED) == (comparator != null));
	}

	/** Returns an instance which specifies that no ordering is guaranteed. */
	public static <S> ElementOrder<S> unordered() {
		return new ElementOrder<>(Type.UNORDERED, null);
	}

	/**
	 * Returns an instance which specifies that ordering is guaranteed to be always be the
	 * same across iterations, and across releases. Some methods may have stronger
	 * guarantees.
	 *
	 * <p>
	 * This instance is only useful in combination with {@code incidentEdgeOrder}, e.g.
	 * {@code
	 * graphBuilder.incidentEdgeOrder(ElementOrder.stable())}.
	 *
	 * <h3>In combination with {@code incidentEdgeOrder}</h3>
	 *
	 * <p>
	 * {@code incidentEdgeOrder(ElementOrder.stable())} guarantees the ordering of the
	 * returned collections of the following methods:
	 *
	 * <ul>
	 * <li>For {@link Graph} and {@link ValueGraph}:
	 * <ul>
	 * <li>{@code edges()}: Stable order
	 * <li>{@code adjacentNodes(node)}: Connecting edge insertion order
	 * <li>{@code predecessors(node)}: Connecting edge insertion order
	 * <li>{@code successors(node)}: Connecting edge insertion order
	 * <li>{@code incidentEdges(node)}: Edge insertion order
	 * </ul>
	 * <li>For {@link Network}:
	 * <ul>
	 * <li>{@code adjacentNodes(node)}: Stable order
	 * <li>{@code predecessors(node)}: Connecting edge insertion order
	 * <li>{@code successors(node)}: Connecting edge insertion order
	 * <li>{@code incidentEdges(node)}: Stable order
	 * <li>{@code inEdges(node)}: Edge insertion order
	 * <li>{@code outEdges(node)}: Edge insertion order
	 * <li>{@code adjacentEdges(edge)}: Stable order
	 * <li>{@code edgesConnecting(nodeU, nodeV)}: Edge insertion order
	 * </ul>
	 * </ul>
	 *
	 * @since 29.0
	 */
	public static <S> ElementOrder<S> stable() {
		return new ElementOrder<>(Type.STABLE, null);
	}

	/** Returns an instance which specifies that insertion ordering is guaranteed. */
	public static <S> ElementOrder<S> insertion() {
		return new ElementOrder<>(Type.INSERTION, null);
	}

	/**
	 * Returns an instance which specifies that the natural ordering of the elements is
	 * guaranteed.
	 */
	public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
		return new ElementOrder<>(Type.SORTED, Ordering.<S>natural());
	}

	/**
	 * Returns an instance which specifies that the ordering of the elements is guaranteed
	 * to be determined by {@code comparator}.
	 */
	public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
		return new ElementOrder<>(Type.SORTED, checkNotNull(comparator));
	}

	/** Returns the type of ordering used. */
	public Type type() {
		return type;
	}

	/**
	 * Returns the {@link Comparator} used.
	 * @throws UnsupportedOperationException if comparator is not defined
	 */
	public Comparator<T> comparator() {
		if (comparator != null) {
			return comparator;
		}
		throw new UnsupportedOperationException("This ordering does not define a comparator.");
	}

	@Override
	public boolean equals(@CheckForNull Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ElementOrder)) {
			return false;
		}

		ElementOrder<?> other = (ElementOrder<?>) obj;
		return (type == other.type) && Objects.equal(comparator, other.comparator);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(type, comparator);
	}

	@Override
	public String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", type);
		if (comparator != null) {
			helper.add("comparator", comparator);
		}
		return helper.toString();
	}

	/** Returns an empty mutable map whose keys will respect this {@link ElementOrder}. */
	<K extends T, V> Map<K, V> createMap(int expectedSize) {
		switch (type) {
		case UNORDERED:
			return org.magneton.core.collect.Maps.newHashMapWithExpectedSize(expectedSize);
		case INSERTION:
		case STABLE:
			return org.magneton.core.collect.Maps.newLinkedHashMapWithExpectedSize(expectedSize);
		case SORTED:
			return Maps.newTreeMap(comparator());
		default:
			throw new AssertionError();
		}
	}

	<T1 extends T> ElementOrder<T1> cast() {
		return (ElementOrder<T1>) this;
	}

	/**
	 * The type of ordering that this object specifies.
	 *
	 * <ul>
	 * <li>UNORDERED: no order is guaranteed.
	 * <li>STABLE: ordering is guaranteed to follow a pattern that won't change between
	 * releases. Some methods may have stronger guarantees.
	 * <li>INSERTION: insertion ordering is guaranteed.
	 * <li>SORTED: ordering according to a supplied comparator is guaranteed.
	 * </ul>
	 */
	public enum Type {

		UNORDERED, STABLE, INSERTION, SORTED

	}

}
