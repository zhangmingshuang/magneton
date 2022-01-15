package org.magneton.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/11
 */
class RedisDistributedLocakTest {

	private static final String LOCK_KEY = "lock_key";

	private static DistributedLock distributedLock = null;

	private static RedissonClient redissonClient = null;

	@BeforeAll
	static void beforeAll() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		redissonClient = Redisson.create(config);
		distributedLock = new RedissonDistributedLock(redissonClient, LOCK_KEY);
	}

	@AfterAll
	static void afterAll() {
		redissonClient.shutdown();
	}

	@Test
	void lockAndUnLock() throws InterruptedException {
		int count = 10;
		CountDownLatch cdl = new CountDownLatch(count);
		int[] sequenceId = { 0 };
		for (int i1 = 0; i1 < count; i1++) {
			new Thread(() -> {
				distributedLock.lock();
				try {
					sequenceId[0]++;
				}
				finally {
					distributedLock.unlock();
					cdl.countDown();
				}
			}).start();
		}
		cdl.await();
		Assertions.assertEquals(count, sequenceId[0], "sequence error");
	}

	@Test
	void testLockInterruptibly() {
		distributedLock.lock();
		try {
			Thread t = new Thread(() -> Assertions.assertThrows(InterruptedException.class,
					() -> distributedLock.lockInterruptibly()));
			t.start();
			Thread.sleep(20);
			t.interrupt();
			Thread.sleep(30);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			Assertions.fail("should not enter this process");
		}
		finally {
			distributedLock.unlock();
		}
	}

	@Test
	void testTryLock() {
		distributedLock.lock();
		try {
			Thread t = new Thread(() -> {
				boolean b = distributedLock.tryLock();
				Assertions.assertFalse(b, "should be not locked");
			});
			t.start();
			t.join();
		}
		catch (InterruptedException e) {
			Assertions.fail("should not enter this process");
		}
		finally {
			distributedLock.unlock();
		}
	}

	@Test
	void testTryLockTimeout() {
		distributedLock.lock();
		try {
			Thread t = new Thread(() -> {
				boolean b = false;
				try {
					b = distributedLock.tryLock(200, TimeUnit.MICROSECONDS);
				}
				catch (InterruptedException e) {
					Assertions.fail("should not enter this process");
				}
				Assertions.assertFalse(b, "should be not locked");
			});
			t.start();
			t.join();
		}
		catch (InterruptedException e) {
			Assertions.fail("should not enter this process");
		}
		finally {
			distributedLock.unlock();
		}
	}

	@Test
	void testConditionWithoutLock() {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> distributedLock.newCondition());
	}

}
