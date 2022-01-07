package org.magneton.cache.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author zhangmsh 2021/6/25
 * @since 1.0.0
 */
@Component
public class JsonRedisSerializer implements RedisValueSerializer {

	@Override
	public byte[] serialize(Object obj) {
		return JSON.toJSONBytes(obj);
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> clazz) {
		return JSON.parseObject(data, clazz);
	}

}
