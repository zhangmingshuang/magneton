package org.magneton.module.wechat.miniprogram;

import com.google.common.base.Preconditions;
import org.magneton.core.Result;
import org.magneton.module.wechat.core.MemoryWechatAccessTokenCache;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.core.WxReq;
import org.magneton.module.wechat.miniprogram.core.auth.WechatMiniProgramOAuth;
import org.magneton.module.wechat.miniprogram.core.auth.WechatMiniProgramOAuthImpl;
import org.magneton.module.wechat.miniprogram.entity.MPAccessToken;
import org.magneton.module.wechat.miniprogram.entity.MPCode2Session;
import org.magneton.module.wechat.miniprogram.entity.MPPhoneInfo;

import java.util.Collections;

/**
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
public class DefaultWechatMiniProgram implements WechatMiniProgram {

	private final WechatMiniProgramConfig config;

	private final WechatMiniProgramOAuth oauth;

	public DefaultWechatMiniProgram(WechatMiniProgramConfig config) {
		this(config, new MemoryWechatAccessTokenCache());
	}

	public DefaultWechatMiniProgram(WechatMiniProgramConfig config, WechatAccessTokenCache wechatAccessTokenCache) {
		Preconditions.checkNotNull(config);
		Preconditions.checkNotNull(config.getAppid(), "appid must not be null");
		Preconditions.checkNotNull(config.getSecret(), "secret must not be null");
		this.config = config;
		this.oauth = new WechatMiniProgramOAuthImpl(config, wechatAccessTokenCache);
	}

	@Override
	public Result<MPCode2Session> code2Session(String code) {
		Preconditions.checkNotNull(code, "code must not be null");
		String url = String.format(
				"https://api.weixin.qq.com/sns/jscode2session?"
						+ "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
				this.config.getAppid(), this.config.getSecret(), code);
		return WxReq.doGet(url, MPCode2Session.class);
	}

	@Override
	public Result<MPPhoneInfo> getPhoneNumber(String code) {
		Preconditions.checkNotNull(code, "code must not be null");
		Result<MPAccessToken> reply = this.oauth.accessToken();
		if (!reply.isSuccess()) {
			return Result.failBy("accessToken error");
		}
		MPAccessToken mpAccessToken = reply.getData();
		String url = String.format(" https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s",
				mpAccessToken.getAccess_token());
		return WxReq.doPost(url, Collections.singletonMap("code", code), MPPhoneInfo.class);
	}

}
