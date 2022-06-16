package org.magneton.module.wechat.core;

import javax.annotation.Nullable;

/**
 * 微信access_token缓存
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WechatAccessTokenCache<T> {

	/**
	 * 缓存AccessToken
	 * @param key cache key
	 * @param accessToken AccessToken
	 */
	void save(String key, T accessToken);

	@Nullable
	T get(String key);

}
