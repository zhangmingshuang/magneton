package org.magneton.test.mock.redisson;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.magneton.test.mock.UnsupportedException;
import org.redisson.api.ObjectListener;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RFuture;
import org.redisson.client.codec.Codec;
import org.redisson.misc.RedissonPromise;

/**
 * .
 *
 * @author zhangmsh 2021/11/19
 * @since 2.0.3
 */
public class MockRCountDownLatch implements RCountDownLatch {

	private final String name;

	private CountDownLatch countDownLatch;

	public MockRCountDownLatch(String name) {
		this.name = name;
	}

	@Override
	public void await() throws InterruptedException {
		if (countDownLatch == null) {
			return;
		}
		countDownLatch.await();
	}

	@Override
	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		if (countDownLatch == null) {
			return false;
		}
		return countDownLatch.await(timeout, unit);
	}

	@Override
	public void countDown() {
		if (countDownLatch == null) {
			return;
		}
		countDownLatch.countDown();
	}

	@Override
	public long getCount() {
		if (countDownLatch == null) {
			return 0;
		}
		return countDownLatch.getCount();
	}

	@Override
	public boolean trySetCount(long count) {
		Preconditions.checkArgument(count > 0 && count < Integer.MAX_VALUE, "count必须大于0小于Int最大值");
		if (countDownLatch == null) {
			countDownLatch = new CountDownLatch((int) count);
		}
		else if (countDownLatch.getCount() != 0) {
			return false;
		}
		countDownLatch = new CountDownLatch((int) count);
		return true;
	}

	@Override
	public RFuture<Void> awaitAsync() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Boolean> awaitAsync(long waitTime, TimeUnit unit) {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Void> countDownAsync() {
		countDown();
		return RedissonPromise.newSucceededFuture(null);
	}

	@Override
	public RFuture<Long> getCountAsync() {
		return RedissonPromise.newSucceededFuture(getCount());
	}

	@Override
	public RFuture<Boolean> trySetCountAsync(long count) {
		return RedissonPromise.newSucceededFuture(trySetCount(count));
	}

	@Override
	public Long getIdleTime() {
		throw UnsupportedException.INSTANCE;
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
		return name;
	}

	@Override
	public boolean delete() {
		countDownLatch = null;
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
		return countDownLatch != null;
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
	public RFuture<Long> getIdleTimeAsync() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RFuture<Long> sizeInMemoryAsync() {
		throw UnsupportedException.INSTANCE;
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
		return RedissonPromise.newSucceededFuture(delete());
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
		return RedissonPromise.newSucceededFuture(isExists());
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
