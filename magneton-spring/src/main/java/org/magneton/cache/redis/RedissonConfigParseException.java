package org.magneton.cache.redis;

/**
 * Redisson配置解析异常.
 *
 * @author zhangmsh
 * @since 1.2.0
 */
public class RedissonConfigParseException extends RuntimeException {

	public RedissonConfigParseException(Throwable e) {
		super(e);
	}

}