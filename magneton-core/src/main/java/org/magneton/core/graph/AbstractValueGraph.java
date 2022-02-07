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
import java.util.Optional;
import java.util.Set;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Function;
import org.magneton.core.collect.Maps;

import static java.util.Objects.requireNonNull;

/**
 * This class provides a skeletal implementation of
 * {@link org.magneton.core.graph.ValueGraph}. It is recommended to extend this class
 * rather than implement {@link org.magneton.core.graph.ValueGraph} directly.
 *
 * <p>
 * The methods implemented in this class should not be overridden unless the subclass
 * admits a more efficient implementation.
 *
 * @author James Sexton
 * @param <N> Node parameter type
 * @param <V> Value parameter type
 * @since 20.0
 */

@ElementTypesAreNonnullByDefault
public abstract class AbstractValueGraph<N, V> extends AbstractBaseGraph<N>
		implements org.magneton.core.graph.ValueGraph<N, V> {

	private static <N, V> Map<org.magneton.core.graph.EndpointPair<N>, V> edgeValueMap(ValueGraph<N, V> graph) {
		Function<org.magneton.core.graph.EndpointPair<N>, V> edgeToValueFn = new Function<org.magneton.core.graph.EndpointPair<N>, V>() {
			@Override
			public V apply(EndpointPair<N> edge) {
				// requireNonNull is safe because the endpoint pair comes from the graph.
				return requireNonNull(graph.edgeValueOrDefault(edge.nodeU(), edge.nodeV(), null));
			}
		};
		return Maps.asMap(graph.edges(), edgeToValueFn);
	}

	@Override
	public Graph<N> asGraph() {
		return new AbstractGraph<N>() {
			@Override
			public Set<N> nodes() {
				return AbstractValueGraph.this.nodes();
			}

			@Override
			public Set<org.magneton.core.graph.EndpointPair<N>> edges() {
				return AbstractValueGraph.this.edges();
			}

			@Override
			public boolean isDirected() {
				return AbstractValueGraph.this.isDirected();
			}

			@Override
			public boolean allowsSelfLoops() {
				return AbstractValueGraph.this.allowsSelfLoops();
			}

			@Override
			public org.magneton.core.graph.ElementOrder<N> nodeOrder() {
				return AbstractValueGraph.this.nodeOrder();
			}

			@Override
			public ElementOrder<N> incidentEdgeOrder() {
				return AbstractValueGraph.this.incidentEdgeOrder();
			}

			@Override
			public Set<N> adjacentNodes(N node) {
				return AbstractValueGraph.this.adjacentNodes(node);
			}

			@Override
			public Set<N> predecessors(N node) {
				return AbstractValueGraph.this.predecessors(node);
			}

			@Override
			public Set<N> successors(N node) {
				return AbstractValueGraph.this.successors(node);
			}

			@Override
			public int degree(N node) {
				return AbstractValueGraph.this.degree(node);
			}

			@Override
			public int inDegree(N node) {
				return AbstractValueGraph.this.inDegree(node);
			}

			@Override
			public int outDegree(N node) {
				return AbstractValueGraph.this.outDegree(node);
			}
		};
	}

	@Override
	public Optional<V> edgeValue(N nodeU, N nodeV) {
		return Optional.ofNullable(edgeValueOrDefault(nodeU, nodeV, null));
	}

	@Override
	public Optional<V> edgeValue(org.magneton.core.graph.EndpointPair<N> endpoints) {
		return Optional.ofNullable(edgeValueOrDefault(endpoints, null));
	}

	@Override
	public final boolean equals(@CheckForNull Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof org.magneton.core.graph.ValueGraph)) {
			return false;
		}
		org.magneton.core.graph.ValueGraph<?, ?> other = (org.magneton.core.graph.ValueGraph<?, ?>) obj;

		return isDirected() == other.isDirected() && nodes().equals(other.nodes())
				&& edgeValueMap(this).equals(edgeValueMap(other));
	}

	@Override
	public final int hashCode() {
		return edgeValueMap(this).hashCode();
	}

	/** Returns a string representation of this graph. */
	@Override
	public String toString() {
		return "isDirected: " + isDirected() + ", allowsSelfLoops: " + allowsSelfLoops() + ", nodes: " + nodes()
				+ ", edges: " + edgeValueMap(this);
	}

}
