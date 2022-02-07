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

package org.magneton.core.cache;

import java.util.concurrent.atomic.AtomicLong;

import org.magneton.core.base.Supplier;

/**
 * Source of {@link org.magneton.core.cache.LongAddable} objects that deals with GWT,
 * Unsafe, and all that.
 *
 * @author Louis Wasserman
 */
@ElementTypesAreNonnullByDefault
final class LongAddables {

	private static final Supplier<org.magneton.core.cache.LongAddable> SUPPLIER;

	public static org.magneton.core.cache.LongAddable create() {
		return SUPPLIER.get();
	}

	private static final class PureJavaLongAddable extends AtomicLong implements org.magneton.core.cache.LongAddable {

		@Override
		public void increment() {
			getAndIncrement();
		}

		@Override
		public void add(long x) {
			getAndAdd(x);
		}

		@Override
		public long sum() {
			return get();
		}

	}

	static {
		Supplier<org.magneton.core.cache.LongAddable> supplier;
		try {
			// trigger static initialization of the LongAdder class, which may fail
			LongAdder unused = new LongAdder();
			supplier = new Supplier<org.magneton.core.cache.LongAddable>() {
				@Override
				public org.magneton.core.cache.LongAddable get() {
					return new LongAdder();
				}
			};
		}
		catch (Throwable t) { // we really want to catch *everything*
			supplier = new Supplier<org.magneton.core.cache.LongAddable>() {
				@Override
				public org.magneton.core.cache.LongAddable get() {
					return new PureJavaLongAddable();
				}
			};
		}
		SUPPLIER = supplier;
	}

}
