package org.magneton.framework.core.safedog.access;

import lombok.AllArgsConstructor;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Accessor factory.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
@AllArgsConstructor
public class DefaultSafeDogAccessorFactory implements SafeDogAccessorFactory {

	private static final ReentrantLock LOCK = new ReentrantLock();

	/**
	 * 访问器处理器.
	 */
	SafeDogAccessorProcessor safeDogAccessorProcessor;

	/**
	 * 访问器容器.
	 */
	AccessorContainer accessorContainer;

	@Override
	public SafeDogAccessor get(String name) {
		SafeDogAccessor safeDogAccessor = this.accessorContainer.get(name);
		if (safeDogAccessor == null) {
			LOCK.lock();
			try {
				safeDogAccessor = this.accessorContainer.get(name);
				if (safeDogAccessor == null) {
					safeDogAccessor = this.safeDogAccessorProcessor.create(name);
					this.accessorContainer.put(name, safeDogAccessor);
				}
			}
			finally {
				LOCK.unlock();
			}
		}
		return safeDogAccessor;
	}

}
