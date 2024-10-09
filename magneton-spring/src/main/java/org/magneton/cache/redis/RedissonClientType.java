package org.magneton.cache.redis;

/**
 * @author zhangmsh 2022/3/27
 * @since 1.0.0
 */
public enum RedissonClientType {

	/**
	 * 集群模式
	 */
	CLUSTER,
	/**
	 * 主从模式
	 */
	MASTER_SLAVE,
	/**
	 * 云托管模式
	 */
	REPLICATED,
	/**
	 * 哨兵模式
	 */
	SENTINEL,
	/**
	 * 单节点模式
	 */
	SINGLE

}
