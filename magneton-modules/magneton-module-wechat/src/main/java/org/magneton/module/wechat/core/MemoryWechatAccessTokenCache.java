package org.magneton.module.wechat.core;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nullable;
import java.time.Duration;

/**
 * 基于内存的微信授权缓存.
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public class MemoryWechatAccessTokenCache<T> implements WechatAccessTokenCache<T> {

	/**
	 * access_token 是调用授权关系接口的调用凭证，由于 access_token 有效期（目前为 2 个小时）
	 */
	private final Cache<String, T> accessTokenCache = CacheBuilder.newBuilder()
			.expireAfterWrite(Duration.ofMinutes(110)).maximumSize(10_000).build();

	@Override
	public void put(String key, T accessTokenRes) {
		Preconditions.checkNotNull(key);
		Preconditions.checkNotNull(accessTokenRes);
		this.accessTokenCache.put(key, accessTokenRes);
	}

	@Nullable
	@Override
	public T get(String key) {
		Preconditions.checkNotNull(key);
		return this.accessTokenCache.getIfPresent(key);
	}

}
