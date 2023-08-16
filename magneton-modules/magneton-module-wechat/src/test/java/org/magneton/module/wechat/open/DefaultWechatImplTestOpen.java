package org.magneton.module.wechat.open;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.module.wechat.open.core.oauth2.WechatOAuth;
import org.magneton.module.wechat.open.platform.mobile.MobileOApp;
import org.magneton.module.wechat.open.platform.website.WebsiteOApp;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class DefaultWechatImplTestOpen {

	private static DefaultWechatOpenImplOpen defaultWechat;

	private static WechatOpenConfig testWechatOpenConfig = new WechatOpenConfig();

	@BeforeAll
	public static void init() {
		defaultWechat = new DefaultWechatOpenImplOpen(WechatOpenBuilder.newBuilder(testWechatOpenConfig));
	}

	@Test
	void testApp() {

		WebsiteOApp website = defaultWechat.website();
		MobileOApp mobile = defaultWechat.mobile();

		Assertions.assertNotNull(website);
		Assertions.assertNotNull(mobile);
	}

	@Test
	void testOAuth() {
		WechatOAuth wechatOAuth = defaultWechat.getOAuth();
		Assertions.assertNotNull(wechatOAuth);
	}

	@Test
	void testWechatConfig() {
		WechatOpenConfig wechatOpenConfig = defaultWechat.getWechatConfig();
		Assertions.assertEquals(testWechatOpenConfig, wechatOpenConfig);
	}

}
