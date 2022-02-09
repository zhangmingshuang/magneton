/*
 * Copyright (C) 2007 The Guava Authors
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;

import javax.annotation.CheckForNull;

import javax.annotations.CanIgnoreReturnValue;
import org.magneton.core.base.Preconditions;
import org.magneton.core.primitives.Primitives;

/**
 * A mutable class-to-instance map backed by an arbitrary user-provided map. See also
 * {@link ImmutableClassToInstanceMap}.
 *
 * <p>
 * See the Guava User Guide article on <a href=
 * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#classtoinstancemap">{@code
 * ClassToInstanceMap}</a>.
 *
 * <p>
 * This implementation <i>does</i> support null values, despite how it is annotated; see
 * discussion at {@link org.magneton.core.collect.ClassToInstanceMap}.
 *
 * @author Kevin Bourrillion
 * @since 2.0
 */

// using writeReplace instead of standard serialization
@ElementTypesAreNonnullByDefault
public final class MutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B>
		implements ClassToInstanceMap<B>, Serializable {

	private final Map<Class<? extends B>, B> delegate;

	private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
		this.delegate = Preconditions.checkNotNull(delegate);
	}

	/**
	 * Returns a new {@code MutableClassToInstanceMap} instance backed by a
	 * {@link HashMap} using the default initial capacity and load factor.
	 */
	public static <B> MutableClassToInstanceMap<B> create() {
		return new MutableClassToInstanceMap<B>(new HashMap<Class<? extends B>, B>());
	}

	/**
	 * Returns a new {@code MutableClassToInstanceMap} instance backed by a given empty
	 * {@code
	 * backingMap}. The caller surrenders control of the backing map, and thus should not
	 * allow any direct references to it to remain accessible.
	 */
	public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
		return new MutableClassToInstanceMap<B>(backingMap);
	}

	/**
	 * Wraps the {@code setValue} implementation of an {@code Entry} to enforce the class
	 * constraint.
	 */
	private static <B> Entry<Class<? extends B>, B> checkedEntry(Entry<Class<? extends B>, B> entry) {
		return new ForwardingMapEntry<Class<? extends B>, B>() {
			@Override
			protected Entry<Class<? extends B>, B> delegate() {
				return entry;
			}

			@Override
			public B setValue(B value) {
				return super.setValue(cast(getKey(), value));
			}
		};
	}

	@CanIgnoreReturnValue
	@CheckForNull
	private static <B, T extends B> T cast(Class<T> type, @CheckForNull B value) {
		return Primitives.wrap(type).cast(value);
	}

	@Override
	protected Map<Class<? extends B>, B> delegate() {
		return delegate;
	}

	@Override
	public Set<Entry<Class<? extends B>, B>> entrySet() {
		return new ForwardingSet<Entry<Class<? extends B>, B>>() {

			@Override
			protected Set<Entry<Class<? extends B>, B>> delegate() {
				return MutableClassToInstanceMap.this.delegate().entrySet();
			}

			@Override
			public Spliterator<Entry<Class<? extends B>, B>> spliterator() {
				return org.magneton.core.collect.CollectSpliterators.map(delegate().spliterator(),
						MutableClassToInstanceMap::checkedEntry);
			}

			@Override
			public Iterator<Entry<Class<? extends B>, B>> iterator() {
				return new org.magneton.core.collect.TransformedIterator<Entry<Class<? extends B>, B>, Entry<Class<? extends B>, B>>(
						delegate().iterator()) {
					@Override
					Entry<Class<? extends B>, B> transform(Entry<Class<? extends B>, B> from) {
						return checkedEntry(from);
					}
				};
			}

			@Override
			public Object[] toArray() {
				/*
				 * standardToArray returns `@Nullable Object[]` rather than `Object[]` but
				 * only because it can be used with collections that may contain null.
				 * This collection is a collection of non-null Entry objects (Entry
				 * objects that might contain null values but are not themselves null), so
				 * we can treat it as a plain `Object[]`.
				 */
				Object[] result = standardToArray();
				return result;
			}

			@Override
			// b/192354773 in our checker affects toArray
			// declarations
			public <T> T[] toArray(T[] array) {
				return standardToArray(array);
			}
		};
	}

	@Override
	@CanIgnoreReturnValue
	@CheckForNull
	public B put(Class<? extends B> key, B value) {
		return super.put(key, cast(key, value));
	}

	@Override
	public void putAll(Map<? extends Class<? extends B>, ? extends B> map) {
		Map<Class<? extends B>, B> copy = new LinkedHashMap<>(map);
		for (Entry<? extends Class<? extends B>, B> entry : copy.entrySet()) {
			cast(entry.getKey(), entry.getValue());
		}
		super.putAll(copy);
	}

	@CanIgnoreReturnValue
	@Override
	@CheckForNull
	public <T extends B> T putInstance(Class<T> type, T value) {
		return cast(type, put(type, value));
	}

	@Override
	@CheckForNull
	public <T extends B> T getInstance(Class<T> type) {
		return cast(type, get(type));
	}

	private Object writeReplace() {
		return new SerializedForm(delegate());
	}

	/** Serialized form of the map, to avoid serializing the constraint. */
	private static final class SerializedForm<B> implements Serializable {

		private static final long serialVersionUID = 0;

		private final Map<Class<? extends B>, B> backingMap;

		SerializedForm(Map<Class<? extends B>, B> backingMap) {
			this.backingMap = backingMap;
		}

		Object readResolve() {
			return create(backingMap);
		}

	}

}