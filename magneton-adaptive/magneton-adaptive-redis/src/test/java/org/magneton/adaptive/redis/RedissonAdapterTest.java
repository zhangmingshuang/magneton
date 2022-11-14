package org.magneton.adaptive.redis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.foundation.reflect.MoreReflection;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;

/**
 * .
 *
 * @author zhangmsh 2022/2/10
 * @since 1.2.0
 */
class RedissonAdapterTest {

	@Test
	void test_ClusterServers() throws IOException, InvocationTargetException, IllegalAccessException {
		Config config = RedissonAdapter.getDefaultClusterServersConfig();
		int threads = config.getThreads();
		Assertions.assertEquals(0, threads);
		Method method = MoreReflection.findMethod(Config.class, "getClusterServersConfig");
		method.setAccessible(true);
		ClusterServersConfig invoke = (ClusterServersConfig) method.invoke(config);
		System.out.println(invoke);
		List<String> nodeAddresses = invoke.getNodeAddresses();
		Assertions.assertEquals("[redis://127.0.0.1:7002, redis://127.0.0.1:7001, redis://127.0.0.1:7000]",
				nodeAddresses.toString());
	}

	@Test
	void test_masterSlaveServers() throws IOException {
		Config config = RedissonAdapter.getDefaultMasterSlaveServersConfig();
		Assertions.assertNotNull(config);
	}

	@Test
	void test_replicatedServersConfig() throws IOException {
		Config config = RedissonAdapter.getDefaultReplicatedServersConfig();
		Assertions.assertNotNull(config);
	}

	@Test
	void test_sentinelServersConfig() throws IOException {
		Config config = RedissonAdapter.getDefaultSentinelServersConfig();
		Assertions.assertNotNull(config);
	}

	@Test
	void test_singleServerConfig() throws IOException {
		Config config = RedissonAdapter.getDefaultSingleServerConfig();
		Assertions.assertNotNull(config);
	}

	@Test
	void testRate() throws InterruptedException {
		RedissonClient client = RedissonAdapter.createSingleServerClient();
		RRateLimiter test = client.getRateLimiter("test");
		test.trySetRate(RateType.OVERALL, 2, 1, RateIntervalUnit.SECONDS);
		// 每秒2次，即500ms一次
		int count = 10;
		CountDownLatch cdl = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			new Thread(() -> {
				System.out.println(test.tryAcquire());
				cdl.countDown();
			}).start();
		}
		cdl.await();
		System.out.println("finish");
	}

	@Test
	@Ignore
	void test() throws InterruptedException {
		RedissonClient client = RedissonAdapter.createSingleServerClient();
		String key = "testKey";

		RLock lock = client.getLock(key);
		lock.lock();
		Thread t1 = new Thread() {
			@Override
			public void run() {
				try {
					RLock lock1 = client.getLock(key);
					lock1.lock();
				}
				finally {
					RLock lock1 = client.getLock(key);
					lock1.unlock();
				}
			}
		};
		t1.start();
		t1.join();
		lock.unlock();
	}

}