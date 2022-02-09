/*
 * Copyright (C) 2007 The Guava Authors
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

package org.magneton.core.base;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.CheckForNull;
import javax.annotations.VisibleForTesting;

import org.checkerframework.checker.nullness.qual.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Useful suppliers.
 *
 * <p>
 * All methods return serializable suppliers as long as they're given serializable
 * parameters.
 *
 * @author Laurence Gonsalves
 * @author Harry Heymann
 * @since 2.0
 */
@ElementTypesAreNonnullByDefault
public final class Suppliers {

	private Suppliers() {
	}

	/**
	 * Returns a new supplier which is the composition of the provided function and
	 * supplier. In other words, the new supplier's value will be computed by retrieving
	 * the value from {@code supplier}, and then applying {@code function} to that value.
	 * Note that the resulting supplier will not call {@code supplier} or invoke
	 * {@code function} until it is called.
	 */
	public static <F extends Object, T extends Object> org.magneton.core.base.Supplier<T> compose(
			org.magneton.core.base.Function<? super F, T> function, org.magneton.core.base.Supplier<F> supplier) {
		return new SupplierComposition<>(function, supplier);
	}

	/**
	 * Returns a supplier which caches the instance retrieved during the first call to
	 * {@code get()} and returns that value on subsequent calls to {@code get()}. See:
	 * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
	 *
	 * <p>
	 * The returned supplier is thread-safe. The delegate's {@code get()} method will be
	 * invoked at most once unless the underlying {@code get()} throws an exception. The
	 * supplier's serialized form does not contain the cached value, which will be
	 * recalculated when {@code get()} is called on the reserialized instance.
	 *
	 * <p>
	 * When the underlying delegate throws an exception then this memoizing supplier will
	 * keep delegating calls until it returns valid data.
	 *
	 * <p>
	 * If {@code delegate} is an instance created by an earlier call to {@code memoize},
	 * it is returned directly.
	 */
	public static <T> org.magneton.core.base.Supplier<T> memoize(org.magneton.core.base.Supplier<T> delegate) {
		if (delegate instanceof NonSerializableMemoizingSupplier || delegate instanceof MemoizingSupplier) {
			return delegate;
		}
		return delegate instanceof Serializable ? new MemoizingSupplier<T>(delegate)
				: new NonSerializableMemoizingSupplier<T>(delegate);
	}

	/**
	 * Returns a supplier that caches the instance supplied by the delegate and removes
	 * the cached value after the specified time has passed. Subsequent calls to
	 * {@code get()} return the cached value if the expiration time has not passed. After
	 * the expiration time, a new value is retrieved, cached, and returned. See:
	 * <a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>
	 *
	 * <p>
	 * The returned supplier is thread-safe. The supplier's serialized form does not
	 * contain the cached value, which will be recalculated when {@code get()} is called
	 * on the reserialized instance. The actual memoization does not happen when the
	 * underlying delegate throws an exception.
	 *
	 * <p>
	 * When the underlying delegate throws an exception then this memoizing supplier will
	 * keep delegating calls until it returns valid data.
	 * @param duration the length of time after a value is created that it should stop
	 * being returned by subsequent {@code get()} calls
	 * @param unit the unit that {@code duration} is expressed in
	 * @throws IllegalArgumentException if {@code duration} is not positive
	 * @since 2.0
	 */
	// should accept a java.time.Duration
	public static <T> org.magneton.core.base.Supplier<T> memoizeWithExpiration(
			org.magneton.core.base.Supplier<T> delegate, long duration, TimeUnit unit) {
		return new ExpiringMemoizingSupplier<>(delegate, duration, unit);
	}

	/** Returns a supplier that always supplies {@code instance}. */
	public static <T> org.magneton.core.base.Supplier<T> ofInstance(@ParametricNullness T instance) {
		return new SupplierOfInstance<>(instance);
	}

	/**
	 * Returns a supplier whose {@code get()} method synchronizes on {@code delegate}
	 * before calling it, making it thread-safe.
	 */
	public static <T> org.magneton.core.base.Supplier<T> synchronizedSupplier(
			org.magneton.core.base.Supplier<T> delegate) {
		return new ThreadSafeSupplier<>(delegate);
	}

	/**
	 * Returns a function that accepts a supplier and returns the result of invoking
	 * {@link org.magneton.core.base.Supplier#get} on that supplier.
	 *
	 * <p>
	 * <b>Java 8 users:</b> use the method reference {@code Supplier::get} instead.
	 *
	 * @since 8.0
	 */
	public static <T> org.magneton.core.base.Function<org.magneton.core.base.Supplier<T>, T> supplierFunction() {
		// implementation is "fully variant"
		SupplierFunction<T> sf = (SupplierFunction<T>) SupplierFunctionImpl.INSTANCE;
		return sf;
	}

	private enum SupplierFunctionImpl implements SupplierFunction<@Nullable Object> {

		INSTANCE;

		// Note: This makes T a "pass-through type"
		@Override
		@CheckForNull
		public Object apply(Supplier<@Nullable Object> input) {
			return input.get();
		}

		@Override
		public String toString() {
			return "Suppliers.supplierFunction()";
		}

	}

	private interface SupplierFunction<T> extends Function<org.magneton.core.base.Supplier<T>, T> {

	}

	private static class SupplierComposition<F extends Object, T extends Object>
			implements org.magneton.core.base.Supplier<T>, Serializable {

		private static final long serialVersionUID = 0;

		final org.magneton.core.base.Function<? super F, T> function;

		final org.magneton.core.base.Supplier<F> supplier;

		SupplierComposition(org.magneton.core.base.Function<? super F, T> function,
				org.magneton.core.base.Supplier<F> supplier) {
			this.function = Preconditions.checkNotNull(function);
			this.supplier = Preconditions.checkNotNull(supplier);
		}

		@Override
		@ParametricNullness
		public T get() {
			return function.apply(supplier.get());
		}

		@Override
		public boolean equals(@CheckForNull Object obj) {
			if (obj instanceof SupplierComposition) {
				SupplierComposition<?, ?> that = (SupplierComposition<?, ?>) obj;
				return function.equals(that.function) && supplier.equals(that.supplier);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return org.magneton.core.base.Objects.hashCode(function, supplier);
		}

		@Override
		public String toString() {
			return "Suppliers.compose(" + function + ", " + supplier + ")";
		}

	}

	@VisibleForTesting
	static class MemoizingSupplier<T> implements org.magneton.core.base.Supplier<T>, Serializable {

		private static final long serialVersionUID = 0;

		final org.magneton.core.base.Supplier<T> delegate;

		transient volatile boolean initialized;

		// "value" does not need to be volatile; visibility piggy-backs
		// on volatile read of "initialized".
		@CheckForNull
		transient T value;

		MemoizingSupplier(org.magneton.core.base.Supplier<T> delegate) {
			this.delegate = Preconditions.checkNotNull(delegate);
		}

		@Override
		@ParametricNullness
		public T get() {
			// A 2-field variant of Double Checked Locking.
			if (!initialized) {
				synchronized (this) {
					if (!initialized) {
						T t = delegate.get();
						value = t;
						initialized = true;
						return t;
					}
				}
			}
			// This is safe because we checked `initialized.`
			return NullnessCasts.uncheckedCastNullableTToT(value);
		}

		@Override
		public String toString() {
			return "Suppliers.memoize(" + (initialized ? "<supplier that returned " + value + ">" : delegate) + ")";
		}

	}

	@VisibleForTesting
	static class NonSerializableMemoizingSupplier<T> implements org.magneton.core.base.Supplier<T> {

		@CheckForNull
		volatile org.magneton.core.base.Supplier<T> delegate;

		volatile boolean initialized;

		// "value" does not need to be volatile; visibility piggy-backs
		// on volatile read of "initialized".
		@CheckForNull
		T value;

		NonSerializableMemoizingSupplier(org.magneton.core.base.Supplier<T> delegate) {
			this.delegate = Preconditions.checkNotNull(delegate);
		}

		@Override
		@ParametricNullness
		public T get() {
			// A 2-field variant of Double Checked Locking.
			if (!initialized) {
				synchronized (this) {
					if (!initialized) {
						/*
						 * requireNonNull is safe because we read and write `delegate`
						 * under synchronization.
						 *
						 * TODO(cpovirk): To avoid having to check for null, replace
						 * `delegate` with a singleton `Supplier` that always throws an
						 * exception.
						 */
						T t = requireNonNull(delegate).get();
						value = t;
						initialized = true;
						// Release the delegate to GC.
						delegate = null;
						return t;
					}
				}
			}
			// This is safe because we checked `initialized.`
			return NullnessCasts.uncheckedCastNullableTToT(value);
		}

		@Override
		public String toString() {
			org.magneton.core.base.Supplier<T> delegate = this.delegate;
			return "Suppliers.memoize(" + (delegate == null ? "<supplier that returned " + value + ">" : delegate)
					+ ")";
		}

	}

	@VisibleForTesting
	// lots of violations
	static class ExpiringMemoizingSupplier<T> implements org.magneton.core.base.Supplier<T>, Serializable {

		private static final long serialVersionUID = 0;

		final org.magneton.core.base.Supplier<T> delegate;

		final long durationNanos;

		@CheckForNull
		transient volatile T value;

		// The special value 0 means "not yet initialized".
		transient volatile long expirationNanos;

		ExpiringMemoizingSupplier(org.magneton.core.base.Supplier<T> delegate, long duration, TimeUnit unit) {
			this.delegate = Preconditions.checkNotNull(delegate);
			durationNanos = unit.toNanos(duration);
			Preconditions.checkArgument(duration > 0, "duration (%s %s) must be > 0", duration, unit);
		}

		@Override
		@ParametricNullness
		public T get() {
			// Another variant of Double Checked Locking.
			//
			// We use two volatile reads. We could reduce this to one by
			// putting our fields into a holder class, but (at least on x86)
			// the extra memory consumption and indirection are more
			// expensive than the extra volatile reads.
			long nanos = expirationNanos;
			long now = org.magneton.core.base.Platform.systemNanoTime();
			if (nanos == 0 || now - nanos >= 0) {
				synchronized (this) {
					if (nanos == expirationNanos) { // recheck for lost race
						T t = delegate.get();
						value = t;
						nanos = now + durationNanos;
						// In the very unlikely event that nanos is 0, set it to 1;
						// no one will notice 1 ns of tardiness.
						expirationNanos = (nanos == 0) ? 1 : nanos;
						return t;
					}
				}
			}
			// This is safe because we checked `expirationNanos.`
			return NullnessCasts.uncheckedCastNullableTToT(value);
		}

		@Override
		public String toString() {
			// This is a little strange if the unit the user provided was not NANOS,
			// but we don't want to store the unit just for toString
			return "Suppliers.memoizeWithExpiration(" + delegate + ", " + durationNanos + ", NANOS)";
		}

	}

	private static class SupplierOfInstance<T> implements org.magneton.core.base.Supplier<T>, Serializable {

		private static final long serialVersionUID = 0;

		@ParametricNullness
		final T instance;

		SupplierOfInstance(@ParametricNullness T instance) {
			this.instance = instance;
		}

		@Override
		@ParametricNullness
		public T get() {
			return instance;
		}

		@Override
		public boolean equals(@CheckForNull Object obj) {
			if (obj instanceof SupplierOfInstance) {
				SupplierOfInstance<?> that = (SupplierOfInstance<?>) obj;
				return org.magneton.core.base.Objects.equal(instance, that.instance);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(instance);
		}

		@Override
		public String toString() {
			return "Suppliers.ofInstance(" + instance + ")";
		}

	}

	private static class ThreadSafeSupplier<T> implements org.magneton.core.base.Supplier<T>, Serializable {

		private static final long serialVersionUID = 0;

		final org.magneton.core.base.Supplier<T> delegate;

		ThreadSafeSupplier(org.magneton.core.base.Supplier<T> delegate) {
			this.delegate = Preconditions.checkNotNull(delegate);
		}

		@Override
		@ParametricNullness
		public T get() {
			synchronized (delegate) {
				return delegate.get();
			}
		}

		@Override
		public String toString() {
			return "Suppliers.synchronizedSupplier(" + delegate + ")";
		}

	}

}