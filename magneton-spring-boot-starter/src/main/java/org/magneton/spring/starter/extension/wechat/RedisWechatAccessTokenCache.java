package org.magneton.spring.starter.extension.wechat;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.magneton.module.wechat.open.core.oauth2.WechatAccessTokenCache;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

/**
 * Redis Wechat AccessToken Cache
 *
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class RedisWechatAccessTokenCache implements WechatAccessTokenCache {

	private final RedissonClient redissonClient;

	public RedisWechatAccessTokenCache(RedissonClient redissonClient) {
		this.redissonClient = Preconditions.checkNotNull(redissonClient);
	}

	@Override
	public void save(AccessTokenRes accessTokenRes) {
		Preconditions.checkNotNull(accessTokenRes);
		String openid = accessTokenRes.getOpenid();
		this.getBucket(openid).set(accessTokenRes, 110, TimeUnit.MINUTES);
	}

	@Nullable
	@Override
	public AccessTokenRes get(String openid) {
		Preconditions.checkNotNull(openid);
		return this.getBucket(openid).get();
	}

	private RBucket<AccessTokenRes> getBucket(String openid) {
		return this.redissonClient.getBucket("wechat:accessToken:" + openid);
	}

}
