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

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotations.CanIgnoreReturnValue;
import javax.annotations.WeakOuter;

import org.magneton.core.base.Supplier;

/**
 * Base class for services that do not need a thread while "running" but may need one
 * during startup and shutdown. Subclasses can implement {@link #startUp} and
 * {@link #shutDown} methods, each which run in a executor which by default uses a
 * separate thread for each method.
 *
 * @author Chris Nokleberg
 * @since 1.0
 */

@ElementTypesAreNonnullByDefault
public abstract class AbstractIdleService implements Service {

	/* Thread names will look like {@code "MyService STARTING"}. */
	private final Supplier<String> threadNameSupplier = new ThreadNameSupplier();

	/* use AbstractService for state management */
	private final Service delegate = new DelegateService();

	/** Constructor for use by subclasses. */
	protected AbstractIdleService() {
	}

	/** Start the service. */
	protected abstract void startUp() throws Exception;

	/** Stop the service. */
	protected abstract void shutDown() throws Exception;

	/**
	 * Returns the {@link Executor} that will be used to run this service. Subclasses may
	 * override this method to use a custom {@link Executor}, which may configure its
	 * worker thread with a specific name, thread group or priority. The returned
	 * executor's {@link Executor#execute(Runnable) execute()} method is called when this
	 * service is started and stopped, and should return promptly.
	 */
	protected Executor executor() {
		return new Executor() {
			@Override
			public void execute(Runnable command) {
				MoreExecutors.newThread(AbstractIdleService.this.threadNameSupplier.get(), command).start();
			}
		};
	}

	@Override
	public String toString() {
		return this.serviceName() + " [" + this.state() + "]";
	}

	@Override
	public final boolean isRunning() {
		return this.delegate.isRunning();
	}

	@Override
	public final State state() {
		return this.delegate.state();
	}

	/** @since 13.0 */
	@Override
	public final void addListener(Listener listener, Executor executor) {
		this.delegate.addListener(listener, executor);
	}

	/** @since 14.0 */
	@Override
	public final Throwable failureCause() {
		return this.delegate.failureCause();
	}

	/** @since 15.0 */
	@CanIgnoreReturnValue
	@Override
	public final Service startAsync() {
		this.delegate.startAsync();
		return this;
	}

	/** @since 15.0 */
	@CanIgnoreReturnValue
	@Override
	public final Service stopAsync() {
		this.delegate.stopAsync();
		return this;
	}

	/** @since 15.0 */
	@Override
	public final void awaitRunning() {
		this.delegate.awaitRunning();
	}

	/** @since 28.0 */
	@Override
	public final void awaitRunning(Duration timeout) throws TimeoutException {
		Service.super.awaitRunning(timeout);
	}

	/** @since 15.0 */
	@Override
	public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
		this.delegate.awaitRunning(timeout, unit);
	}

	/** @since 15.0 */
	@Override
	public final void awaitTerminated() {
		this.delegate.awaitTerminated();
	}

	/** @since 28.0 */
	@Override
	public final void awaitTerminated(Duration timeout) throws TimeoutException {
		Service.super.awaitTerminated(timeout);
	}

	/** @since 15.0 */
	@Override
	public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
		this.delegate.awaitTerminated(timeout, unit);
	}

	/**
	 * Returns the name of this service. {@link AbstractIdleService} may include the name
	 * in debugging output.
	 *
	 * @since 14.0
	 */
	protected String serviceName() {
		return this.getClass().getSimpleName();
	}

	@WeakOuter
	private final class ThreadNameSupplier implements Supplier<String> {

		@Override
		public String get() {
			return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state();
		}

	}

	@WeakOuter
	private final class DelegateService extends AbstractService {

		@Override
		protected final void doStart() {
			MoreExecutors
					.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier)
					.execute(new Runnable() {
						@Override
						public void run() {
							try {
								AbstractIdleService.this.startUp();
								DelegateService.this.notifyStarted();
							}
							catch (Throwable t) {
								DelegateService.this.notifyFailed(t);
							}
						}
					});
		}

		@Override
		protected final void doStop() {
			MoreExecutors
					.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier)
					.execute(new Runnable() {
						@Override
						public void run() {
							try {
								AbstractIdleService.this.shutDown();
								DelegateService.this.notifyStopped();
							}
							catch (Throwable t) {
								DelegateService.this.notifyFailed(t);
							}
						}
					});
		}

		@Override
		public String toString() {
			return AbstractIdleService.this.toString();
		}

	}

}
