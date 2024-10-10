package org.magneton.cache.redis;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundiation.MoreResources;
import org.magneton.foundiation.MoreStrings;
import org.magneton.foundiation.Operation;
import org.magneton.foundiation.RuntimeArgs;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Redisson工厂类，用来创建RedissonClient.
 *
 * @author zhangmsh
 * @since 1.2.0
 */
@Slf4j
public class RedissonFactory {

	public static final String REDIS_ADDRESS = "REDIS_ADDRESS";

	public static final String REDIS_PASSWORD = "REDIS_PASSWORD";

	private RedissonFactory() {
		// ignore
	}

	public static RedissonClient createClient(RedissonClientType redissonClientType) {
		Preconditions.checkNotNull(redissonClientType);
		switch (redissonClientType) {
			case CLUSTER:
				return createClusterServersClient();
			case MASTER_SLAVE:
				return createMasterSlaveServersClient();
			case REPLICATED:
				return createReplicatedServersClient();
			case SENTINEL:
				return createSentinelServersClient();
			case SINGLE:
				return createSingleServerClient();
			default:
				throw new UnsupportedOperationException(
						String.format("redisson %s type unsupported", redissonClientType));
		}
	}

	/**
	 * 获取默认的集群模式设置
	 * <p>
	 * Redis 集群是一个提供在多个Redis间节点间共享数据的程序集。
	 * <p>
	 * Redis集群并不支持处理多个keys的命令,因为这需要在不同的节点间移动数据,从而达不到像Redis那样的性能,在高负载的情况下可能会导致不可预料的错误.
	 * <p>
	 * Redis 集群通过分区来提供一定程度的可用性,在实际环境中当某个节点宕机或者不可达的情况下继续处理命令. Redis 集群的优势:
	 * <ul>
	 * <li>自动分割数据到不同的节点上。</li>
	 * <li>整个集群的部分节点失败或者不可达的情况下能够继续处理命令。</li>
	 * </ul>
	 *
	 * 注意： Redis集群组态的最低要求是必须有三个主节点。
	 * 详见：{@link <a href="http://www.redis.cn/topics/cluster-tutorial.html">官网</a>}
	 * @return 默认的集群模式设置
	 */
	public static Config getDefaultClusterServersConfig() {
		return createConfig("%sredisson-clusterServersConfig.yaml");
	}

	public static RedissonClient createClusterServersClient() {
		return createRedissonClient(getDefaultSingleServerConfig());
	}

	/**
	 * 获取默认的主从模式配置
	 *
	 * <p>
	 * Redis的主从架构，详见 {@link <a href="http://redis.cn/topics/replication.html">官网</a>}
	 * @return 默认的主从模式配置
	 */
	public static Config getDefaultMasterSlaveServersConfig() {
		return createConfig("%sredisson-masterSlaveServersConfig.yaml");
	}

	public static RedissonClient createMasterSlaveServersClient() {
		return createRedissonClient(getDefaultMasterSlaveServersConfig());
	}

	/**
	 * 获取默认的云托管模式配置
	 *
	 * <p>
	 * 云托管模式适用于任何由云计算运营商提供的Redis云服务，包括亚马逊云的AWS ElastiCache、微软云的Azure Redis
	 * 缓存和阿里云（Aliyun）的云数据库Redis版
	 * </p>
	 * @return 默认的云托管模式配置
	 */
	public static Config getDefaultReplicatedServersConfig() {
		return createConfig("%sredisson-replicatedServersConfig.yaml");
	}

	public static RedissonClient createReplicatedServersClient() {
		return createRedissonClient(getDefaultReplicatedServersConfig());
	}

	/**
	 * 获取默认的哨兵模式配置
	 *
	 * <p>
	 * Redis 的 Sentinel 系统用于管理多个 Redis 服务器（instance）
	 * <p>
	 * Redis Sentinel 是一个分布式系统， 你可以在一个架构中运行多个 Sentinel 进程（progress）， 这些进程使用流言协议（gossip
	 * protocols)来接收关于主服务器是否下线的信息， 并使用投票协议（agreement protocols）来决定是否执行自动故障迁移，
	 * 以及选择哪个从服务器作为新的主服务器。
	 * <p>
	 * 祥见：{@link <a href="http://redis.cn/topics/sentinel.html">官网</a>}
	 * @return 默认的哨兵模式配置
	 */
	public static Config getDefaultSentinelServersConfig() {
		return createConfig("%sredisson-sentinelServersConfig.yaml");
	}

	public static RedissonClient createSentinelServersClient() {
		return createRedissonClient(getDefaultSentinelServersConfig());
	}

	/**
	 * 获取默认的单节点模式配置
	 * @return 默认的单节点模式配置
	 */
	public static Config getDefaultSingleServerConfig() {
		Config config = createConfig("%sredisson-singleServerConfig.yaml");
		Operation redisAddress = RuntimeArgs.sys(REDIS_ADDRESS);
		if (!StringUtils.isBlank(redisAddress.get())) {
			SingleServerConfig singleServerConfig = config.useSingleServer();
			singleServerConfig.setAddress(redisAddress.get());
			Operation redisPassword = RuntimeArgs.sys(REDIS_PASSWORD);
			if (!Strings.isNullOrEmpty(redisPassword.get())) {
				singleServerConfig.setPassword(redisPassword.get());
			}
		}
		return config;
	}

	public static RedissonClient createSingleServerClient() {
		return createRedissonClient(getDefaultSingleServerConfig());
	}

	public static RedissonClient createRedissonClient(Config config) {
		return Redisson.create(Preconditions.checkNotNull(config));
	}

	private static Config createConfig(String path) {
		String redissonConfigPrefix = System.getProperty("redisson.config.prefix", "");
		if (Strings.isNullOrEmpty(redissonConfigPrefix)) {
			redissonConfigPrefix = System.getProperty("spring.profiles.active");
		}

		String configFilePath = Strings.lenientFormat(path,
				"classpath:" + MoreStrings.suffixIfNotNullOrEmpty(redissonConfigPrefix, "-"));
		try {
			File file = MoreResources.getFile(configFilePath);
			Config config = Config.fromYAML(file);
			log.info("using redisson config [{}]", configFilePath);
			return config;
		}
		catch (@SuppressWarnings("OverlyBroadCatchBlock") IOException e) {
			log.warn("loading redisson config  [{}] but not found", configFilePath);
			configFilePath = Strings.lenientFormat(path,
					"/" + MoreStrings.suffixIfNotNullOrEmpty(redissonConfigPrefix, "-"));
			try (InputStream inputStream = RedissonFactory.class.getResourceAsStream(configFilePath)) {
				if (inputStream == null) {
					// noinspection ThrowCaughtLocally
					throw new IOException(String.format("file %s not found", configFilePath));
				}
				Config config = Config.fromYAML(inputStream);
				log.info("using redisson config [{}]", configFilePath);
				return config;
			}
			catch (IOException e1) {
				log.warn("loading redisson config  [{}] but not found", configFilePath);

				configFilePath = Strings.lenientFormat(path, "/default-");
				try (InputStream inputStream = RedissonFactory.class.getResourceAsStream(configFilePath)) {
					Config config = Config.fromYAML(inputStream);
					log.info("using redisson config [{}]", configFilePath);
					return config;
				}
				catch (IOException e2) {
					log.warn("loading redisson config  [{}] but not found", configFilePath);
					throw new RedissonConfigParseException(e2);
				}
			}
		}
	}

}