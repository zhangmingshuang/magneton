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

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.magneton.core.collect.Iterables;
import org.magneton.core.collect.Iterators;
import org.magneton.core.collect.Sets;
import org.magneton.core.collect.UnmodifiableIterator;
import org.magneton.core.math.IntMath;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkState;

/**
 * A base implementation of {@link org.magneton.core.graph.NetworkConnections} for
 * directed networks.
 *
 * @author James Sexton
 * @param <N> Node parameter type
 * @param <E> Edge parameter type
 */
@ElementTypesAreNonnullByDefault
abstract class AbstractDirectedNetworkConnections<N, E> implements org.magneton.core.graph.NetworkConnections<N, E> {

	/** Keys are edges incoming to the origin node, values are the source node. */
	final Map<E, N> inEdgeMap;

	/** Keys are edges outgoing from the origin node, values are the target node. */
	final Map<E, N> outEdgeMap;

	private int selfLoopCount;

	AbstractDirectedNetworkConnections(Map<E, N> inEdgeMap, Map<E, N> outEdgeMap, int selfLoopCount) {
		this.inEdgeMap = checkNotNull(inEdgeMap);
		this.outEdgeMap = checkNotNull(outEdgeMap);
		this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
		checkState(selfLoopCount <= inEdgeMap.size() && selfLoopCount <= outEdgeMap.size());
	}

	@Override
	public Set<N> adjacentNodes() {
		return org.magneton.core.collect.Sets.union(predecessors(), successors());
	}

	@Override
	public Set<E> incidentEdges() {
		return new AbstractSet<E>() {
			@Override
			public UnmodifiableIterator<E> iterator() {
				Iterable<E> incidentEdges = (selfLoopCount == 0)
						? Iterables.concat(inEdgeMap.keySet(), outEdgeMap.keySet())
						: Sets.union(inEdgeMap.keySet(), outEdgeMap.keySet());
				return Iterators.unmodifiableIterator(incidentEdges.iterator());
			}

			@Override
			public int size() {
				return IntMath.saturatedAdd(inEdgeMap.size(), outEdgeMap.size() - selfLoopCount);
			}

			@Override
			public boolean contains(@CheckForNull Object obj) {
				return inEdgeMap.containsKey(obj) || outEdgeMap.containsKey(obj);
			}
		};
	}

	@Override
	public Set<E> inEdges() {
		return Collections.unmodifiableSet(inEdgeMap.keySet());
	}

	@Override
	public Set<E> outEdges() {
		return Collections.unmodifiableSet(outEdgeMap.keySet());
	}

	@Override
	public N adjacentNode(E edge) {
		// Since the reference node is defined to be 'source' for directed graphs,
		// we can assume this edge lives in the set of outgoing edges.
		// (We're relying on callers to call this method only with an edge that's in the
		// graph.)
		return requireNonNull(outEdgeMap.get(edge));
	}

	@Override
	public N removeInEdge(E edge, boolean isSelfLoop) {
		if (isSelfLoop) {
			Graphs.checkNonNegative(--selfLoopCount);
		}
		N previousNode = inEdgeMap.remove(edge);
		// We're relying on callers to call this method only with an edge that's in the
		// graph.
		return requireNonNull(previousNode);
	}

	@Override
	public N removeOutEdge(E edge) {
		N previousNode = outEdgeMap.remove(edge);
		// We're relying on callers to call this method only with an edge that's in the
		// graph.
		return requireNonNull(previousNode);
	}

	@Override
	public void addInEdge(E edge, N node, boolean isSelfLoop) {
		checkNotNull(edge);
		checkNotNull(node);

		if (isSelfLoop) {
			Graphs.checkPositive(++selfLoopCount);
		}
		N previousNode = inEdgeMap.put(edge, node);
		checkState(previousNode == null);
	}

	@Override
	public void addOutEdge(E edge, N node) {
		checkNotNull(edge);
		checkNotNull(node);

		N previousNode = outEdgeMap.put(edge, node);
		checkState(previousNode == null);
	}

}
