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

import javax.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.base.Preconditions.checkArgument;
import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkState;
import static org.magneton.core.graph.GraphConstants.SELF_LOOPS_NOT_ALLOWED;

/**
 * Standard implementation of {@link org.magneton.core.graph.MutableValueGraph} that
 * supports both directed and undirected graphs. Instances of this class should be
 * constructed with {@link ValueGraphBuilder}.
 *
 * <p>
 * Time complexities for mutation methods are all O(1) except for
 * {@code removeNode(N node)}, which is in O(d_node) where d_node is the degree of
 * {@code node}.
 *
 * @author James Sexton
 * @author Joshua O'Madadhain
 * @author Omar Darwish
 * @param <N> Node parameter type
 * @param <V> Value parameter type
 */
@ElementTypesAreNonnullByDefault
final class StandardMutableValueGraph<N, V> extends org.magneton.core.graph.StandardValueGraph<N, V>
		implements MutableValueGraph<N, V> {

	private final org.magneton.core.graph.ElementOrder<N> incidentEdgeOrder;

	/** Constructs a mutable graph with the properties specified in {@code builder}. */
	StandardMutableValueGraph(org.magneton.core.graph.AbstractGraphBuilder<? super N> builder) {
		super(builder);
		incidentEdgeOrder = builder.incidentEdgeOrder.cast();
	}

	@Override
	public ElementOrder<N> incidentEdgeOrder() {
		return incidentEdgeOrder;
	}

	@Override
	@CanIgnoreReturnValue
	public boolean addNode(N node) {
		checkNotNull(node, "node");

		if (containsNode(node)) {
			return false;
		}

		addNodeInternal(node);
		return true;
	}

	/**
	 * Adds {@code node} to the graph and returns the associated
	 * {@link org.magneton.core.graph.GraphConnections}.
	 * @throws IllegalStateException if {@code node} is already present
	 */
	@CanIgnoreReturnValue
	private org.magneton.core.graph.GraphConnections<N, V> addNodeInternal(N node) {
		org.magneton.core.graph.GraphConnections<N, V> connections = newConnections();
		checkState(nodeConnections.put(node, connections) == null);
		return connections;
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public V putEdgeValue(N nodeU, N nodeV, V value) {
		checkNotNull(nodeU, "nodeU");
		checkNotNull(nodeV, "nodeV");
		checkNotNull(value, "value");

		if (!allowsSelfLoops()) {
			checkArgument(!nodeU.equals(nodeV), SELF_LOOPS_NOT_ALLOWED, nodeU);
		}

		org.magneton.core.graph.GraphConnections<N, V> connectionsU = nodeConnections.get(nodeU);
		if (connectionsU == null) {
			connectionsU = addNodeInternal(nodeU);
		}
		V previousValue = connectionsU.addSuccessor(nodeV, value);
		org.magneton.core.graph.GraphConnections<N, V> connectionsV = nodeConnections.get(nodeV);
		if (connectionsV == null) {
			connectionsV = addNodeInternal(nodeV);
		}
		connectionsV.addPredecessor(nodeU, value);
		if (previousValue == null) {
			Graphs.checkPositive(++edgeCount);
		}
		return previousValue;
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public V putEdgeValue(org.magneton.core.graph.EndpointPair<N> endpoints, V value) {
		validateEndpoints(endpoints);
		return putEdgeValue(endpoints.nodeU(), endpoints.nodeV(), value);
	}

	@Override
	@CanIgnoreReturnValue
	public boolean removeNode(N node) {
		checkNotNull(node, "node");

		org.magneton.core.graph.GraphConnections<N, V> connections = nodeConnections.get(node);
		if (connections == null) {
			return false;
		}

		if (allowsSelfLoops()) {
			// Remove self-loop (if any) first, so we don't get CME while removing
			// incident edges.
			if (connections.removeSuccessor(node) != null) {
				connections.removePredecessor(node);
				--edgeCount;
			}
		}

		for (N successor : connections.successors()) {
			// requireNonNull is safe because the node is a successor.
			requireNonNull(nodeConnections.getWithoutCaching(successor)).removePredecessor(node);
			--edgeCount;
		}
		if (isDirected()) { // In undirected graphs, the successor and predecessor sets
							// are equal.
			for (N predecessor : connections.predecessors()) {
				// requireNonNull is safe because the node is a predecessor.
				checkState(
						requireNonNull(nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null);
				--edgeCount;
			}
		}
		nodeConnections.remove(node);
		Graphs.checkNonNegative(edgeCount);
		return true;
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public V removeEdge(N nodeU, N nodeV) {
		checkNotNull(nodeU, "nodeU");
		checkNotNull(nodeV, "nodeV");

		org.magneton.core.graph.GraphConnections<N, V> connectionsU = nodeConnections.get(nodeU);
		org.magneton.core.graph.GraphConnections<N, V> connectionsV = nodeConnections.get(nodeV);
		if (connectionsU == null || connectionsV == null) {
			return null;
		}

		V previousValue = connectionsU.removeSuccessor(nodeV);
		if (previousValue != null) {
			connectionsV.removePredecessor(nodeU);
			Graphs.checkNonNegative(--edgeCount);
		}
		return previousValue;
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public V removeEdge(EndpointPair<N> endpoints) {
		validateEndpoints(endpoints);
		return removeEdge(endpoints.nodeU(), endpoints.nodeV());
	}

	private org.magneton.core.graph.GraphConnections<N, V> newConnections() {
		return isDirected() ? org.magneton.core.graph.DirectedGraphConnections.<N, V>of(incidentEdgeOrder)
				: org.magneton.core.graph.UndirectedGraphConnections.<N, V>of(incidentEdgeOrder);
	}

}
