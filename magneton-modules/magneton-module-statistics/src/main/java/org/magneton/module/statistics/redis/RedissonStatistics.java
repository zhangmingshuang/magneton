package org.magneton.module.statistics.redis;

import java.util.concurrent.TimeUnit;

import javax.annotations.VisibleForTesting;

import org.magneton.core.base.Preconditions;
import org.magneton.module.statistics.Statistics;
import org.magneton.module.statistics.process.PvUv;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public class RedissonStatistics implements Statistics, PvUv {

	private static final String KEY = "magneton:m:stat";

	private final RedissonClient redissonClient;

	public RedissonStatistics(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public PvUv pvUv() {
		return this;
	}

	@Override
	public boolean isUv(String group, int id, long timeToLiveSeconds) {
		Preconditions.checkNotNull(group);
		RBitSet bitSet = this.redissonClient.getBitSet(KEY + ":uv:" + group);
		if (timeToLiveSeconds > 0 && !bitSet.isExists()) {
			bitSet.expire(timeToLiveSeconds, TimeUnit.SECONDS);
		}
		return !bitSet.set(id);
	}

	@VisibleForTesting
	public void clean(String group) {
		this.redissonClient.getBitSet(KEY + ":uv:" + group).delete();
	}

}
