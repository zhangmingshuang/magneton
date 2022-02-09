package org.magneton.module.distributed.cache.redis;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.magneton.core.base.Objects;
import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.Collections;
import org.magneton.core.collect.Lists;
import org.magneton.core.collect.Maps;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
public class JSONRedisValueSerializer implements RedisValueSerializer {

	@Override
	public byte[] serialize(Object obj) {
		Object data = Objects.nullable(obj);
		if (data instanceof CharSequence) {
			return ((CharSequence) data).toString().getBytes(StandardCharsets.UTF_8);
		}
		return JSON.toJSONBytes(data);
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T> clazz) {
		return JSON.parseObject(Preconditions.checkNotNull(data), Preconditions.checkNotNull(clazz));
	}

	@Override
	public <T> List<T> deserialize(List<byte[]> data, Class<T> clazz) {
		if (Collections.isNullOrEmpty(data)) {
			return Collections.emptyList();
		}
		List<T> list = Lists.newArrayListWithCapacity(data.size());
		for (byte[] bytes : data) {
			list.add(JSON.parseObject(bytes, clazz));
		}
		return list;
	}

	@Override
	public <V> Map<String, V> deserialize(Map<byte[], byte[]> data, Class<V> clazz) {
		if (Maps.isNullOrEmpty(data)) {
			return Collections.emptyMap();
		}
		Map<String, V> map = Maps.newHashMapWithExpectedSize(data.size());
		data.forEach((k, v) -> {
			map.put(new String(k), JSON.parseObject(v, clazz));
		});
		return map;
	}

}
