/*
 * Copyright (C) 2009 The Guava Authors
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

package org.magneton.core.collect;

import java.util.Map;

import org.magneton.core.base.Preconditions;

/**
 * An implementation of {@link org.magneton.core.collect.ImmutableTable} that holds a
 * single cell.
 *
 * @author Gregory Kick
 */

@ElementTypesAreNonnullByDefault
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {

	final R singleRowKey;

	final C singleColumnKey;

	final V singleValue;

	SingletonImmutableTable(R rowKey, C columnKey, V value) {
		singleRowKey = Preconditions.checkNotNull(rowKey);
		singleColumnKey = Preconditions.checkNotNull(columnKey);
		singleValue = Preconditions.checkNotNull(value);
	}

	SingletonImmutableTable(Table.Cell<R, C, V> cell) {
		this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<R, V> column(C columnKey) {
		Preconditions.checkNotNull(columnKey);
		return containsColumn(columnKey) ? org.magneton.core.collect.ImmutableMap.of(singleRowKey, singleValue)
				: org.magneton.core.collect.ImmutableMap.<R, V>of();
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<C, Map<R, V>> columnMap() {
		return org.magneton.core.collect.ImmutableMap.of(singleColumnKey,
				(Map<R, V>) org.magneton.core.collect.ImmutableMap.of(singleRowKey, singleValue));
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<R, Map<C, V>> rowMap() {
		return org.magneton.core.collect.ImmutableMap.of(singleRowKey,
				(Map<C, V>) ImmutableMap.of(singleColumnKey, singleValue));
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	org.magneton.core.collect.ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
		return org.magneton.core.collect.ImmutableSet.of(cellOf(singleRowKey, singleColumnKey, singleValue));
	}

	@Override
	ImmutableCollection<V> createValues() {
		return ImmutableSet.of(singleValue);
	}

	@Override
	SerializedForm createSerializedForm() {
		return SerializedForm.create(this, new int[] { 0 }, new int[] { 0 });
	}

}
