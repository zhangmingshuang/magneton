/*
 * Copyright (C) 2009 The Guava Authors
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

import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.WeakOuter;
import javax.annotation.concurrent.Immutable;

import org.magneton.core.collect.ImmutableMap.IteratorBasedImmutableMap;

import static java.util.Objects.requireNonNull;

/** A {@code RegularImmutableTable} optimized for dense data. */
@Immutable(containerOf = { "R", "C", "V" })
@ElementTypesAreNonnullByDefault
final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {

	private final org.magneton.core.collect.ImmutableMap<R, Integer> rowKeyToIndex;

	private final org.magneton.core.collect.ImmutableMap<C, Integer> columnKeyToIndex;

	private final org.magneton.core.collect.ImmutableMap<R, org.magneton.core.collect.ImmutableMap<C, V>> rowMap;

	private final org.magneton.core.collect.ImmutableMap<C, org.magneton.core.collect.ImmutableMap<R, V>> columnMap;

	// We don't modify this after construction.
	private final int[] rowCounts;

	// We don't modify this after construction.
	private final int[] columnCounts;

	// We don't modify this after construction.
	private final @Nullable V[][] values;

	// For each cell in iteration order, the index of that cell's row key in the row key
	// list.
	// We don't modify this after construction.
	private final int[] cellRowIndices;

	// For each cell in iteration order, the index of that cell's column key in the column
	// key list.
	// We don't modify this after construction.
	private final int[] cellColumnIndices;

	DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, org.magneton.core.collect.ImmutableSet<R> rowSpace,
			org.magneton.core.collect.ImmutableSet<C> columnSpace) {
		V[][] array = (V[][]) new Object[rowSpace.size()][columnSpace.size()];
		values = array;
		rowKeyToIndex = Maps.indexMap(rowSpace);
		columnKeyToIndex = Maps.indexMap(columnSpace);
		rowCounts = new int[rowKeyToIndex.size()];
		columnCounts = new int[columnKeyToIndex.size()];
		int[] cellRowIndices = new int[cellList.size()];
		int[] cellColumnIndices = new int[cellList.size()];
		for (int i = 0; i < cellList.size(); i++) {
			Table.Cell<R, C, V> cell = cellList.get(i);
			R rowKey = cell.getRowKey();
			C columnKey = cell.getColumnKey();
			// The requireNonNull calls are safe because we construct the indexes with
			// indexMap.
			int rowIndex = requireNonNull(rowKeyToIndex.get(rowKey));
			int columnIndex = requireNonNull(columnKeyToIndex.get(columnKey));
			V existingValue = values[rowIndex][columnIndex];
			checkNoDuplicate(rowKey, columnKey, existingValue, cell.getValue());
			values[rowIndex][columnIndex] = cell.getValue();
			rowCounts[rowIndex]++;
			columnCounts[columnIndex]++;
			cellRowIndices[i] = rowIndex;
			cellColumnIndices[i] = columnIndex;
		}
		this.cellRowIndices = cellRowIndices;
		this.cellColumnIndices = cellColumnIndices;
		rowMap = new RowMap();
		columnMap = new ColumnMap();
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<C, Map<R, V>> columnMap() {
		// Casts without copying.
		org.magneton.core.collect.ImmutableMap<C, org.magneton.core.collect.ImmutableMap<R, V>> columnMap = this.columnMap;
		return org.magneton.core.collect.ImmutableMap.<C, Map<R, V>>copyOf(columnMap);
	}

	@Override
	public org.magneton.core.collect.ImmutableMap<R, Map<C, V>> rowMap() {
		// Casts without copying.
		org.magneton.core.collect.ImmutableMap<R, org.magneton.core.collect.ImmutableMap<C, V>> rowMap = this.rowMap;
		return ImmutableMap.<R, Map<C, V>>copyOf(rowMap);
	}

	@Override
	@CheckForNull
	public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
		Integer rowIndex = rowKeyToIndex.get(rowKey);
		Integer columnIndex = columnKeyToIndex.get(columnKey);
		return ((rowIndex == null) || (columnIndex == null)) ? null : values[rowIndex][columnIndex];
	}

	@Override
	public int size() {
		return cellRowIndices.length;
	}

	@Override
	Table.Cell<R, C, V> getCell(int index) {
		int rowIndex = cellRowIndices[index];
		int columnIndex = cellColumnIndices[index];
		R rowKey = rowKeySet().asList().get(rowIndex);
		C columnKey = columnKeySet().asList().get(columnIndex);
		// requireNonNull is safe because we use indexes that were populated by the
		// constructor.
		V value = requireNonNull(values[rowIndex][columnIndex]);
		return cellOf(rowKey, columnKey, value);
	}

	@Override
	V getValue(int index) {
		// requireNonNull is safe because we use indexes that were populated by the
		// constructor.
		return requireNonNull(values[cellRowIndices[index]][cellColumnIndices[index]]);
	}

	@Override
	SerializedForm createSerializedForm() {
		return SerializedForm.create(this, cellRowIndices, cellColumnIndices);
	}

	/** An immutable map implementation backed by an indexed nullable array. */
	private abstract static class ImmutableArrayMap<K, V> extends IteratorBasedImmutableMap<K, V> {

		private final int size;

		ImmutableArrayMap(int size) {
			this.size = size;
		}

		abstract org.magneton.core.collect.ImmutableMap<K, Integer> keyToIndex();

		// True if getValue never returns null.
		private boolean isFull() {
			return size == keyToIndex().size();
		}

		K getKey(int index) {
			return keyToIndex().keySet().asList().get(index);
		}

		@CheckForNull
		abstract V getValue(int keyIndex);

		@Override
		ImmutableSet<K> createKeySet() {
			return isFull() ? keyToIndex().keySet() : super.createKeySet();
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		@CheckForNull
		public V get(@CheckForNull Object key) {
			Integer keyIndex = keyToIndex().get(key);
			return (keyIndex == null) ? null : getValue(keyIndex);
		}

		@Override
		UnmodifiableIterator<Entry<K, V>> entryIterator() {
			return new AbstractIterator<Entry<K, V>>() {
				private final int maxIndex = keyToIndex().size();

				private int index = -1;

				@Override
				@CheckForNull
				protected Entry<K, V> computeNext() {
					for (index++; index < maxIndex; index++) {
						V value = getValue(index);
						if (value != null) {
							return Maps.immutableEntry(getKey(index), value);
						}
					}
					return endOfData();
				}
			};
		}

	}

	private final class Row extends ImmutableArrayMap<C, V> {

		private final int rowIndex;

		Row(int rowIndex) {
			super(rowCounts[rowIndex]);
			this.rowIndex = rowIndex;
		}

		@Override
		org.magneton.core.collect.ImmutableMap<C, Integer> keyToIndex() {
			return columnKeyToIndex;
		}

		@Override
		@CheckForNull
		V getValue(int keyIndex) {
			return DenseImmutableTable.this.values[rowIndex][keyIndex];
		}

		@Override
		boolean isPartialView() {
			return true;
		}

	}

	private final class Column extends ImmutableArrayMap<R, V> {

		private final int columnIndex;

		Column(int columnIndex) {
			super(columnCounts[columnIndex]);
			this.columnIndex = columnIndex;
		}

		@Override
		org.magneton.core.collect.ImmutableMap<R, Integer> keyToIndex() {
			return rowKeyToIndex;
		}

		@Override
		@CheckForNull
		V getValue(int keyIndex) {
			return DenseImmutableTable.this.values[keyIndex][columnIndex];
		}

		@Override
		boolean isPartialView() {
			return true;
		}

	}

	@WeakOuter
	private final class RowMap extends ImmutableArrayMap<R, org.magneton.core.collect.ImmutableMap<C, V>> {

		private RowMap() {
			super(rowCounts.length);
		}

		@Override
		org.magneton.core.collect.ImmutableMap<R, Integer> keyToIndex() {
			return rowKeyToIndex;
		}

		@Override
		org.magneton.core.collect.ImmutableMap<C, V> getValue(int keyIndex) {
			return new Row(keyIndex);
		}

		@Override
		boolean isPartialView() {
			return false;
		}

	}

	@WeakOuter
	private final class ColumnMap extends ImmutableArrayMap<C, org.magneton.core.collect.ImmutableMap<R, V>> {

		private ColumnMap() {
			super(columnCounts.length);
		}

		@Override
		org.magneton.core.collect.ImmutableMap<C, Integer> keyToIndex() {
			return columnKeyToIndex;
		}

		@Override
		org.magneton.core.collect.ImmutableMap<R, V> getValue(int keyIndex) {
			return new Column(keyIndex);
		}

		@Override
		boolean isPartialView() {
			return false;
		}

	}

}
