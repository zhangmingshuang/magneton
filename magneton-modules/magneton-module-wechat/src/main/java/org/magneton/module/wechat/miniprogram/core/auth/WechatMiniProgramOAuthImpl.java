package org.magneton.module.wechat.miniprogram.core.auth;

import com.google.common.base.Preconditions;
import org.magneton.core.Result;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.core.WxReq;
import org.magneton.module.wechat.miniprogram.WechatMiniProgramConfig;
import org.magneton.module.wechat.miniprogram.entity.MPAccessToken;

import javax.annotation.Nullable;

/**
 * <a href=
 * "https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-access-token/getAccessToken.html">微信小程序授权实现</a>
 *
 * @author zhangmsh 2022/6/16
 * @since 1.0.1
 */
public class WechatMiniProgramOAuthImpl implements WechatMiniProgramOAuth {

	private final WechatMiniProgramConfig wechatMiniProgramConfig;

	@Nullable
	private final WechatAccessTokenCache<MPAccessToken> wechatAccessTokenCache;

	public WechatMiniProgramOAuthImpl(WechatMiniProgramConfig wechatMiniProgramConfig,
			@Nullable WechatAccessTokenCache<MPAccessToken> wechatAccessTokenCache) {
		this.wechatMiniProgramConfig = Preconditions.checkNotNull(wechatMiniProgramConfig);
		this.wechatAccessTokenCache = wechatAccessTokenCache;
	}

	@Override
	public Result<MPAccessToken> accessToken() {
		MPAccessToken res = this.accessTokenFromCache();
		if (res != null) {
			return Result.successWith(res);
		}
		String requestUrl = String.format(
				"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
				this.wechatMiniProgramConfig.getAppid(), this.wechatMiniProgramConfig.getSecret());
		Result<MPAccessToken> response = WxReq.doGet(requestUrl, MPAccessToken.class);
		if (!response.isSuccess()) {
			return response;
		}
		MPAccessToken accessTokenRes = response.getData();
		this.accessTokenToCache(accessTokenRes);
		return Result.successWith(accessTokenRes);
	}

	private void accessTokenToCache(MPAccessToken accessTokenRes) {
		if (this.wechatAccessTokenCache != null) {
			this.wechatAccessTokenCache.put("min-wechat-pro", accessTokenRes);
		}
	}

	private MPAccessToken accessTokenFromCache() {
		if (this.wechatAccessTokenCache == null) {
			return null;
		}
		return this.wechatAccessTokenCache.get("min-wechat-pro");
	}

}
