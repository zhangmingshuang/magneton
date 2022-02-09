package org.magneton.module.distributed.cache.redis;

import java.util.List;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
public interface RedisValueSerializer {

	byte[] serialize(Object obj);

	<T> T deserialize(byte[] data, Class<T> clazz);

	<T> List<T> deserialize(List<byte[]> data, Class<T> clazz);

}