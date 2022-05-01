package org.magneton.module.wechat.core.oauth2;

import javax.annotation.Nullable;

import org.magneton.module.wechat.entity.AccessTokenRes;

/**
 * 微信access_token缓存
 *
 * @author zhangmsh 2022/4/2
 * @since 1.0.0
 */
public interface WechatAccessTokenCache {

	/**
	 * 缓存AccessToken
	 * @param accessTokenRes AccessToken
	 */
	void save(AccessTokenRes accessTokenRes);

	@Nullable
	AccessTokenRes get(String openid);

}
