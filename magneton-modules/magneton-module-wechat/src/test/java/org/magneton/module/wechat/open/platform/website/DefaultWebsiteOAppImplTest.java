package org.magneton.module.wechat.open.platform.website;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.core.Reply;
import org.magneton.module.wechat.open.WechatOpen;
import org.magneton.module.wechat.open.WechatOpenBuilder;
import org.magneton.module.wechat.open.WechatOpenConfig;
import org.magneton.module.wechat.open.entity.AccessTokenRes;
import org.magneton.module.wechat.open.entity.WebsiteCodeReq;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
class DefaultWebsiteOAppImplTest {

	private static WechatOpen wechatOpen;

	@BeforeAll
	public static void init() {
		WechatOpenConfig wechatOpenConfig = new WechatOpenConfig();
		wechatOpenConfig.setAppid("testAppId");
		wechatOpen = WechatOpenBuilder.newBuilder(wechatOpenConfig).build();
	}

	@Test
	void requestCodeUrl() {
		WebsiteOApp website = wechatOpen.website();
		WebsiteCodeReq req = new WebsiteCodeReq();
		req.setRedirectUri("testRedirectUri").setScope("testScope").setState("testState");
		String requestCodeUrl = website.requestCodeUrl(req);

		Assertions.assertEquals("https://open.weixin.qq.com/connect/qrconnect?appid=testAppId"
				+ "&redirect_uri=testRedirectUri&response_type=code&scope=testScope&state=testState#wechat_redirect",
				requestCodeUrl);
	}

	@Test
	void requestAccessToken() {
		WebsiteOApp website = wechatOpen.website();
		Reply<AccessTokenRes> accessTokenRes = website.requestAccessTokenByCode("testCode");
		Assertions.assertTrue(accessTokenRes.isSuccess());

		AccessTokenRes data = accessTokenRes.getData();
		AccessTokenRes accessTokenFromCache = website.getAccessTokenByOpenid(data.getOpenid());
		Assertions.assertNotNull(accessTokenFromCache);

		System.out.println(accessTokenRes);
	}

}