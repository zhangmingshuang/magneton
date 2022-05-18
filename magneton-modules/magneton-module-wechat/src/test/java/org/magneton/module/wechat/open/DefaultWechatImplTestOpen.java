package org.magneton.module.wechat.open;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.module.wechat.open.core.oauth2.WechatOAuth;
import org.magneton.module.wechat.open.platform.mobile.MobileApp;
import org.magneton.module.wechat.open.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class DefaultWechatImplTestOpen {

	private static DefaultWechatOpenImpl defaultWechat;

	private static WechatOpenConfig testWechatOpenConfig = new WechatOpenConfig();

	@BeforeAll
	public static void init() {
		defaultWechat = new DefaultWechatOpenImpl(WechatOpenBuilder.newBuilder(testWechatOpenConfig));
	}

	@Test
	void testApp() {

		WebsiteApp website = defaultWechat.website();
		MobileApp mobile = defaultWechat.mobile();

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
