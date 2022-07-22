package org.magneton.module.wechat.miniprogram;

import java.util.Collections;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.magneton.core.Consequences;
import org.magneton.module.wechat.core.MemoryWechatAccessTokenCache;
import org.magneton.module.wechat.core.Req;
import org.magneton.module.wechat.core.WechatAccessTokenCache;
import org.magneton.module.wechat.core.WxJson;
import org.magneton.module.wechat.miniprogram.core.MPAesHelper;
import org.magneton.module.wechat.miniprogram.core.auth.WechatMiniProgramOAuth;
import org.magneton.module.wechat.miniprogram.core.auth.WechatMiniProgramOAuthImpl;
import org.magneton.module.wechat.miniprogram.entity.MPAccessTokenRes;
import org.magneton.module.wechat.miniprogram.entity.MPCode2Session;
import org.magneton.module.wechat.miniprogram.entity.MPPhoneInfo;
import org.magneton.module.wechat.miniprogram.entity.MPSensitiveUserInfo;
import org.magneton.module.wechat.miniprogram.entity.MPUserInfo;

/**
 * @author zhangmsh 2022/5/1
 * @since 1.0.0
 */
@Slf4j
public class DefaultWechatMiniProgram implements WechatMiniProgram {

	private final WechatMiniProgramConfig config;

	private WechatMiniProgramOAuth wechatMiniProgramOAuth;

	public DefaultWechatMiniProgram(WechatMiniProgramConfig config) {
		this(config, new MemoryWechatAccessTokenCache());
	}

	public DefaultWechatMiniProgram(WechatMiniProgramConfig config, WechatAccessTokenCache wechatAccessTokenCache) {
		Preconditions.checkNotNull(config);
		Preconditions.checkNotNull(config.getAppid(), "appid must not be null");
		Preconditions.checkNotNull(config.getSecret(), "secret must not be null");
		this.config = config;
		this.wechatMiniProgramOAuth = new WechatMiniProgramOAuthImpl(config, wechatAccessTokenCache);
	}

	@Override
	public Consequences<MPCode2Session> code2Session(String code) {
		Preconditions.checkNotNull(code, "code must not be null");
		String url = String.format(
				"https://api.weixin.qq.com/sns/jscode2session?"
						+ "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
				this.config.getAppid(), this.config.getSecret(), code);
		if (log.isDebugEnabled()) {
			log.debug("code2session: {}", url);
		}
		return Req.doGet(url, MPCode2Session.class);
	}

	@Override
	public Consequences<MPPhoneInfo> getPhoneNumber(String code) {
		Preconditions.checkNotNull(code, "code must not be null");
		MPAccessTokenRes mpAccessTokenRes = this.wechatMiniProgramOAuth.accessTokenFromCache();
		if (mpAccessTokenRes == null) {
			Consequences<MPAccessTokenRes> consequences = this.wechatMiniProgramOAuth.accessToken();
			if (!consequences.isSuccess()) {
				return Consequences.fail("accessToken error").coverage();
			}
			mpAccessTokenRes = consequences.getData();
		}
		String url = String.format(" https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=%s",
				mpAccessTokenRes.getAccess_token());
		return Req.doPost(url, Collections.singletonMap("code", code), MPPhoneInfo.class);
	}

	@Override
	public Consequences<MPSensitiveUserInfo> decodeUserInfo(String sessionKey, MPUserInfo userInfo) {
		Preconditions.checkNotNull(sessionKey, "sessionKey");
		Preconditions.checkNotNull(userInfo, "userInfo");
		try {
			String decodeData = MPAesHelper.decryptForWeChatApplet(userInfo.getEncryptedData(), sessionKey,
					userInfo.getIv());
			return Consequences.success(WxJson.getInstance().readValue(decodeData, MPSensitiveUserInfo.class));
		}
		catch (Exception e) {
			throw new RuntimeException("小程序敏感数据解密失败", e);
		}
	}

}
