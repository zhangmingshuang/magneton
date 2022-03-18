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

import java.util.concurrent.Callable;

import org.magneton.core.base.Supplier;

import static org.magneton.core.base.Preconditions.checkNotNull;

/**
 * Static utility methods pertaining to the {@link Callable} interface.
 *
 * @author Isaac Shum
 * @since 1.0
 */

@ElementTypesAreNonnullByDefault
public final class Callables {

	private Callables() {
	}

	/**
	 * Creates a {@code Callable} which immediately returns a preset value each time it is
	 * called.
	 */
	public static <T> Callable<T> returning(@ParametricNullness T value) {
		return () -> value;
	}

	/**
	 * Creates an {@link AsyncCallable} from a {@link Callable}.
	 *
	 * <p>
	 * The {@link AsyncCallable} returns the {@link ListenableFuture} resulting from
	 * {@link ListeningExecutorService#submit(Callable)}.
	 *
	 * @since 20.0
	 */

	public static <T> AsyncCallable<T> asAsyncCallable(Callable<T> callable,
			ListeningExecutorService listeningExecutorService) {
		checkNotNull(callable);
		checkNotNull(listeningExecutorService);
		return () -> listeningExecutorService.submit(callable);
	}

	/**
	 * Wraps the given callable such that for the duration of {@link Callable#call} the
	 * thread that is running will have the given name.
	 * @param callable The callable to wrap
	 * @param nameSupplier The supplier of thread names, {@link Supplier#get get} will be
	 * called once for each invocation of the wrapped callable.
	 */

	static <T> Callable<T> threadRenaming(Callable<T> callable, Supplier<String> nameSupplier) {
		checkNotNull(nameSupplier);
		checkNotNull(callable);
		return () -> {
			Thread currentThread = Thread.currentThread();
			String oldName = currentThread.getName();
			boolean restoreName = trySetName(nameSupplier.get(), currentThread);
			try {
				return callable.call();
			}
			finally {
				if (restoreName) {
					boolean unused = trySetName(oldName, currentThread);
				}
			}
		};
	}

	/**
	 * Wraps the given runnable such that for the duration of {@link Runnable#run} the
	 * thread that is running with have the given name.
	 * @param task The Runnable to wrap
	 * @param nameSupplier The supplier of thread names, {@link Supplier#get get} will be
	 * called once for each invocation of the wrapped callable.
	 */

	static Runnable threadRenaming(Runnable task, Supplier<String> nameSupplier) {
		checkNotNull(nameSupplier);
		checkNotNull(task);
		return () -> {
			Thread currentThread = Thread.currentThread();
			String oldName = currentThread.getName();
			boolean restoreName = trySetName(nameSupplier.get(), currentThread);
			try {
				task.run();
			}
			finally {
				if (restoreName) {
					boolean unused = trySetName(oldName, currentThread);
				}
			}
		};
	}

	/** Tries to set name of the given {@link Thread}, returns true if successful. */

	private static boolean trySetName(String threadName, Thread currentThread) {
		/*
		 * setName should usually succeed, but the security manager can prohibit it. Is
		 * there a way to see if we have the modifyThread permission without catching an
		 * exception?
		 */
		try {
			currentThread.setName(threadName);
			return true;
		}
		catch (SecurityException e) {
			return false;
		}
	}

}
