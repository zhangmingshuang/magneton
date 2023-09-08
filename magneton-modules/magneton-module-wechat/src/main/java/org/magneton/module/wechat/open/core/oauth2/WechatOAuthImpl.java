package org.magneton.module.wechat.open.core.oauth2;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.magneton.module.wechat.core.Req;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.open.WechatOpenConfig;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.UserInfoReq;
import org.magneton.module.wechat.open.entity.UserInfoRes;

import javax.annotation.Nullable;

/**
 * @author zhangmsh 2022/4/1
 * @since 1.0.0
 */
@Slf4j
public class WechatOAuthImpl implements WechatOAuth {

	private final WechatOpenConfig wechatOpenConfig;

	@Nullable
	private final WechatAccessTokenCache<AccessTokenRes> wechatAccessTokenCache;

	public WechatOAuthImpl(WechatOpenConfig wechatOpenConfig,
			@Nullable WechatAccessTokenCache<AccessTokenRes> wechatAccessTokenCache) {
		this.wechatOpenConfig = Preconditions.checkNotNull(wechatOpenConfig);
		this.wechatAccessTokenCache = wechatAccessTokenCache;
	}

	@Override
	public Reply<AccessTokenRes> accessToken(String code) {
		Preconditions.checkNotNull(code);
		String requestUrl = String.format(
				"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%sE&grant_type=authorization_code",
				this.wechatOpenConfig.getAppid(), this.wechatOpenConfig.getSecret(), code);
		Reply<AccessTokenRes> response = Req.doGet(requestUrl, AccessTokenRes.class);
		if (!response.isSuccess()) {
			return response.coverage();
		}
		AccessTokenRes accessTokenRes = response.getData();
		if (this.wechatAccessTokenCache != null) {
			this.wechatAccessTokenCache.save(accessTokenRes.getOpenid(), accessTokenRes);
		}
		return Reply.success(accessTokenRes);
	}

	@Override
	public Reply<UserInfoRes> userInfo(UserInfoReq userInfoReq) {
		Preconditions.checkNotNull(userInfoReq);
		String requestUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s",
				userInfoReq.getAccessToken(), userInfoReq.getOpenid(),
				MoreObjects.firstNonNull(userInfoReq.getLang(), ""));
		return Req.doGet(requestUrl, UserInfoRes.class);
	}

}
