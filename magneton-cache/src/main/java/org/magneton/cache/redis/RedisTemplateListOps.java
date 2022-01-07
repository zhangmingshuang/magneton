package org.magneton.cache.redis;

import java.util.Collections;
import java.util.List;
import org.magneton.cache.ops.ListOps;
import org.magneton.cache.util.Datas;
import org.magneton.cache.util.Trans;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SuppressWarnings("ConstantConditions")
public class RedisTemplateListOps extends AbstractRedisTemplateOps implements ListOps {

	public RedisTemplateListOps(RedisTemplate redisTemplate, RedisValueSerializer redisValueSerializer) {
		super(redisTemplate, redisValueSerializer);
	}

	@Override
	public long add(String list, List<String> values) {
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.rPush(Trans.toByte(list), this.serialize(values)));
	}

	@Override
	public long addAtHead(String list, List<String> values) {
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lPush(Trans.toByte(list), this.serializeToArray(values)));
	}

	@Override
	public List<String> range(String list, long start, long end) {
		List<byte[]> response = (List<byte[]>) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lRange(Trans.toByte(list), start, end));

		return Datas.isEmpty(response) ? Collections.emptyList() : Trans.toStr(response);
	}

	@Override
	public long size(String list) {
		return (long) this.redisTemplate.execute((RedisCallback) rc -> rc.lLen(Trans.toByte(list)));
	}

	@Override
	public String get(String list, long index) {
		byte[] response = (byte[]) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lIndex(Trans.toByte(list), index));
		return Datas.isEmpty(response) ? null : Trans.toStr(response);
	}

	@Override
	public long remove(String list, String value) {
		return (long) this.redisTemplate
				.execute((RedisCallback) rc -> rc.lRem(Trans.toByte(list), 0, Trans.toByte(value)));
	}

}
