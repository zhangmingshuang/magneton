/*
 * Copyright (C) 2015 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.collect;

import java.util.Queue;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;

/**
 * An Iterator implementation which draws elements from a queue, removing them from the
 * queue as it iterates.
 */
@ElementTypesAreNonnullByDefault
final class ConsumingQueueIterator<T> extends AbstractIterator<T> {

	private final Queue<T> queue;

	ConsumingQueueIterator(Queue<T> queue) {
		this.queue = Preconditions.checkNotNull(queue);
	}

	@Override
	@CheckForNull
	public T computeNext() {
		// TODO(b/192579700): Use a ternary once it no longer confuses our nullness
		// checker.
		if (queue.isEmpty()) {
			return endOfData();
		}
		return queue.remove();
	}

}
