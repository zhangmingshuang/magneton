package org.magneton.module.distributed.lock;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 分布式锁.
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/7
 */
public interface DistributedLock {

	/**
	 * 获取一个锁
	 * @param key Key
	 * @return 对应的锁
	 */
	Lock getLock(String key);

	/**
	 * 加琐
	 * @param key 要加琐的资源Key
	 */
	void lock(String key);

	/**
	 * 尝试加琐
	 * @param key 要加琐的资源Key
	 * @param duration 获取锁的等待时间，等待时间内无法获取锁则放弃加琐操作
	 * @return 是否成功加琐
	 */
	default boolean tryLock(String key, Duration duration) {
		return this.tryLock(key, duration.getSeconds(), TimeUnit.SECONDS);
	}

	/**
	 * 尝试加琐
	 * @param key 要加琐的资源Key
	 * @param instant 获取锁的等待时间，等待时间内无法获取锁则放弃加琐操作
	 * @return 是否成功加琐
	 */
	default boolean tryLock(String key, Instant instant) {
		return this.tryLock(key, instant.getEpochSecond(), TimeUnit.SECONDS);
	}

	/**
	 * 尝试加琐
	 * @param key 要加琐的资源Key
	 * @param time 获取锁的等待时间，等待时间内无法获取锁则放弃加琐操作
	 * @param unit 获取锁的等待时间单位
	 * @return 是否成功加琐
	 */
	boolean tryLock(String key, long time, TimeUnit unit);

	/**
	 * 尝试加琐
	 * @param key 要加琐的资源Key
	 * @return 是否成功加琐
	 */
	boolean tryLock(String key);

	/**
	 * 解琐
	 * @param key 要解琐的资源Key
	 */
	void unlock(String key);

}
