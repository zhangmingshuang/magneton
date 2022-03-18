package org.magneton.adaptive.redis;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.magneton.core.base.Preconditions;
import org.magneton.core.base.Strings;
import org.magneton.foundation.resource.Resources;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * .
 *
 * @author zhangmsh 2022/2/10
 * @since 1.2.0
 */
@Slf4j
public class RedissonAdapter {

	private RedissonAdapter() {

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
		return createConfig("%sredisson-singleServerConfig.yaml");
	}

	public static RedissonClient createSingleServerClient() {
		return createRedissonClient(getDefaultSingleServerConfig());
	}

	public static RedissonClient createRedissonClient(Config config) {
		return Redisson.create(Preconditions.checkNotNull(config));
	}

	private static Config createConfig(String path) {
		try {
			File file = Resources.getFile(Strings.lenientFormat(path, ""));
			return Config.fromYAML(file);
		}
		catch (IOException e) {
			try {
				File file = Resources.getFile(Strings.lenientFormat(path, "classpath:adaptive-"));
				return Config.fromYAML(file);
			}
			catch (IOException e1) {
				throw new RedissonConfigParseException(e1);
			}
		}
	}

}
