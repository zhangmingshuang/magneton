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

package org.magneton.core.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.magneton.core.base.Preconditions.checkNotNull;

/**
 * {@link FluentFuture} that forwards all calls to a delegate.
 *
 * <h3>Extension</h3>
 *
 * If you want a class like {@code FluentFuture} but with extra methods, we recommend
 * declaring your own subclass of {@link ListenableFuture}, complete with a method like
 * {@link #from} to adapt an existing {@code ListenableFuture}, implemented atop a
 * {@link ForwardingListenableFuture} that forwards to that future and adds the desired
 * methods.
 */

@ElementTypesAreNonnullByDefault
final class ForwardingFluentFuture<V extends Object> extends FluentFuture<V> {

	private final ListenableFuture<V> delegate;

	ForwardingFluentFuture(ListenableFuture<V> delegate) {
		this.delegate = checkNotNull(delegate);
	}

	@Override
	public void addListener(Runnable listener, Executor executor) {
		this.delegate.addListener(listener, executor);
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return this.delegate.cancel(mayInterruptIfRunning);
	}

	@Override
	public boolean isCancelled() {
		return this.delegate.isCancelled();
	}

	@Override
	public boolean isDone() {
		return this.delegate.isDone();
	}

	@Override
	@ParametricNullness
	public V get() throws InterruptedException, ExecutionException {
		return this.delegate.get();
	}

	@Override
	@ParametricNullness
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return this.delegate.get(timeout, unit);
	}

	@Override
	public String toString() {
		return this.delegate.toString();
	}

}
