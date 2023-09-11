package org.magneton.module.wechat.open.core.oauth2;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Result;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.core.WxReq;
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
	public Result<AccessTokenRes> accessToken(String code) {
		Preconditions.checkNotNull(code);
		String requestUrl = String.format(
				"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%sE&grant_type=authorization_code",
				this.wechatOpenConfig.getAppid(), this.wechatOpenConfig.getSecret(), code);
		Result<AccessTokenRes> response = WxReq.doGet(requestUrl, AccessTokenRes.class);
		if (!response.isSuccess()) {
			return response;
		}
		AccessTokenRes accessTokenRes = response.getData();
		Verify.verifyNotNull(accessTokenRes, "access_token response is null");
		if (this.wechatAccessTokenCache != null) {
			this.wechatAccessTokenCache.put(accessTokenRes.getOpenid(), accessTokenRes);
		}
		return Result.successWith(accessTokenRes);
	}

	@Override
	public Result<UserInfoRes> userInfo(UserInfoReq userInfoReq) {
		Preconditions.checkNotNull(userInfoReq);
		String requestUrl = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=%s",
				userInfoReq.getAccessToken(), userInfoReq.getOpenid(),
				MoreObjects.firstNonNull(userInfoReq.getLang(), ""));
		return WxReq.doGet(requestUrl, UserInfoRes.class);
	}

}
