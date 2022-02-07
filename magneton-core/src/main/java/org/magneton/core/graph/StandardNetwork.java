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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.ImmutableSet;

import static java.util.Objects.requireNonNull;
import static org.magneton.core.base.Preconditions.checkNotNull;
import static org.magneton.core.graph.GraphConstants.DEFAULT_EDGE_COUNT;
import static org.magneton.core.graph.GraphConstants.DEFAULT_NODE_COUNT;
import static org.magneton.core.graph.GraphConstants.EDGE_NOT_IN_GRAPH;
import static org.magneton.core.graph.GraphConstants.NODE_NOT_IN_GRAPH;

/**
 * Standard implementation of {@link Network} that supports the options supplied by
 * {@link org.magneton.core.graph.NetworkBuilder}.
 *
 * <p>
 * This class maintains a map of nodes to
 * {@link org.magneton.core.graph.NetworkConnections}. This class also maintains a map of
 * edges to reference nodes. The reference node is defined to be the edge's source node on
 * directed graphs, and an arbitrary endpoint of the edge on undirected graphs.
 *
 * <p>
 * Collection-returning accessors return unmodifiable views: the view returned will
 * reflect changes to the graph (if the graph is mutable) but may not be modified by the
 * user.
 *
 * <p>
 * The time complexity of all collection-returning accessors is O(1), since views are
 * returned.
 *
 * @author James Sexton
 * @author Joshua O'Madadhain
 * @author Omar Darwish
 * @param <N> Node parameter type
 * @param <E> Edge parameter type
 */
@ElementTypesAreNonnullByDefault
class StandardNetwork<N, E> extends AbstractNetwork<N, E> {

	final org.magneton.core.graph.MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;

	// We could make this a Map<E, EndpointPair<N>>. It would make incidentNodes(edge)
	// slightly
	// faster, but also make Networks consume 5 to 20+% (increasing with average degree)
	// more memory.
	final org.magneton.core.graph.MapIteratorCache<E, N> edgeToReferenceNode; // referenceNode

	private final boolean isDirected;

	private final boolean allowsParallelEdges;

	private final boolean allowsSelfLoops;

	private final org.magneton.core.graph.ElementOrder<N> nodeOrder;

	private final org.magneton.core.graph.ElementOrder<E> edgeOrder;

	// == source
	// if directed

	/** Constructs a graph with the properties specified in {@code builder}. */
	StandardNetwork(org.magneton.core.graph.NetworkBuilder<? super N, ? super E> builder) {
		this(builder,
				builder.nodeOrder.<N, org.magneton.core.graph.NetworkConnections<N, E>>createMap(
						builder.expectedNodeCount.or(DEFAULT_NODE_COUNT)),
				builder.edgeOrder.<E, N>createMap(builder.expectedEdgeCount.or(DEFAULT_EDGE_COUNT)));
	}

	/**
	 * Constructs a graph with the properties specified in {@code builder}, initialized
	 * with the given node and edge maps.
	 */
	StandardNetwork(NetworkBuilder<? super N, ? super E> builder,
			Map<N, org.magneton.core.graph.NetworkConnections<N, E>> nodeConnections, Map<E, N> edgeToReferenceNode) {
		isDirected = builder.directed;
		allowsParallelEdges = builder.allowsParallelEdges;
		allowsSelfLoops = builder.allowsSelfLoops;
		nodeOrder = builder.nodeOrder.cast();
		edgeOrder = builder.edgeOrder.cast();
		// Prefer the heavier "MapRetrievalCache" for nodes if lookup is expensive. This
		// optimizes
		// methods that access the same node(s) repeatedly, such as
		// Graphs.removeEdgesConnecting().
		this.nodeConnections = (nodeConnections instanceof TreeMap)
				? new MapRetrievalCache<N, org.magneton.core.graph.NetworkConnections<N, E>>(nodeConnections)
				: new org.magneton.core.graph.MapIteratorCache<N, NetworkConnections<N, E>>(nodeConnections);
		this.edgeToReferenceNode = new org.magneton.core.graph.MapIteratorCache<>(edgeToReferenceNode);
	}

	@Override
	public Set<N> nodes() {
		return nodeConnections.unmodifiableKeySet();
	}

	@Override
	public Set<E> edges() {
		return edgeToReferenceNode.unmodifiableKeySet();
	}

	@Override
	public boolean isDirected() {
		return isDirected;
	}

	@Override
	public boolean allowsParallelEdges() {
		return allowsParallelEdges;
	}

	@Override
	public boolean allowsSelfLoops() {
		return allowsSelfLoops;
	}

	@Override
	public org.magneton.core.graph.ElementOrder<N> nodeOrder() {
		return nodeOrder;
	}

	@Override
	public ElementOrder<E> edgeOrder() {
		return edgeOrder;
	}

	@Override
	public Set<E> incidentEdges(N node) {
		return checkedConnections(node).incidentEdges();
	}

	@Override
	public org.magneton.core.graph.EndpointPair<N> incidentNodes(E edge) {
		N nodeU = checkedReferenceNode(edge);
		// requireNonNull is safe because checkedReferenceNode made sure the edge is in
		// the network.
		N nodeV = requireNonNull(nodeConnections.get(nodeU)).adjacentNode(edge);
		return EndpointPair.of(this, nodeU, nodeV);
	}

	@Override
	public Set<N> adjacentNodes(N node) {
		return checkedConnections(node).adjacentNodes();
	}

	@Override
	public Set<E> edgesConnecting(N nodeU, N nodeV) {
		org.magneton.core.graph.NetworkConnections<N, E> connectionsU = checkedConnections(nodeU);
		if (!allowsSelfLoops && nodeU == nodeV) { // just an optimization, only check
													// reference equality
			return ImmutableSet.of();
		}
		Preconditions.checkArgument(containsNode(nodeV), NODE_NOT_IN_GRAPH, nodeV);
		return connectionsU.edgesConnecting(nodeV);
	}

	@Override
	public Set<E> inEdges(N node) {
		return checkedConnections(node).inEdges();
	}

	@Override
	public Set<E> outEdges(N node) {
		return checkedConnections(node).outEdges();
	}

	@Override
	public Set<N> predecessors(N node) {
		return checkedConnections(node).predecessors();
	}

	@Override
	public Set<N> successors(N node) {
		return checkedConnections(node).successors();
	}

	final org.magneton.core.graph.NetworkConnections<N, E> checkedConnections(N node) {
		org.magneton.core.graph.NetworkConnections<N, E> connections = nodeConnections.get(node);
		if (connections == null) {
			checkNotNull(node);
			throw new IllegalArgumentException(String.format(NODE_NOT_IN_GRAPH, node));
		}
		return connections;
	}

	final N checkedReferenceNode(E edge) {
		N referenceNode = edgeToReferenceNode.get(edge);
		if (referenceNode == null) {
			checkNotNull(edge);
			throw new IllegalArgumentException(String.format(EDGE_NOT_IN_GRAPH, edge));
		}
		return referenceNode;
	}

	final boolean containsNode(N node) {
		return nodeConnections.containsKey(node);
	}

	final boolean containsEdge(E edge) {
		return edgeToReferenceNode.containsKey(edge);
	}

}
