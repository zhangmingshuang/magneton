package org.magneton.module.wechat.miniprogram.core.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import org.magneton.core.Consequences;
import org.magneton.module.wechat.core.Req;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.miniprogram.WechatMiniProgramConfig;
import org.magneton.module.wechat.miniprogram.entity.MPAccessTokenRes;

/**
 * @author zhangmsh 2022/6/16
 * @since 1.0.1
 */
public class WechatMiniProgramOAuthImpl implements WechatMiniProgramOAuth {

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final WechatMiniProgramConfig wechatMiniProgramConfig;

	@Nullable
	private final WechatAccessTokenCache<MPAccessTokenRes> wechatAccessTokenCache;

	public WechatMiniProgramOAuthImpl(WechatMiniProgramConfig wechatMiniProgramConfig,
			@Nullable WechatAccessTokenCache<MPAccessTokenRes> wechatAccessTokenCache) {
		this.wechatMiniProgramConfig = Preconditions.checkNotNull(wechatMiniProgramConfig);
		this.wechatAccessTokenCache = wechatAccessTokenCache;
	}

	@Override
	public Consequences<MPAccessTokenRes> accessToken() {
		String requestUrl = String.format(
				"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
				this.wechatMiniProgramConfig.getAppid(), this.wechatMiniProgramConfig.getSecret());
		Consequences<MPAccessTokenRes> response = Req.doGet(requestUrl, MPAccessTokenRes.class);
		if (!response.isSuccess()) {
			return response.coverage();
		}
		MPAccessTokenRes accessTokenRes = response.getData();
		if (this.wechatAccessTokenCache != null) {
			this.wechatAccessTokenCache.save("min-wechat-pro", accessTokenRes);
		}
		return Consequences.success(accessTokenRes);
	}

	@Override
	@Nullable
	public MPAccessTokenRes accessTokenFromCache() {
		if (this.wechatAccessTokenCache == null) {
			return null;
		}
		return this.wechatAccessTokenCache.get("min-wechat-pro");
	}

}
