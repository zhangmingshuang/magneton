package org.magneton.adaptive.redis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.foundation.reflect.MoreReflection;
import org.redisson.api.RLock;
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

		// ExecutorService executorService = Executors.newCachedThreadPool();
		// long c = System.currentTimeMillis();
		// AtomicInteger i = new AtomicInteger();
		// StringBuffer buffer = new StringBuffer(1024);
		// int count = 200;
		// CountDownLatch countDownLatch = new CountDownLatch(count);
		// executorService.submit(() -> {
		// long id = Thread.currentThread().getId();
		// try {
		// Thread.sleep(100);
		// buffer.append("t" + id + "start\n");
		// RLock lock = client.getLock(key);
		// lock.unlock();
		// buffer.append("t" + id + "lock: " + lock + "\n");
		// }
		// catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// finally {
		// RLock lock = client.getLock(key);
		// buffer.append("t " + id + " unlock:" + lock + ", " +
		// lock.isHeldByCurrentThread() + "\n");
		// lock.unlock();
		//
		// countDownLatch.countDown();
		// }
		// });
		// System.out.println(buffer.toString());
	}

}