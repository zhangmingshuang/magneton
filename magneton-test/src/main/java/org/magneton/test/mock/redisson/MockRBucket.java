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

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.magneton.test.mock.UnsupportedException;
import org.redisson.api.ObjectListener;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.client.codec.Codec;
import org.redisson.misc.RedissonPromise;

/**
 * .
 *
 * @author zhangmsh 2021/11/17
 * @since 2.0.3
 */
public class MockRBucket<V> implements RBucket<V> {

	/**
	 * RBucket的名字
	 */
	private final String name;

	private V value;

	private long expire = Ttl.NON_EXIST;

	public MockRBucket(String name) {
		this.name = name;
	}

	@Override
	public long size() {
		return this.value != null ? this.value.toString().getBytes(StandardCharsets.UTF_8).length : 0;
	}

	@Override
	public V get() {
		return this.value;
	}

	@Override
	public V getAndDelete() {
		V v = this.value;
		this.value = null;
		return v;
	}

	@Override
	public boolean trySet(V value) {
		if (this.value == null) {
			this.value = value;
			this.expire = Ttl.NON_EXPIRE;
			return true;
		}
		return false;
	}

	@Override
	public boolean trySet(V value, long timeToLive, TimeUnit timeUnit) {
		long expireTime = timeUnit.toMillis(timeToLive);
		this.expire = System.currentTimeMillis() + expireTime;
		return this.trySet(value);
	}

	@Override
	public boolean setIfExists(V value) {
		if (this.value == null) {
			return false;
		}
		this.value = value;
		return true;
	}

	@Override
	public boolean setIfExists(V value, long timeToLive, TimeUnit timeUnit) {
		long expireTime = timeUnit.toMillis(timeToLive);
		this.expire = System.currentTimeMillis() + expireTime;
		return this.setIfExists(value);
	}

	@Override
	public boolean compareAndSet(V expect, V update) {
		if (Objects.equals(this.value, expect)) {
			this.value = update;
			return true;
		}
		return false;
	}

	@Override
	public V getAndSet(V newValue) {
		V v = this.value;
		this.value = newValue;
		return v;
	}

	@Override
	public V getAndSet(V value, long timeToLive, TimeUnit timeUnit) {
		long expireTime = timeUnit.toMillis(timeToLive);
		this.expire = System.currentTimeMillis() + expireTime;
		return this.getAndSet(value);
	}

	@Override
	public void set(V value) {
		this.value = value;
	}

	@Override
	public void set(V value, long timeToLive, TimeUnit timeUnit) {
		long expireTime = timeUnit.toMillis(timeToLive);
		this.expire = System.currentTimeMillis() + expireTime;
		this.set(value);
	}

	@Override
	public void setAndKeepTTL(V value) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public Long getIdleTime() {
		long ttl = this.expire - System.currentTimeMillis();
		return ttl < 0 ? Ttl.NON_EXIST : ttl / 1000;
	}

	@Override
	public long sizeInMemory() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void restore(byte[] state) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void restore(byte[] state, long timeToLive, TimeUnit timeUnit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void restoreAndReplace(byte[] state) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void restoreAndReplace(byte[] state, long timeToLive, TimeUnit timeUnit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public byte[] dump() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public boolean touch() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void migrate(String host, int port, int database, long timeout) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void copy(String host, int port, int database, long timeout) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public boolean move(int database) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean delete() {
		this.value = null;
		return true;
	}

	@Override
	public boolean unlink() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void rename(String newName) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public boolean renamenx(String newName) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public boolean isExists() {
		return this.value != null;
	}

	@Override
	public Codec getCodec() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public int addListener(ObjectListener listener) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public void removeListener(int listenerId) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Long> sizeAsync() {
		return RedissonPromise.newSucceededFuture(this.size());
	}

	@Override
	public RFuture<V> getAsync() {
		return RedissonPromise.newSucceededFuture(this.get());
	}

	@Override
	public RFuture<V> getAndDeleteAsync() {
		return RedissonPromise.newSucceededFuture(this.getAndDelete());
	}

	@Override
	public RFuture<Boolean> trySetAsync(V value) {
		return RedissonPromise.newSucceededFuture(this.trySet(value));
	}

	@Override
	public RFuture<Boolean> trySetAsync(V value, long timeToLive, TimeUnit timeUnit) {
		return RedissonPromise.newSucceededFuture(this.trySet(value, timeToLive, timeUnit));
	}

	@Override
	public RFuture<Boolean> setIfExistsAsync(V value) {
		return RedissonPromise.newSucceededFuture(this.setIfExists(value));
	}

	@Override
	public RFuture<Boolean> setIfExistsAsync(V value, long timeToLive, TimeUnit timeUnit) {
		return RedissonPromise.newSucceededFuture(this.setIfExists(value, timeToLive, timeUnit));
	}

	@Override
	public RFuture<Boolean> compareAndSetAsync(V expect, V update) {
		return RedissonPromise.newSucceededFuture(this.compareAndSet(expect, update));
	}

	@Override
	public RFuture<V> getAndSetAsync(V newValue) {
		return RedissonPromise.newSucceededFuture(this.getAndSet(newValue));
	}

	@Override
	public RFuture<V> getAndSetAsync(V value, long timeToLive, TimeUnit timeUnit) {
		return RedissonPromise.newSucceededFuture(this.getAndSet(value, timeToLive, timeUnit));
	}

	@Override
	public RFuture<Void> setAsync(V value) {
		this.set(value);
		return RedissonPromise.newSucceededFuture(null);
	}

	@Override
	public RFuture<Void> setAsync(V value, long timeToLive, TimeUnit timeUnit) {
		this.set(value, timeToLive, timeUnit);
		return RedissonPromise.newSucceededFuture(null);
	}

	@Override
	public RFuture<Void> setAndKeepTTLAsync(V value) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public boolean expire(long timeToLive, TimeUnit timeUnit) {
		if (this.value == null) {
			return false;
		}
		long expireTime = timeUnit.toMillis(timeToLive);
		this.expire = System.currentTimeMillis() + expireTime;
		return true;
	}

	@Override
	public boolean expireAt(long timestamp) {
		if (this.value == null) {
			return false;
		}
		this.expire = timestamp;
		return true;
	}

	@Override
	public boolean expireAt(Date timestamp) {
		if (this.value == null) {
			return false;
		}
		this.expire = timestamp.getTime();
		return true;
	}

	@Override
	public boolean expire(Instant instant) {
		if (this.value == null) {
			return false;
		}
		this.expire = instant.toEpochMilli();
		return true;
	}

	@Override
	public boolean clearExpire() {
		if (this.expire < 0) {
			return false;
		}
		this.expire = Ttl.NON_EXPIRE;
		return true;
	}

	@Override
	public long remainTimeToLive() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> expireAsync(long timeToLive, TimeUnit timeUnit) {
		return RedissonPromise.newSucceededFuture(this.expire(timeToLive, timeUnit));
	}

	@Override
	public RFuture<Boolean> expireAtAsync(Date timestamp) {
		return RedissonPromise.newSucceededFuture(this.expireAt(timestamp));
	}

	@Override
	public RFuture<Boolean> expireAtAsync(long timestamp) {
		return RedissonPromise.newSucceededFuture(this.expireAt(timestamp));
	}

	@Override
	public RFuture<Boolean> expireAsync(Instant instant) {
		return RedissonPromise.newSucceededFuture(this.expire(instant));
	}

	@Override
	public RFuture<Boolean> clearExpireAsync() {
		return RedissonPromise.newSucceededFuture(this.clearExpire());
	}

	@Override
	public RFuture<Long> remainTimeToLiveAsync() {
		return RedissonPromise.newSucceededFuture(this.remainTimeToLive());
	}

	@Override
	public RFuture<Long> getIdleTimeAsync() {
		return RedissonPromise.newSucceededFuture(this.getIdleTime());
	}

	@Override
	public RFuture<Long> sizeInMemoryAsync() {
		return RedissonPromise.newSucceededFuture(this.sizeInMemory());
	}

	@Override
	public RFuture<Void> restoreAsync(byte[] state) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> restoreAsync(byte[] state, long timeToLive, TimeUnit timeUnit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> restoreAndReplaceAsync(byte[] state) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> restoreAndReplaceAsync(byte[] state, long timeToLive, TimeUnit timeUnit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<byte[]> dumpAsync() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> touchAsync() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> migrateAsync(String host, int port, int database, long timeout) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> copyAsync(String host, int port, int database, long timeout) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> moveAsync(int database) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> deleteAsync() {
		return RedissonPromise.newSucceededFuture(this.delete());
	}

	@Override
	public RFuture<Boolean> unlinkAsync() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> renameAsync(String newName) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> renamenxAsync(String newName) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> isExistsAsync() {
		return RedissonPromise.newSucceededFuture(this.isExists());
	}

	@Override
	public RFuture<Integer> addListenerAsync(ObjectListener listener) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> removeListenerAsync(int listenerId) {
		throw UnsupportedException.INSTANCE;
	}

}
