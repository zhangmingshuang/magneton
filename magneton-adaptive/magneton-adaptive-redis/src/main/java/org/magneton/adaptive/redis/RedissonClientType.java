package org.magneton.adaptive.redis;

/**
 * @author zhangmsh 2022/3/27
 * @since 1.0.0
 */
public enum RedissonClientType {

	CLUSTER,

	MASTER_SLAVE,

	REPLICATED,

	SENTINEL,

	SINGLE

}
