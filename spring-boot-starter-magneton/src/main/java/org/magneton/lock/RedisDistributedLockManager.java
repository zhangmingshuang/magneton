package org.magneton.lock;

import com.google.common.base.Preconditions;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
public class RedisDistributedLockManager implements LockManager {

	private final RedissonClient redissonClient;

	public RedisDistributedLockManager(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public DistributedLock getLock(String key) {
		Preconditions.checkNotNull(key, "key must be not null");
		return new RedisDistributedLock(this.redissonClient, key);
	}

}
