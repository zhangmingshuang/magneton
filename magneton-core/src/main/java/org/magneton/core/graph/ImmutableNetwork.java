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

package org.magneton.core.graph;

import java.util.Map;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotations.Immutable;

import org.magneton.core.base.Function;
import org.magneton.core.collect.ImmutableMap;
import org.magneton.core.collect.Maps;

import static org.magneton.core.base.Preconditions.checkNotNull;

/**
 * A {@link org.magneton.core.graph.Network} whose elements and structural relationships
 * will never change. Instances of this class may be obtained with
 * {@link #copyOf(org.magneton.core.graph.Network)}.
 *
 * <p>
 * See the Guava User's Guide's <a href=
 * "https://github.com/google/guava/wiki/GraphsExplained#immutable-implementations">discussion
 * of the {@code Immutable*} types</a> for more information on the properties and
 * guarantees provided by this class.
 *
 * @author James Sexton
 * @author Joshua O'Madadhain
 * @author Omar Darwish
 * @author Jens Nyman
 * @param <N> Node parameter type
 * @param <E> Edge parameter type
 * @since 20.0
 */

@Immutable(containerOf = { "N", "E" })
// Extends StandardNetwork but uses ImmutableMaps.
@ElementTypesAreNonnullByDefault
public final class ImmutableNetwork<N, E> extends StandardNetwork<N, E> {

	private ImmutableNetwork(org.magneton.core.graph.Network<N, E> network) {
		super(org.magneton.core.graph.NetworkBuilder.from(network), getNodeConnections(network),
				getEdgeToReferenceNode(network));
	}

	/** Returns an immutable copy of {@code network}. */
	public static <N, E> ImmutableNetwork<N, E> copyOf(org.magneton.core.graph.Network<N, E> network) {
		return (network instanceof ImmutableNetwork) ? (ImmutableNetwork<N, E>) network
				: new ImmutableNetwork<N, E>(network);
	}

	/**
	 * Simply returns its argument.
	 * @deprecated no need to use this
	 */
	@Deprecated
	public static <N, E> ImmutableNetwork<N, E> copyOf(ImmutableNetwork<N, E> network) {
		return checkNotNull(network);
	}

	private static <N, E> Map<N, org.magneton.core.graph.NetworkConnections<N, E>> getNodeConnections(
			org.magneton.core.graph.Network<N, E> network) {
		// ImmutableMap.Builder maintains the order of the elements as inserted, so the
		// map will have
		// whatever ordering the network's nodes do, so ImmutableSortedMap is unnecessary
		// even if the
		// input nodes are sorted.
		ImmutableMap.Builder<N, org.magneton.core.graph.NetworkConnections<N, E>> nodeConnections = ImmutableMap
				.builder();
		for (N node : network.nodes()) {
			nodeConnections.put(node, connectionsOf(network, node));
		}
		return nodeConnections.build();
	}

	private static <N, E> Map<E, N> getEdgeToReferenceNode(org.magneton.core.graph.Network<N, E> network) {
		// ImmutableMap.Builder maintains the order of the elements as inserted, so the
		// map will have
		// whatever ordering the network's edges do, so ImmutableSortedMap is unnecessary
		// even if the
		// input edges are sorted.
		ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.builder();
		for (E edge : network.edges()) {
			edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
		}
		return edgeToReferenceNode.build();
	}

	private static <N, E> org.magneton.core.graph.NetworkConnections<N, E> connectionsOf(
			org.magneton.core.graph.Network<N, E> network, N node) {
		if (network.isDirected()) {
			Map<E, N> inEdgeMap = org.magneton.core.collect.Maps.asMap(network.inEdges(node), sourceNodeFn(network));
			Map<E, N> outEdgeMap = org.magneton.core.collect.Maps.asMap(network.outEdges(node), targetNodeFn(network));
			int selfLoopCount = network.edgesConnecting(node, node).size();
			return network.allowsParallelEdges()
					? DirectedMultiNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount)
					: DirectedNetworkConnections.ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount);
		}
		else {
			Map<E, N> incidentEdgeMap = Maps.asMap(network.incidentEdges(node), adjacentNodeFn(network, node));
			return network.allowsParallelEdges() ? UndirectedMultiNetworkConnections.ofImmutable(incidentEdgeMap)
					: UndirectedNetworkConnections.ofImmutable(incidentEdgeMap);
		}
	}

	private static <N, E> Function<E, N> sourceNodeFn(org.magneton.core.graph.Network<N, E> network) {
		return (E edge) -> network.incidentNodes(edge).source();
	}

	private static <N, E> Function<E, N> targetNodeFn(org.magneton.core.graph.Network<N, E> network) {
		return (E edge) -> network.incidentNodes(edge).target();
	}

	private static <N, E> Function<E, N> adjacentNodeFn(Network<N, E> network, N node) {
		return (E edge) -> network.incidentNodes(edge).adjacentNode(node);
	}

	@Override
	public org.magneton.core.graph.ImmutableGraph<N> asGraph() {
		return new ImmutableGraph<>(super.asGraph()); // safe because the view is
														// effectively immutable
	}

	/**
	 * A builder for creating {@link ImmutableNetwork} instances, especially
	 * {@code static final} networks. Example:
	 *
	 * <pre>{@code
	 * static final ImmutableNetwork<City, Train> TRAIN_NETWORK =
	 *     NetworkBuilder.undirected()
	 *         .allowsParallelEdges(true)
	 *         .<City, Train>immutable()
	 *         .addEdge(PARIS, BRUSSELS, Thalys.trainNumber("1111"))
	 *         .addEdge(PARIS, BRUSSELS, RegionalTrain.trainNumber("2222"))
	 *         .addEdge(LONDON, PARIS, Eurostar.trainNumber("3333"))
	 *         .addEdge(LONDON, BRUSSELS, Eurostar.trainNumber("4444"))
	 *         .addNode(REYKJAVIK)
	 *         .build();
	 * }</pre>
	 *
	 * <p>
	 * Builder instances can be reused; it is safe to call {@link #build} multiple times
	 * to build multiple networks in series. Each new network contains all the elements of
	 * the ones created before it.
	 *
	 * @since 28.0
	 */
	public static class Builder<N, E> {

		private final MutableNetwork<N, E> mutableNetwork;

		Builder(NetworkBuilder<N, E> networkBuilder) {
			mutableNetwork = networkBuilder.build();
		}

		/**
		 * Adds {@code node} if it is not already present.
		 *
		 * <p>
		 * <b>Nodes must be unique</b>, just as {@code Map} keys must be. They must also
		 * be non-null.
		 * @return this {@code Builder} object
		 */
		@CanIgnoreReturnValue
		public Builder<N, E> addNode(N node) {
			mutableNetwork.addNode(node);
			return this;
		}

		/**
		 * Adds {@code edge} connecting {@code nodeU} to {@code nodeV}.
		 *
		 * <p>
		 * If the network is directed, {@code edge} will be directed in this network;
		 * otherwise, it will be undirected.
		 *
		 * <p>
		 * <b>{@code edge} must be unique to this network</b>, just as a {@code Map} key
		 * must be. It must also be non-null.
		 *
		 * <p>
		 * If {@code nodeU} and {@code nodeV} are not already present in this network,
		 * this method will silently {@link #addNode(Object) add} {@code nodeU} and
		 * {@code nodeV} to the network.
		 *
		 * <p>
		 * If {@code edge} already connects {@code nodeU} to {@code nodeV} (in the
		 * specified order if this network {@link #isDirected()}, else in any order), then
		 * this method will have no effect.
		 * @return this {@code Builder} object
		 * @throws IllegalArgumentException if {@code edge} already exists in the network
		 * and does not connect {@code nodeU} to {@code nodeV}
		 * @throws IllegalArgumentException if the introduction of the edge would violate
		 * {@link #allowsParallelEdges()} or {@link #allowsSelfLoops()}
		 */
		@CanIgnoreReturnValue
		public Builder<N, E> addEdge(N nodeU, N nodeV, E edge) {
			mutableNetwork.addEdge(nodeU, nodeV, edge);
			return this;
		}

		/**
		 * Adds {@code edge} connecting {@code endpoints}. In an undirected network,
		 * {@code edge} will also connect {@code nodeV} to {@code nodeU}.
		 *
		 * <p>
		 * If this network is directed, {@code edge} will be directed in this network; if
		 * it is undirected, {@code edge} will be undirected in this network.
		 *
		 * <p>
		 * If this network is directed, {@code endpoints} must be ordered.
		 *
		 * <p>
		 * <b>{@code edge} must be unique to this network</b>, just as a {@code Map} key
		 * must be. It must also be non-null.
		 *
		 * <p>
		 * If either or both endpoints are not already present in this network, this
		 * method will silently {@link #addNode(Object) add} each missing endpoint to the
		 * network.
		 *
		 * <p>
		 * If {@code edge} already connects an endpoint pair equal to {@code endpoints},
		 * then this method will have no effect.
		 * @return this {@code Builder} object
		 * @throws IllegalArgumentException if {@code edge} already exists in the network
		 * and connects some other endpoint pair that is not equal to {@code endpoints}
		 * @throws IllegalArgumentException if the introduction of the edge would violate
		 * {@link #allowsParallelEdges()} or {@link #allowsSelfLoops()}
		 * @throws IllegalArgumentException if the endpoints are unordered and the network
		 * is directed
		 */
		@CanIgnoreReturnValue
		public Builder<N, E> addEdge(EndpointPair<N> endpoints, E edge) {
			mutableNetwork.addEdge(endpoints, edge);
			return this;
		}

		/**
		 * Returns a newly-created {@code ImmutableNetwork} based on the contents of this
		 * {@code
		 * Builder}.
		 */
		public ImmutableNetwork<N, E> build() {
			return ImmutableNetwork.copyOf(mutableNetwork);
		}

	}

}
