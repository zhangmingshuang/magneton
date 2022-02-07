/*
 * Copyright (C) 2012 The Guava Authors
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

package org.magneton.core.reflect;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

import javax.annotation.DoNotCall;
import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.ForwardingMap;
import org.magneton.core.collect.ForwardingMapEntry;
import org.magneton.core.collect.ForwardingSet;
import org.magneton.core.collect.Iterators;
import org.magneton.core.collect.Maps;

/**
 * A mutable type-to-instance map. See also {@link ImmutableTypeToInstanceMap}.
 *
 * <p>
 * This implementation <i>does</i> support null values, despite how it is annotated; see
 * discussion at {@link TypeToInstanceMap}.
 *
 * @author Ben Yu
 * @since 13.0
 */
@ElementTypesAreNonnullByDefault
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B>
		implements TypeToInstanceMap<B> {

	private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();

	@Override
	@CheckForNull
	public <T extends B> T getInstance(Class<T> type) {
		return trustedGet(TypeToken.of(type));
	}

	@Override
	@CheckForNull
	public <T extends B> T getInstance(TypeToken<T> type) {
		return trustedGet(type.rejectTypeVariables());
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public <T extends B> T putInstance(Class<T> type, T value) {
		return trustedPut(TypeToken.of(type), value);
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public <T extends B> T putInstance(TypeToken<T> type, T value) {
		return trustedPut(type.rejectTypeVariables(), value);
	}

	/**
	 * Not supported. Use {@link #putInstance} instead.
	 * @deprecated unsupported operation
	 * @throws UnsupportedOperationException always
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	@CheckForNull
	public B put(TypeToken<? extends B> key, B value) {
		throw new UnsupportedOperationException("Please use putInstance() instead.");
	}

	/**
	 * Not supported. Use {@link #putInstance} instead.
	 * @deprecated unsupported operation
	 * @throws UnsupportedOperationException always
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
		throw new UnsupportedOperationException("Please use putInstance() instead.");
	}

	@Override
	public Set<Entry<TypeToken<? extends B>, B>> entrySet() {
		return UnmodifiableEntry.transformEntries(super.entrySet());
	}

	@Override
	protected Map<TypeToken<? extends B>, B> delegate() {
		return backingMap;
	}

	// value could not get in if not a T
	@CheckForNull
	private <T extends B> T trustedPut(TypeToken<T> type, T value) {
		return (T) backingMap.put(type, value);
	}

	// value could not get in if not a T
	@CheckForNull
	private <T extends B> T trustedGet(TypeToken<T> type) {
		return (T) backingMap.get(type);
	}

	private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {

		private final Entry<K, V> delegate;

		private UnmodifiableEntry(Entry<K, V> delegate) {
			this.delegate = Preconditions.checkNotNull(delegate);
		}

		static <K, V> Set<Entry<K, V>> transformEntries(Set<Entry<K, V>> entries) {
			return new ForwardingSet<Entry<K, V>>() {
				@Override
				protected Set<Entry<K, V>> delegate() {
					return entries;
				}

				@Override
				public Iterator<Entry<K, V>> iterator() {
					return UnmodifiableEntry.transformEntries(super.iterator());
				}

				@Override
				public Object[] toArray() {
					/*
					 * standardToArray returns `@Nullable Object[]` rather than `Object[]`
					 * but only because it can be used with collections that may contain
					 * null. This collection is a collection of non-null Entry objects
					 * (Entry objects that might contain null values but are not
					 * themselves null), so we can treat it as a plain `Object[]`.
					 */
					Object[] result = standardToArray();
					return result;
				}

				@Override
				// b/192354773 in our checker affects
				// toArray declarations
				public <T> T[] toArray(T[] array) {
					return standardToArray(array);
				}
			};
		}

		private static <K, V> Iterator<Entry<K, V>> transformEntries(Iterator<Entry<K, V>> entries) {
			return Iterators.transform(entries, UnmodifiableEntry::new);
		}

		@Override
		protected Entry<K, V> delegate() {
			return delegate;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException();
		}

	}

}
