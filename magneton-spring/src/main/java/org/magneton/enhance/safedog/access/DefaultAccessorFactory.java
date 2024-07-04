package org.magneton.enhance.safedog.access;

import lombok.AllArgsConstructor;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Accessor factory.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
@AllArgsConstructor
public class DefaultAccessorFactory implements AccessorFactory {

	private static final ReentrantLock LOCK = new ReentrantLock();

	/**
	 * 访问器处理器.
	 */
	AccessorProcessor accessorProcessor;

	/**
	 * 访问器容器.
	 */
	AccessorContainer accessorContainer;

	@Override
	public Accessor get(String name) {
		Accessor accessor = this.accessorContainer.get(name);
		if (accessor == null) {
			LOCK.lock();
			try {
				accessor = this.accessorContainer.get(name);
				if (accessor == null) {
					accessor = this.accessorProcessor.create(name);
					this.accessorContainer.put(name, accessor);
				}
			}
			finally {
				LOCK.unlock();
			}
		}
		return accessor;
	}

}
