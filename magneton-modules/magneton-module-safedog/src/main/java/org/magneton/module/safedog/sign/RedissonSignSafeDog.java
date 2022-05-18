package org.magneton.module.safedog.sign;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.magneton.core.base.Objects;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
public class RedissonSignSafeDog extends AbstractSignSafeDog {

	private static final String KEY = "magneton:m:signdog";

	private final RedissonClient redissonClient;

	public RedissonSignSafeDog(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public boolean validate(String sign, int signPeriodSeconds, Map<String, String> data, String salt) {
		RBucket<String> signBucket = this.redissonClient.getBucket(KEY + ":" + sign);
		if (signPeriodSeconds > 0) {
			if (signBucket.isExists()) {
				return false;
			}
			signBucket.set(String.valueOf(System.currentTimeMillis()), signPeriodSeconds, TimeUnit.SECONDS);
		}
		return Objects.equal(sign, this.sign(data, salt));
	}

}
