package org.magneton.module.statistics.pvpu;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public class RedissonPvUvProcessor implements PvUvProcessor {

	private static final String KEY = "magneton:m:stat";

	private final RedissonClient redissonClient;

	public RedissonPvUvProcessor(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public boolean isUv(String group, int id, long timeToLiveSeconds) {
		Preconditions.checkNotNull(group, "group is must not be null");

		RBitSet bitSet = this.redissonClient.getBitSet(this.theKey(group));
		if (timeToLiveSeconds > 0 && !bitSet.isExists()) {
			bitSet.expire(timeToLiveSeconds, TimeUnit.SECONDS);
		}
		return !bitSet.set(id);
	}

	/**
	 * 清理数据
	 * @param group 组
	 */
	@VisibleForTesting
	public void clean(String group) {
		this.redissonClient.getBitSet(this.theKey(group)).delete();
	}

	/**
	 * 获取key
	 * @param suffix 后缀，用于统一的正确的Key组装
	 * @return 预期的key
	 */
	private String theKey(String suffix) {
		return KEY + ":uv:" + suffix;
	}

}
