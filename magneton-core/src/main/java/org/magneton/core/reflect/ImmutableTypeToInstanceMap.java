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

import java.util.Map;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;

import javax.annotations.DoNotCall;
import org.magneton.core.collect.ForwardingMap;
import org.magneton.core.collect.ImmutableMap;

/**
 * A type-to-instance map backed by an {@link ImmutableMap}. See also
 * {@link MutableTypeToInstanceMap}.
 *
 * @author Ben Yu
 * @since 13.0
 */
@ElementTypesAreNonnullByDefault
public final class ImmutableTypeToInstanceMap<B>
		extends ForwardingMap<org.magneton.core.reflect.TypeToken<? extends B>, B> implements TypeToInstanceMap<B> {

	private final ImmutableMap<org.magneton.core.reflect.TypeToken<? extends B>, B> delegate;

	private ImmutableTypeToInstanceMap(ImmutableMap<org.magneton.core.reflect.TypeToken<? extends B>, B> delegate) {
		this.delegate = delegate;
	}

	/** Returns an empty type to instance map. */
	public static <B> ImmutableTypeToInstanceMap<B> of() {
		return new ImmutableTypeToInstanceMap<>(ImmutableMap.<org.magneton.core.reflect.TypeToken<? extends B>, B>of());
	}

	/** Returns a new builder. */
	public static <B> Builder<B> builder() {
		return new Builder<>();
	}

	@Override
	@CheckForNull
	public <T extends B> T getInstance(org.magneton.core.reflect.TypeToken<T> type) {
		return trustedGet(type.rejectTypeVariables());
	}

	@Override
	@CheckForNull
	public <T extends B> T getInstance(Class<T> type) {
		return trustedGet(org.magneton.core.reflect.TypeToken.of(type));
	}

	/**
	 * Guaranteed to throw an exception and leave the map unmodified.
	 * @deprecated unsupported operation
	 * @throws UnsupportedOperationException always
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	@CheckForNull
	public <T extends B> T putInstance(org.magneton.core.reflect.TypeToken<T> type, T value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the map unmodified.
	 * @deprecated unsupported operation
	 * @throws UnsupportedOperationException always
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	@CheckForNull
	public <T extends B> T putInstance(Class<T> type, T value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the map unmodified.
	 * @deprecated unsupported operation
	 * @throws UnsupportedOperationException always
	 */
	@CanIgnoreReturnValue
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	@CheckForNull
	public B put(org.magneton.core.reflect.TypeToken<? extends B> key, B value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Guaranteed to throw an exception and leave the map unmodified.
	 * @deprecated unsupported operation
	 * @throws UnsupportedOperationException always
	 */
	@Deprecated
	@Override
	@DoNotCall("Always throws UnsupportedOperationException")
	public void putAll(Map<? extends org.magneton.core.reflect.TypeToken<? extends B>, ? extends B> map) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Map<org.magneton.core.reflect.TypeToken<? extends B>, B> delegate() {
		return delegate;
	}

	// value could not get in if not a T
	@CheckForNull
	private <T extends B> T trustedGet(TypeToken<T> type) {
		return (T) delegate.get(type);
	}

	/**
	 * A builder for creating immutable type-to-instance maps. Example:
	 *
	 * <pre>{@code
	 * static final ImmutableTypeToInstanceMap<Handler<?>> HANDLERS =
	 *     ImmutableTypeToInstanceMap.<Handler<?>>builder()
	 *         .put(new TypeToken<Handler<Foo>>() {}, new FooHandler())
	 *         .put(new TypeToken<Handler<Bar>>() {}, new SubBarHandler())
	 *         .build();
	 * }</pre>
	 *
	 * <p>
	 * After invoking {@link #build()} it is still possible to add more entries and build
	 * again. Thus each map generated by this builder will be a superset of any map
	 * generated before it.
	 *
	 * @since 13.0
	 */
	public static final class Builder<B> {

		private final ImmutableMap.Builder<org.magneton.core.reflect.TypeToken<? extends B>, B> mapBuilder = ImmutableMap
				.builder();

		private Builder() {
		}

		/**
		 * Associates {@code key} with {@code value} in the built map. Duplicate keys are
		 * not allowed, and will cause {@link #build} to fail.
		 */
		@CanIgnoreReturnValue
		public <T extends B> Builder<B> put(Class<T> key, T value) {
			mapBuilder.put(org.magneton.core.reflect.TypeToken.of(key), value);
			return this;
		}

		/**
		 * Associates {@code key} with {@code value} in the built map. Duplicate keys are
		 * not allowed, and will cause {@link #build} to fail.
		 */
		@CanIgnoreReturnValue
		public <T extends B> Builder<B> put(org.magneton.core.reflect.TypeToken<T> key, T value) {
			mapBuilder.put(key.rejectTypeVariables(), value);
			return this;
		}

		/**
		 * Returns a new immutable type-to-instance map containing the entries provided to
		 * this builder.
		 * @throws IllegalArgumentException if duplicate keys were added
		 */
		public ImmutableTypeToInstanceMap<B> build() {
			return new ImmutableTypeToInstanceMap<>(mapBuilder.build());
		}

	}

}