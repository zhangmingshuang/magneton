package org.magneton.module.distributed.lock.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.distributed.lock.DistributedLock;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
class RedissonDistributedLockTest extends TestRedisson {

	private final DistributedLock lock = new RedissonDistributedLock(redissonClient);

	@Test
	void getLock() throws InterruptedException {
		Lock test = this.lock.getLock("getLock");
		Assertions.assertTrue(test.tryLock(3, TimeUnit.SECONDS));
		test.unlock();
	}

	@Test
	void lock() throws InterruptedException {
		this.lock.lock("lock");
		AtomicInteger i = new AtomicInteger(1);
		Thread t = new Thread(() -> {
			RedissonDistributedLockTest.this.lock.lock("lock");
			Assertions.assertEquals(0, i.get());
			i.decrementAndGet();
		});
		t.start();
		Thread.sleep(200);
		i.decrementAndGet();
		this.lock.unlock("lock");
		t.join();
		Assertions.assertEquals(-1, i.get());
	}

	@Test
	void tryLock() throws InterruptedException {
		this.lock.lock("tryLock");
		Thread t = new Thread(() -> {
			boolean tryLock = this.lock.tryLock("tryLock");
			Assertions.assertFalse(tryLock);
		});
		t.start();
		t.join();

		this.lock.unlock("tryLock");
		Thread t1 = new Thread(() -> {
			boolean tryLock = this.lock.tryLock("tryLock");
			Assertions.assertTrue(tryLock);
		});
		t1.start();
		t1.join();
	}

}