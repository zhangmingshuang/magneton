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

import javax.annotation.CanIgnoreReturnValue;

import org.magneton.core.collect.ImmutableList;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.base.Preconditions.checkArgument;
import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.base.Preconditions.checkState;
import static org.magneton.core.graph.GraphConstants.PARALLEL_EDGES_NOT_ALLOWED;
import static org.magneton.core.graph.GraphConstants.REUSING_EDGE;
import static org.magneton.core.graph.GraphConstants.SELF_LOOPS_NOT_ALLOWED;

/**
 * Standard implementation of {@link org.magneton.core.graph.MutableNetwork} that supports
 * both directed and undirected graphs. Instances of this class should be constructed with
 * {@link org.magneton.core.graph.NetworkBuilder}.
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
 * @param <E> Edge parameter type
 */
@ElementTypesAreNonnullByDefault
final class StandardMutableNetwork<N, E> extends org.magneton.core.graph.StandardNetwork<N, E>
		implements MutableNetwork<N, E> {

	/** Constructs a mutable graph with the properties specified in {@code builder}. */
	StandardMutableNetwork(NetworkBuilder<? super N, ? super E> builder) {
		super(builder);
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
	 * {@link org.magneton.core.graph.NetworkConnections}.
	 * @throws IllegalStateException if {@code node} is already present
	 */
	@CanIgnoreReturnValue
	private org.magneton.core.graph.NetworkConnections<N, E> addNodeInternal(N node) {
		org.magneton.core.graph.NetworkConnections<N, E> connections = newConnections();
		checkState(nodeConnections.put(node, connections) == null);
		return connections;
	}

	@Override
	@CanIgnoreReturnValue
	public boolean addEdge(N nodeU, N nodeV, E edge) {
		checkNotNull(nodeU, "nodeU");
		checkNotNull(nodeV, "nodeV");
		checkNotNull(edge, "edge");

		if (containsEdge(edge)) {
			org.magneton.core.graph.EndpointPair<N> existingIncidentNodes = incidentNodes(edge);
			org.magneton.core.graph.EndpointPair<N> newIncidentNodes = org.magneton.core.graph.EndpointPair.of(this,
					nodeU, nodeV);
			checkArgument(existingIncidentNodes.equals(newIncidentNodes), REUSING_EDGE, edge, existingIncidentNodes,
					newIncidentNodes);
			return false;
		}
		org.magneton.core.graph.NetworkConnections<N, E> connectionsU = nodeConnections.get(nodeU);
		if (!allowsParallelEdges()) {
			checkArgument(!(connectionsU != null && connectionsU.successors().contains(nodeV)),
					PARALLEL_EDGES_NOT_ALLOWED, nodeU, nodeV);
		}
		boolean isSelfLoop = nodeU.equals(nodeV);
		if (!allowsSelfLoops()) {
			checkArgument(!isSelfLoop, SELF_LOOPS_NOT_ALLOWED, nodeU);
		}

		if (connectionsU == null) {
			connectionsU = addNodeInternal(nodeU);
		}
		connectionsU.addOutEdge(edge, nodeV);
		org.magneton.core.graph.NetworkConnections<N, E> connectionsV = nodeConnections.get(nodeV);
		if (connectionsV == null) {
			connectionsV = addNodeInternal(nodeV);
		}
		connectionsV.addInEdge(edge, nodeU, isSelfLoop);
		edgeToReferenceNode.put(edge, nodeU);
		return true;
	}

	@Override
	@CanIgnoreReturnValue
	public boolean addEdge(EndpointPair<N> endpoints, E edge) {
		validateEndpoints(endpoints);
		return addEdge(endpoints.nodeU(), endpoints.nodeV(), edge);
	}

	@Override
	@CanIgnoreReturnValue
	public boolean removeNode(N node) {
		checkNotNull(node, "node");

		org.magneton.core.graph.NetworkConnections<N, E> connections = nodeConnections.get(node);
		if (connections == null) {
			return false;
		}

		// Since views are returned, we need to copy the edges that will be removed.
		// Thus we avoid modifying the underlying view while iterating over it.
		for (E edge : ImmutableList.copyOf(connections.incidentEdges())) {
			removeEdge(edge);
		}
		nodeConnections.remove(node);
		return true;
	}

	@Override
	@CanIgnoreReturnValue
	public boolean removeEdge(E edge) {
		checkNotNull(edge, "edge");

		N nodeU = edgeToReferenceNode.get(edge);
		if (nodeU == null) {
			return false;
		}

		// requireNonNull is safe because of the edgeToReferenceNode check above.
		org.magneton.core.graph.NetworkConnections<N, E> connectionsU = requireNonNull(nodeConnections.get(nodeU));
		N nodeV = connectionsU.adjacentNode(edge);
		org.magneton.core.graph.NetworkConnections<N, E> connectionsV = requireNonNull(nodeConnections.get(nodeV));
		connectionsU.removeOutEdge(edge);
		connectionsV.removeInEdge(edge, allowsSelfLoops() && nodeU.equals(nodeV));
		edgeToReferenceNode.remove(edge);
		return true;
	}

	private org.magneton.core.graph.NetworkConnections<N, E> newConnections() {
		return isDirected()
				? allowsParallelEdges() ? org.magneton.core.graph.DirectedMultiNetworkConnections.<N, E>of()
						: org.magneton.core.graph.DirectedNetworkConnections.<N, E>of()
				: allowsParallelEdges() ? org.magneton.core.graph.UndirectedMultiNetworkConnections.<N, E>of()
						: org.magneton.core.graph.UndirectedNetworkConnections.<N, E>of();
	}

}
