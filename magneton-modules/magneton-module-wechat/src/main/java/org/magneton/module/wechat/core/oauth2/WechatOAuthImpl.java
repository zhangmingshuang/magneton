package org.magneton.module.wechat.core.oauth2;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.core.base.MoreObjects;
import org.magneton.core.base.Preconditions;
import org.magneton.module.wechat.WechatConfig;
import org.magneton.module.wechat.core.Req;
import org.magneton.module.wechat.entity.AccessTokenRes;
import org.magneton.module.wechat.entity.UserInfoReq;
import org.magneton.module.wechat.entity.UserInfoRes;

/**
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
@Slf4j
public class WechatOAuthImpl implements WechatOAuth {

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%sE&grant_type=authorization_code";

	private static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s";

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final WechatConfig wechatConfig;

	@Nullable
	private final WechatAccessTokenCache wechatAccessTokenCache;

	public WechatOAuthImpl(WechatConfig wechatConfig, @Nullable WechatAccessTokenCache wechatAccessTokenCache) {
		this.wechatConfig = Preconditions.checkNotNull(wechatConfig);
		this.wechatAccessTokenCache = wechatAccessTokenCache;
	}

	@Override
	public Consequences<AccessTokenRes> accessToken(String code) {
		Preconditions.checkNotNull(code);
		String requestUrl = String.format(ACCESS_TOKEN_URL, this.wechatConfig.getAppid(), this.wechatConfig.getSecret(),
				code);
		Consequences<AccessTokenRes> response = Req.doGet(requestUrl, AccessTokenRes.class);
		if (!response.isSuccess()) {
			return response.coverage();
		}
		AccessTokenRes accessTokenRes = response.getData();
		if (this.wechatAccessTokenCache != null) {
			this.wechatAccessTokenCache.save(accessTokenRes);
		}
		return Consequences.success(accessTokenRes);
	}

	@Override
	@Nullable
	public AccessTokenRes accessTokenFromCache(String openid) {
		Preconditions.checkNotNull(openid);
		if (this.wechatAccessTokenCache == null) {
			return null;
		}
		return this.wechatAccessTokenCache.get(openid);
	}

	@Override
	public Consequences<UserInfoRes> userInfo(UserInfoReq userInfoReq) {
		Preconditions.checkNotNull(userInfoReq);
		String requestUrl = String.format(USER_INFO_URL, userInfoReq.getAccess_token(), userInfoReq.getOpenid(),
				MoreObjects.firstNonNull(userInfoReq.getLang(), ""));
		return Req.doGet(requestUrl, UserInfoRes.class);
	}

}
