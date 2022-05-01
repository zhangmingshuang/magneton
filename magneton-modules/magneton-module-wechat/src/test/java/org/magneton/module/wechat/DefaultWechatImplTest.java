package org.magneton.module.wechat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.magneton.module.wechat.core.oauth2.WechatOAuth;
import org.magneton.module.wechat.platform.mobile.MobileApp;
import org.magneton.module.wechat.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
public class DefaultWechatImplTest {

	private static DefaultWechatImpl defaultWechat;

	private static WechatConfig testWechatConfig = new WechatConfig();

	@BeforeAll
	public static void init() {
		defaultWechat = new DefaultWechatImpl(WechatBuilder.newBuilder(testWechatConfig));
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
		WechatConfig wechatConfig = defaultWechat.getWechatConfig();
		Assertions.assertEquals(testWechatConfig, wechatConfig);
	}

}
