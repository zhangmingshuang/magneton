/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.mock.redisson;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.magneton.test.mock.UnsupportedException;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.misc.RedissonPromise;

/**
 * .
 *
 * @author zhangmsh 2021/11/19
 * @since 2.0.3
 */
public class MockRLock implements RLock {

	private final String name;

	private final ReentrantLock lock = new ReentrantLock();

	/**
	 * 当前获取锁的线程
	 */
	private Thread thread;

	public MockRLock(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void lockInterruptibly(long leaseTime, TimeUnit unit) throws InterruptedException {
		this.lock.lockInterruptibly();
	}

	@Override
	public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
		return this.lock.tryLock(waitTime, unit);
	}

	@Override
	public void lock(long leaseTime, TimeUnit unit) {
		this.lock.lock();
	}

	@Override
	public boolean forceUnlock() {
		this.lock.unlock();
		this.clearCurrentThread();
		return true;
	}

	private void clearCurrentThread() {
		this.thread = null;
	}

	@Override
	public boolean isLocked() {
		return this.lock.isLocked();
	}

	@Override
	public boolean isHeldByThread(long threadId) {
		throw UnsupportedException.INSTANCE;
	}

	private long getLockThreadId() {
		return this.thread == null ? -1 : this.thread.getId();
	}

	@Override
	public boolean isHeldByCurrentThread() {
		return this.lock.isHeldByCurrentThread();
	}

	@Override
	public int getHoldCount() {
		return this.lock.getHoldCount();
	}

	@Override
	public long remainTimeToLive() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void lock() {
		this.lock.lock();
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		this.lock.lockInterruptibly();
	}

	@Override
	public boolean tryLock() {
		return this.lock.tryLock();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return this.lock.tryLock(time, unit);
	}

	@Override
	public void unlock() {
		this.lock.unlock();
	}

	@Override
	public Condition newCondition() {
		return this.lock.newCondition();
	}

	@Override
	public RFuture<Boolean> forceUnlockAsync() {
		return RedissonPromise.newSucceededFuture(this.forceUnlock());
	}

	@Override
	public RFuture<Void> unlockAsync() {
		this.unlock();
		return RedissonPromise.newSucceededFuture(null);
	}

	@Override
	public RFuture<Void> unlockAsync(long threadId) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> tryLockAsync() {
		return RedissonPromise.newSucceededFuture(this.tryLock());
	}

	@Override
	public RFuture<Void> lockAsync() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> lockAsync(long threadId) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> lockAsync(long leaseTime, TimeUnit unit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> lockAsync(long leaseTime, TimeUnit unit, long threadId) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> tryLockAsync(long threadId) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> tryLockAsync(long waitTime, TimeUnit unit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> tryLockAsync(long waitTime, long leaseTime, TimeUnit unit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> tryLockAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Integer> getHoldCountAsync() {
		return RedissonPromise.newSucceededFuture(this.getHoldCount());
	}

	@Override
	public RFuture<Boolean> isLockedAsync() {
		return RedissonPromise.newSucceededFuture(this.isLocked());
	}

	@Override
	public RFuture<Long> remainTimeToLiveAsync() {
		return RedissonPromise.newSucceededFuture(this.remainTimeToLive());
	}

}
