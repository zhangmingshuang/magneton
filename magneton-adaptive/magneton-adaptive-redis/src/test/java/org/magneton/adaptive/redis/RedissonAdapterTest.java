package org.magneton.adaptive.redis;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.reflect.Reflection;
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
		Method method = Reflection.findMethod(Config.class, "getClusterServersConfig");
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

}