package org.magneton.spring.starter.extension.wechat;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * Redis Wechat AccessToken Cache
 *
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class RedisWechatAccessTokenCache<T> implements WechatAccessTokenCache<T> {

	private final RedissonClient redissonClient;

	public RedisWechatAccessTokenCache(RedissonClient redissonClient) {
		this.redissonClient = Preconditions.checkNotNull(redissonClient);
	}

	@Override
	public void save(String key, T accessTokenRes) {
		Preconditions.checkNotNull(key);
		Preconditions.checkNotNull(accessTokenRes);
		RBucket<T> bucket = this.getBucket(key);
		bucket.set(accessTokenRes, 110, TimeUnit.MINUTES);
	}

	@Nullable
	@Override
	public T get(String key) {
		Preconditions.checkNotNull(key);
		return this.getBucket(key).get();
	}

	private RBucket<T> getBucket(String openid) {
		return this.redissonClient.getBucket("wechat:accessToken:" + openid);
	}

}
