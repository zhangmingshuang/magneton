package org.magneton.module.distributed.lock.redis;

import org.magneton.module.distributed.lock.DistributedLock;

/**
 * 分布式锁抽象类.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
public abstract class AbstractDistributedLock implements DistributedLock {

	private static final String LOCK_KEY_PREFIX = "magneton:dl:";

	/**
	 * retouch key.
	 * @param key the given lock key.
	 * @return decorated lock key.
	 */
	protected final String retouchKey(String key) {
		return LOCK_KEY_PREFIX + key;
	}

}
