package org.magneton.cache.redis.enhance.im.tencent;

import com.google.common.base.Preconditions;
import org.magneton.enhance.im.tencent.UserSignCache;
import org.redisson.api.RedissonClient;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * .
 *
 * @author zhangmsh 2022/4/26
 * @since 2.0.8
 */
public class TencentImUserSignCache implements UserSignCache {

	private final RedissonClient redissonClient;

	public TencentImUserSignCache(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public void put(String userId, String userSig, long expireSeconds) {
		Preconditions.checkNotNull(userId, "userId must not be null");
		Preconditions.checkNotNull(userSig, "userSign must not be null");
		this.redissonClient.getBucket("tencentIm:userSign:" + userId).set(userSig, expireSeconds, TimeUnit.SECONDS);
	}

	@Nullable
	@Override
	public String get(String userId) {
		Preconditions.checkNotNull(userId, "userId must not be null");
		return (String) this.redissonClient.getBucket("tencentIm:userSign:" + userId).get();
	}

	@Override
	public void remove(String userId) {
		Preconditions.checkNotNull(userId, "userId must not be null");
		this.redissonClient.getBucket("tencentIm:userSign:" + userId).delete();
	}

}
