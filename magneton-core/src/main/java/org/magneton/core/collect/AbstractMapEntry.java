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

import java.util.Map.Entry;

import javax.annotation.CheckForNull;

import org.magneton.core.base.Objects;

/**
 * Implementation of the {@code equals}, {@code hashCode}, and {@code toString} methods of
 * {@code
 * Entry}.
 *
 * @author Jared Levy
 */
@ElementTypesAreNonnullByDefault
abstract class AbstractMapEntry<K extends Object, V extends Object> implements Entry<K, V> {

	@Override
	@ParametricNullness
	public abstract K getKey();

	@Override
	@ParametricNullness
	public abstract V getValue();

	@Override
	@ParametricNullness
	public V setValue(@ParametricNullness V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(@CheckForNull Object object) {
		if (object instanceof Entry) {
			Entry<?, ?> that = (Entry<?, ?>) object;
			return org.magneton.core.base.Objects.equal(getKey(), that.getKey())
					&& Objects.equal(getValue(), that.getValue());
		}
		return false;
	}

	@Override
	public int hashCode() {
		K k = getKey();
		V v = getValue();
		return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
	}

	/** Returns a string representation of the form {@code {key}={value}}. */
	@Override
	public String toString() {
		return getKey() + "=" + getValue();
	}

}
