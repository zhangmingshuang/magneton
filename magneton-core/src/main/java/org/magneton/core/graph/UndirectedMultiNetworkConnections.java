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

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;

import com.google.errorprone.annotations.concurrent.LazyInit;
import org.magneton.core.collect.HashMultiset;
import org.magneton.core.collect.ImmutableMap;
import org.magneton.core.collect.Multiset;

import static org.magneton.core.base.Preconditions.checkState;
import static org.magneton.core.graph.GraphConstants.INNER_CAPACITY;
import static org.magneton.core.graph.GraphConstants.INNER_LOAD_FACTOR;

/**
 * An implementation of {@link org.magneton.core.graph.NetworkConnections} for undirected
 * networks with parallel edges.
 *
 * @author James Sexton
 * @param <N> Node parameter type
 * @param <E> Edge parameter type
 */
@ElementTypesAreNonnullByDefault
final class UndirectedMultiNetworkConnections<N, E>
		extends org.magneton.core.graph.AbstractUndirectedNetworkConnections<N, E> {

	@CheckForNull
	@LazyInit
	private transient Reference<org.magneton.core.collect.Multiset<N>> adjacentNodesReference;

	private UndirectedMultiNetworkConnections(Map<E, N> incidentEdges) {
		super(incidentEdges);
	}

	static <N, E> UndirectedMultiNetworkConnections<N, E> of() {
		return new UndirectedMultiNetworkConnections<>(new HashMap<E, N>(INNER_CAPACITY, INNER_LOAD_FACTOR));
	}

	static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> incidentEdges) {
		return new UndirectedMultiNetworkConnections<>(ImmutableMap.copyOf(incidentEdges));
	}

	@CheckForNull
	private static <T> T getReference(@CheckForNull Reference<T> reference) {
		return (reference == null) ? null : reference.get();
	}

	@Override
	public Set<N> adjacentNodes() {
		return Collections.unmodifiableSet(adjacentNodesMultiset().elementSet());
	}

	private org.magneton.core.collect.Multiset<N> adjacentNodesMultiset() {
		org.magneton.core.collect.Multiset<N> adjacentNodes = getReference(adjacentNodesReference);
		if (adjacentNodes == null) {
			adjacentNodes = HashMultiset.create(incidentEdgeMap.values());
			adjacentNodesReference = new SoftReference<>(adjacentNodes);
		}
		return adjacentNodes;
	}

	@Override
	public Set<E> edgesConnecting(N node) {
		return new MultiEdgesConnecting<E>(incidentEdgeMap, node) {
			@Override
			public int size() {
				return adjacentNodesMultiset().count(node);
			}
		};
	}

	@Override
	@CheckForNull
	public N removeInEdge(E edge, boolean isSelfLoop) {
		if (!isSelfLoop) {
			return removeOutEdge(edge);
		}
		return null;
	}

	@Override
	public N removeOutEdge(E edge) {
		N node = super.removeOutEdge(edge);
		org.magneton.core.collect.Multiset<N> adjacentNodes = getReference(adjacentNodesReference);
		if (adjacentNodes != null) {
			checkState(adjacentNodes.remove(node));
		}
		return node;
	}

	@Override
	public void addInEdge(E edge, N node, boolean isSelfLoop) {
		if (!isSelfLoop) {
			addOutEdge(edge, node);
		}
	}

	@Override
	public void addOutEdge(E edge, N node) {
		super.addOutEdge(edge, node);
		Multiset<N> adjacentNodes = getReference(adjacentNodesReference);
		if (adjacentNodes != null) {
			checkState(adjacentNodes.add(node));
		}
	}

}
