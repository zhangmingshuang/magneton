package org.magneton.module.wechat.open.core.oauth2;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.time.Duration;
import javax.annotation.Nullable;
import org.magneton.module.wechat.open.entity.AccessTokenRes;

/**
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class MemoryWechatAccessTokenCache implements WechatAccessTokenCache {

	// access_token 是调用授权关系接口的调用凭证，由于 access_token 有效期（目前为 2 个小时）
	private final Cache<String, AccessTokenRes> accessTokenCache = CacheBuilder.newBuilder()
			.expireAfterWrite(Duration.ofMinutes(110)).maximumSize(10_000).build();

	@Override
	public void save(AccessTokenRes accessTokenRes) {
		Preconditions.checkNotNull(accessTokenRes);
		this.accessTokenCache.put(accessTokenRes.getOpenid(), accessTokenRes);
	}

	@Nullable
	@Override
	public AccessTokenRes get(String openid) {
		Preconditions.checkNotNull(openid);
		return this.accessTokenCache.getIfPresent(openid);
	}

}
