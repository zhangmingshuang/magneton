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

import javax.annotation.CheckForNull;

/**
 * This class provides a skeletal implementation of {@link org.magneton.core.graph.Graph}.
 * It is recommended to extend this class rather than implement
 * {@link org.magneton.core.graph.Graph} directly.
 *
 * @author James Sexton
 * @param <N> Node parameter type
 * @since 20.0
 */

@ElementTypesAreNonnullByDefault
public abstract class AbstractGraph<N> extends AbstractBaseGraph<N> implements org.magneton.core.graph.Graph<N> {

	@Override
	public final boolean equals(@CheckForNull Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof org.magneton.core.graph.Graph)) {
			return false;
		}
		org.magneton.core.graph.Graph<?> other = (Graph<?>) obj;

		return isDirected() == other.isDirected() && nodes().equals(other.nodes()) && edges().equals(other.edges());
	}

	@Override
	public final int hashCode() {
		return edges().hashCode();
	}

	/** Returns a string representation of this graph. */
	@Override
	public String toString() {
		return "isDirected: " + isDirected() + ", allowsSelfLoops: " + allowsSelfLoops() + ", nodes: " + nodes()
				+ ", edges: " + edges();
	}

}
