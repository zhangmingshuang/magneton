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

import javax.annotation.DoNotMock;

import org.magneton.core.base.Optional;

import static org.magneton.core.base.Preconditions.checkArgument;
import static org.magneton.core.base.Preconditions.checkNotNull;

/**
 * A builder for constructing instances of {@link MutableGraph} or
 * {@link org.magneton.core.graph.ImmutableGraph} with user-defined properties.
 *
 * <p>
 * A graph built by this class will have the following properties by default:
 *
 * <ul>
 * <li>does not allow self-loops
 * <li>orders {@link org.magneton.core.graph.Graph#nodes()} in the order in which the
 * elements were added
 * </ul>
 *
 * <p>
 * Examples of use:
 *
 * <pre>{@code
 * // Building a mutable graph
 * MutableGraph<String> graph = GraphBuilder.undirected().allowsSelfLoops(true).build();
 * graph.putEdge("bread", "bread");
 * graph.putEdge("chocolate", "peanut butter");
 * graph.putEdge("peanut butter", "jelly");
 *
 * // Building an immutable graph
 * ImmutableGraph<String> immutableGraph =
 *     GraphBuilder.undirected()
 *         .allowsSelfLoops(true)
 *         .<String>immutable()
 *         .putEdge("bread", "bread")
 *         .putEdge("chocolate", "peanut butter")
 *         .putEdge("peanut butter", "jelly")
 *         .build();
 * }</pre>
 *
 * @author James Sexton
 * @author Joshua O'Madadhain
 * @param <N> The most general node type this builder will support. This is normally
 * {@code Object} unless it is constrained by using a method like {@link #nodeOrder}, or
 * the builder is constructed based on an existing {@code Graph} using
 * {@link #from(org.magneton.core.graph.Graph)}.
 * @since 20.0
 */

@DoNotMock
@ElementTypesAreNonnullByDefault
public final class GraphBuilder<N> extends AbstractGraphBuilder<N> {

	/** Creates a new instance with the specified edge directionality. */
	private GraphBuilder(boolean directed) {
		super(directed);
	}

	/** Returns a {@link GraphBuilder} for building directed graphs. */
	public static GraphBuilder<Object> directed() {
		return new GraphBuilder<>(true);
	}

	/** Returns a {@link GraphBuilder} for building undirected graphs. */
	public static GraphBuilder<Object> undirected() {
		return new GraphBuilder<>(false);
	}

	/**
	 * Returns a {@link GraphBuilder} initialized with all properties queryable from
	 * {@code graph}.
	 *
	 * <p>
	 * The "queryable" properties are those that are exposed through the
	 * {@link org.magneton.core.graph.Graph} interface, such as
	 * {@link org.magneton.core.graph.Graph#isDirected()}. Other properties, such as
	 * {@link #expectedNodeCount(int)}, are not set in the new builder.
	 */
	public static <N> GraphBuilder<N> from(org.magneton.core.graph.Graph<N> graph) {
		return new GraphBuilder<N>(graph.isDirected()).allowsSelfLoops(graph.allowsSelfLoops())
				.nodeOrder(graph.nodeOrder()).incidentEdgeOrder(graph.incidentEdgeOrder());
	}

	/**
	 * Returns an {@link org.magneton.core.graph.ImmutableGraph.Builder} with the
	 * properties of this {@link GraphBuilder}.
	 *
	 * <p>
	 * The returned builder can be used for populating an
	 * {@link org.magneton.core.graph.ImmutableGraph}.
	 *
	 * <p>
	 * Note that the returned builder will always have {@link #incidentEdgeOrder} set to
	 * {@link org.magneton.core.graph.ElementOrder#stable()}, regardless of the value that
	 * was set in this builder.
	 *
	 * @since 28.0
	 */
	public <N1 extends N> org.magneton.core.graph.ImmutableGraph.Builder<N1> immutable() {
		GraphBuilder<N1> castBuilder = cast();
		return new ImmutableGraph.Builder<>(castBuilder);
	}

	/**
	 * Specifies whether the graph will allow self-loops (edges that connect a node to
	 * itself). Attempting to add a self-loop to a graph that does not allow them will
	 * throw an {@link UnsupportedOperationException}.
	 *
	 * <p>
	 * The default value is {@code false}.
	 */
	public GraphBuilder<N> allowsSelfLoops(boolean allowsSelfLoops) {
		this.allowsSelfLoops = allowsSelfLoops;
		return this;
	}

	/**
	 * Specifies the expected number of nodes in the graph.
	 * @throws IllegalArgumentException if {@code expectedNodeCount} is negative
	 */
	public GraphBuilder<N> expectedNodeCount(int expectedNodeCount) {
		this.expectedNodeCount = Optional.of(Graphs.checkNonNegative(expectedNodeCount));
		return this;
	}

	/**
	 * Specifies the order of iteration for the elements of
	 * {@link org.magneton.core.graph.Graph#nodes()}.
	 *
	 * <p>
	 * The default value is {@link org.magneton.core.graph.ElementOrder#insertion()
	 * insertion order}.
	 */
	public <N1 extends N> GraphBuilder<N1> nodeOrder(org.magneton.core.graph.ElementOrder<N1> nodeOrder) {
		GraphBuilder<N1> newBuilder = cast();
		newBuilder.nodeOrder = checkNotNull(nodeOrder);
		return newBuilder;
	}

	/**
	 * Specifies the order of iteration for the elements of
	 * {@link org.magneton.core.graph.Graph#edges()},
	 * {@link org.magneton.core.graph.Graph#adjacentNodes(Object)},
	 * {@link org.magneton.core.graph.Graph#predecessors(Object)},
	 * {@link org.magneton.core.graph.Graph#successors(Object)} and
	 * {@link Graph#incidentEdges(Object)}.
	 *
	 * <p>
	 * The default value is {@link org.magneton.core.graph.ElementOrder#unordered()
	 * unordered} for mutable graphs. For immutable graphs, this value is ignored; they
	 * always have a {@link org.magneton.core.graph.ElementOrder#stable() stable} order.
	 * @throws IllegalArgumentException if {@code incidentEdgeOrder} is not either {@code
	 *     ElementOrder.unordered()} or {@code ElementOrder.stable()}.
	 * @since 29.0
	 */
	public <N1 extends N> GraphBuilder<N1> incidentEdgeOrder(
			org.magneton.core.graph.ElementOrder<N1> incidentEdgeOrder) {
		checkArgument(
				incidentEdgeOrder.type() == org.magneton.core.graph.ElementOrder.Type.UNORDERED
						|| incidentEdgeOrder.type() == ElementOrder.Type.STABLE,
				"The given elementOrder (%s) is unsupported. incidentEdgeOrder() only supports"
						+ " ElementOrder.unordered() and ElementOrder.stable().",
				incidentEdgeOrder);
		GraphBuilder<N1> newBuilder = cast();
		newBuilder.incidentEdgeOrder = checkNotNull(incidentEdgeOrder);
		return newBuilder;
	}

	/**
	 * Returns an empty {@link MutableGraph} with the properties of this
	 * {@link GraphBuilder}.
	 */
	public <N1 extends N> MutableGraph<N1> build() {
		return new StandardMutableGraph<>(this);
	}

	GraphBuilder<N> copy() {
		GraphBuilder<N> newBuilder = new GraphBuilder<>(directed);
		newBuilder.allowsSelfLoops = allowsSelfLoops;
		newBuilder.nodeOrder = nodeOrder;
		newBuilder.expectedNodeCount = expectedNodeCount;
		newBuilder.incidentEdgeOrder = incidentEdgeOrder;
		return newBuilder;
	}

	private <N1 extends N> GraphBuilder<N1> cast() {
		return (GraphBuilder<N1>) this;
	}

}
