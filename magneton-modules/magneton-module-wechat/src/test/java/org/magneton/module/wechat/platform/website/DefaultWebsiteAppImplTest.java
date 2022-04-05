package org.magneton.module.wechat.platform.website;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.core.Consequences;
import org.magneton.module.wechat.Wechat;
import org.magneton.module.wechat.WechatBuilder;
import org.magneton.module.wechat.WechatConfig;
import org.magneton.module.wechat.entity.AccessTokenRes;
import org.magneton.module.wechat.entity.WebsiteCodeReq;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
class DefaultWebsiteAppImplTest {

	private static Wechat wechat;

	@BeforeAll
	public static void init() {
		WechatConfig wechatConfig = new WechatConfig();
		wechatConfig.setAppid("testAppId");
		wechat = WechatBuilder.newBuilder(wechatConfig).build();
	}

	@Test
	void requestCodeUrl() {
		WebsiteApp website = wechat.website();
		WebsiteCodeReq req = new WebsiteCodeReq();
		req.setRedirect_uri("testRedirectUri").setScope("testScope").setState("testState");
		String requestCodeUrl = website.requestCodeUrl(req);

		Assertions.assertEquals("https://open.weixin.qq.com/connect/qrconnect?appid=testAppId"
				+ "&redirect_uri=testRedirectUri&response_type=code&scope=testScope&state=testState#wechat_redirect",
				requestCodeUrl);
	}

	@Test
	void requestAccessToken() {
		WebsiteApp website = wechat.website();
		Consequences<AccessTokenRes> accessTokenRes = website.requestAccessTokenByCode("testCode");
		Assertions.assertTrue(accessTokenRes.isSuccess());

		AccessTokenRes data = accessTokenRes.getData();
		AccessTokenRes accessTokenFromCache = website.getAccessTokenByOpenid(data.getOpenid());
		Assertions.assertNotNull(accessTokenFromCache);

		System.out.println(accessTokenRes);
	}

}