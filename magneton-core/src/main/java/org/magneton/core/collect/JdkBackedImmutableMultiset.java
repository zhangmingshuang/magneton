/*
 * Copyright (C) 2018 The Guava Authors
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

import java.util.Collection;
import java.util.Map;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Preconditions;
import org.magneton.core.primitives.Ints;

/**
 * An implementation of ImmutableMultiset backed by a JDK Map and a list of entries. Used
 * to protect against hash flooding attacks.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
final class JdkBackedImmutableMultiset<E> extends org.magneton.core.collect.ImmutableMultiset<E> {

	private final Map<E, Integer> delegateMap;

	private final org.magneton.core.collect.ImmutableList<Multiset.Entry<E>> entries;

	private final long size;

	@CheckForNull
	private transient org.magneton.core.collect.ImmutableSet<E> elementSet;

	private JdkBackedImmutableMultiset(Map<E, Integer> delegateMap, ImmutableList<Entry<E>> entries, long size) {
		this.delegateMap = delegateMap;
		this.entries = entries;
		this.size = size;
	}

	static <E> ImmutableMultiset<E> create(Collection<? extends Multiset.Entry<? extends E>> entries) {
		Multiset.Entry<E>[] entriesArray = entries.toArray(new Multiset.Entry[0]);
		Map<E, Integer> delegateMap = Maps.newHashMapWithExpectedSize(entriesArray.length);
		long size = 0;
		for (int i = 0; i < entriesArray.length; i++) {
			Multiset.Entry<E> entry = entriesArray[i];
			int count = entry.getCount();
			size += count;
			E element = Preconditions.checkNotNull(entry.getElement());
			delegateMap.put(element, count);
			if (!(entry instanceof org.magneton.core.collect.Multisets.ImmutableEntry)) {
				entriesArray[i] = Multisets.immutableEntry(element, count);
			}
		}
		return new JdkBackedImmutableMultiset<>(delegateMap,
				org.magneton.core.collect.ImmutableList.asImmutableList(entriesArray), size);
	}

	@Override
	public int count(@CheckForNull Object element) {
		return delegateMap.getOrDefault(element, 0);
	}

	@Override
	public org.magneton.core.collect.ImmutableSet<E> elementSet() {
		ImmutableSet<E> result = elementSet;
		return (result == null) ? elementSet = new ElementSet<>(entries, this) : result;
	}

	@Override
	Multiset.Entry<E> getEntry(int index) {
		return entries.get(index);
	}

	@Override
	boolean isPartialView() {
		return false;
	}

	@Override
	public int size() {
		return Ints.saturatedCast(size);
	}

}
