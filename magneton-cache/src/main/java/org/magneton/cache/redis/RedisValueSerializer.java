package org.magneton.cache.redis;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
public interface RedisValueSerializer {

  byte[] serialize(Object obj);

  <T> T deserialize(byte[] data, Class<T> clazz);
}
