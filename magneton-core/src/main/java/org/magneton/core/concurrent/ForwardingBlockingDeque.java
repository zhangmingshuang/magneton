/*
 * Copyright (C) 2012 The Guava Authors
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

package org.magneton.core.concurrent;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.CheckForNull;

import org.magneton.core.collect.ForwardingDeque;

/**
 * A {@link BlockingDeque} which forwards all its method calls to another
 * {@code BlockingDeque}. Subclasses should override one or more methods to modify the
 * behavior of the backing deque as desired per the
 * <a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p>
 * <b>Warning:</b> The methods of {@code ForwardingBlockingDeque} forward
 * <b>indiscriminately</b> to the methods of the delegate. For example, overriding
 * {@link #add} alone <b>will not</b> change the behaviour of {@link #offer} which can
 * lead to unexpected behaviour. In this case, you should override {@code offer} as well,
 * either providing your own implementation, or delegating to the provided
 * {@code standardOffer} method.
 *
 * <p>
 * <b>{@code default} method warning:</b> This class does <i>not</i> forward calls to
 * {@code
 * default} methods. Instead, it inherits their default implementations. When those
 * implementations invoke methods, they invoke methods on the
 * {@code ForwardingBlockingDeque}.
 *
 * <p>
 * The {@code standard} methods are not guaranteed to be thread-safe, even when all of the
 * methods that they depend on are thread-safe.
 *
 * @author Emily Soldal
 * @since 21.0 (since 14.0 as {@link org.magneton.core.collect.ForwardingBlockingDeque})
 */

@ElementTypesAreNonnullByDefault
public abstract class ForwardingBlockingDeque<E> extends ForwardingDeque<E> implements BlockingDeque<E> {

	/** Constructor for use by subclasses. */
	protected ForwardingBlockingDeque() {
	}

	@Override
	protected abstract BlockingDeque<E> delegate();

	@Override
	public int remainingCapacity() {
		return this.delegate().remainingCapacity();
	}

	@Override
	public void putFirst(E e) throws InterruptedException {
		this.delegate().putFirst(e);
	}

	@Override
	public void putLast(E e) throws InterruptedException {
		this.delegate().putLast(e);
	}

	@Override
	public boolean offerFirst(E e, long timeout, TimeUnit unit) throws InterruptedException {
		return this.delegate().offerFirst(e, timeout, unit);
	}

	@Override
	public boolean offerLast(E e, long timeout, TimeUnit unit) throws InterruptedException {
		return this.delegate().offerLast(e, timeout, unit);
	}

	@Override
	public E takeFirst() throws InterruptedException {
		return this.delegate().takeFirst();
	}

	@Override
	public E takeLast() throws InterruptedException {
		return this.delegate().takeLast();
	}

	@Override
	@CheckForNull
	public E pollFirst(long timeout, TimeUnit unit) throws InterruptedException {
		return this.delegate().pollFirst(timeout, unit);
	}

	@Override
	@CheckForNull
	public E pollLast(long timeout, TimeUnit unit) throws InterruptedException {
		return this.delegate().pollLast(timeout, unit);
	}

	@Override
	public void put(E e) throws InterruptedException {
		this.delegate().put(e);
	}

	@Override
	public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
		return this.delegate().offer(e, timeout, unit);
	}

	@Override
	public E take() throws InterruptedException {
		return this.delegate().take();
	}

	@Override
	@CheckForNull
	public E poll(long timeout, TimeUnit unit) throws InterruptedException {
		return this.delegate().poll(timeout, unit);
	}

	@Override
	public int drainTo(Collection<? super E> c) {
		return this.delegate().drainTo(c);
	}

	@Override
	public int drainTo(Collection<? super E> c, int maxElements) {
		return this.delegate().drainTo(c, maxElements);
	}

}