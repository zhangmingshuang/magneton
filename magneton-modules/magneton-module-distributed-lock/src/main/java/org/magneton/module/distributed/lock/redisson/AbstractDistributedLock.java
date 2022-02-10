package org.magneton.module.distributed.lock.redisson;

import org.magneton.module.distributed.lock.DistributedLock;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
public abstract class AbstractDistributedLock implements DistributedLock {

	private static final String LOCK_KEY_PREFIX = "dl_";

	/**
	 * retouch key.
	 * @param key the given lock key.
	 * @return decorated lock key.
	 */
	protected final String retouchKey(String key) {
		return LOCK_KEY_PREFIX + key;
	}

}
