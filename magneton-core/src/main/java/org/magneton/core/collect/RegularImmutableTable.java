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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.CheckForNull;

import javax.annotation.WeakOuter;
import org.magneton.core.base.Preconditions;

/**
 * An implementation of {@link org.magneton.core.collect.ImmutableTable} holding an
 * arbitrary number of cells.
 *
 * @author Gregory Kick
 */

@ElementTypesAreNonnullByDefault
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {

	RegularImmutableTable() {
	}

	static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells,
			@CheckForNull Comparator<? super R> rowComparator, @CheckForNull Comparator<? super C> columnComparator) {
		Preconditions.checkNotNull(cells);
		if (rowComparator != null || columnComparator != null) {
			/*
			 * This sorting logic leads to a cellSet() ordering that may not be expected
			 * and that isn't documented in the Javadoc. If a row Comparator is provided,
			 * cellSet() iterates across the columns in the first row, the columns in the
			 * second row, etc. If a column Comparator is provided but a row Comparator
			 * isn't, cellSet() iterates across the rows in the first column, the rows in
			 * the second column, etc.
			 */
			Comparator<Table.Cell<R, C, V>> comparator = (Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) -> {
				int rowCompare = (rowComparator == null) ? 0
						: rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
				if (rowCompare != 0) {
					return rowCompare;
				}
				return (columnComparator == null) ? 0
						: columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
			};
			Collections.sort(cells, comparator);
		}
		return forCellsInternal(cells, rowComparator, columnComparator);
	}

	static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
		return forCellsInternal(cells, null, null);
	}

	private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells,
			@CheckForNull Comparator<? super R> rowComparator, @CheckForNull Comparator<? super C> columnComparator) {
		Set<R> rowSpaceBuilder = new LinkedHashSet<>();
		Set<C> columnSpaceBuilder = new LinkedHashSet<>();
		org.magneton.core.collect.ImmutableList<Table.Cell<R, C, V>> cellList = org.magneton.core.collect.ImmutableList
				.copyOf(cells);
		for (Table.Cell<R, C, V> cell : cells) {
			rowSpaceBuilder.add(cell.getRowKey());
			columnSpaceBuilder.add(cell.getColumnKey());
		}

		org.magneton.core.collect.ImmutableSet<R> rowSpace = (rowComparator == null)
				? org.magneton.core.collect.ImmutableSet.copyOf(rowSpaceBuilder)
				: org.magneton.core.collect.ImmutableSet
						.copyOf(org.magneton.core.collect.ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
		org.magneton.core.collect.ImmutableSet<C> columnSpace = (columnComparator == null)
				? org.magneton.core.collect.ImmutableSet.copyOf(columnSpaceBuilder)
				: org.magneton.core.collect.ImmutableSet.copyOf(
						org.magneton.core.collect.ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));

		return forOrderedComponents(cellList, rowSpace, columnSpace);
	}

	/** A factory that chooses the most space-efficient representation of the table. */
	static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Cell<R, C, V>> cellList,
			org.magneton.core.collect.ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
		// use a dense table if more than half of the cells have values
		// TODO(gak): tune this condition based on empirical evidence
		return (cellList.size() > (((long) rowSpace.size() * columnSpace.size()) / 2))
				? new org.magneton.core.collect.DenseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace)
				: new org.magneton.core.collect.SparseImmutableTable<R, C, V>(cellList, rowSpace, columnSpace);
	}

	abstract Table.Cell<R, C, V> getCell(int iterationIndex);

	@Override
	final org.magneton.core.collect.ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
		return isEmpty() ? org.magneton.core.collect.ImmutableSet.<Table.Cell<R, C, V>>of() : new CellSet();
	}

	abstract V getValue(int iterationIndex);

	@Override
	final ImmutableCollection<V> createValues() {
		return isEmpty() ? org.magneton.core.collect.ImmutableList.<V>of() : new Values();
	}

	/**
	 * @throws IllegalArgumentException if {@code existingValue} is not null.
	 */
	/*
	 * We could have declared this method 'static' but the additional compile-time checks
	 * achieved by referencing the type variables seem worthwhile.
	 */
	final void checkNoDuplicate(R rowKey, C columnKey, @CheckForNull V existingValue, V newValue) {
		Preconditions.checkArgument(existingValue == null, "Duplicate key: (row=%s, column=%s), values: [%s, %s].",
				rowKey, columnKey, newValue, existingValue);
	}

	@WeakOuter
	private final class CellSet extends org.magneton.core.collect.IndexedImmutableSet<Table.Cell<R, C, V>> {

		@Override
		public int size() {
			return RegularImmutableTable.this.size();
		}

		@Override
		Table.Cell<R, C, V> get(int index) {
			return getCell(index);
		}

		@Override
		public boolean contains(@CheckForNull Object object) {
			if (object instanceof Table.Cell) {
				Table.Cell<?, ?, ?> cell = (Table.Cell<?, ?, ?>) object;
				Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
				return value != null && value.equals(cell.getValue());
			}
			return false;
		}

		@Override
		boolean isPartialView() {
			return false;
		}

	}

	@WeakOuter
	private final class Values extends org.magneton.core.collect.ImmutableList<V> {

		@Override
		public int size() {
			return RegularImmutableTable.this.size();
		}

		@Override
		public V get(int index) {
			return getValue(index);
		}

		@Override
		boolean isPartialView() {
			return true;
		}

	}

}
