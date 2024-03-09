package org.magneton.module.distributed.lock.redis;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.magneton.module.distributed.lock.exception.LockException;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * {@code Redisson} distributed lock that relies on {@code Redis}.
 *
 * <p>
 * 1. create the redisson client
 *
 * <pre>{@code
 * Config config = new Config();
 * config.useSingleServer().setAddress("redis://xxxx:xxxx").setPassword("xxxxxx");
 * RedissonClient redissonClient = Redisson.create(config);
 * }</pre>
 *
 * 2. using redis distribute lock
 *
 * <pre>{@code
 * DistributedLock lock = new RedisDistributedLock(client,"lock_key");
 * lock.lock();
 * try {
 *   ...
 * } finally {
 *   lock.unLock();
 * }
 * }</pre>
 *
 * @author zhangmsh
 * @since 2021/1/7
 */
public class RedissonDistributedLock extends AbstractDistributedLock {

	private final RedissonClient redissonClient;

	public RedissonDistributedLock(RedissonClient redissonClient) {
		Preconditions.checkNotNull(redissonClient);
		this.redissonClient = redissonClient;
	}

	@Override
	public Lock getLock(String key) {
		Preconditions.checkNotNull(key);
		return this.redissonClient.getLock(this.retouchKey(key));
	}

	@Override
	public void lock(String key) {
		Preconditions.checkNotNull(key);
		this.getLock(this.retouchKey(key)).lock();
	}

	@Override
	public boolean tryLock(String key, long time, TimeUnit unit) {
		Preconditions.checkNotNull(key);
		try {
			return this.getLock(this.retouchKey(key)).tryLock(time, unit);
		}
		catch (InterruptedException e) {
			throw new LockException(Strings.lenientFormat("try lock %s key interrupted", key), e);
		}
	}

	@Override
	public boolean tryLock(String key) {
		Preconditions.checkNotNull(key);
		return this.getLock(this.retouchKey(key)).tryLock();
	}

	@Override
	public void unlock(String key) {
		Preconditions.checkNotNull(key);
		this.getLock(this.retouchKey(key)).unlock();
	}

}