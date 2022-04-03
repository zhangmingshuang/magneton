package org.magneton.module.wechat;

import org.junit.jupiter.api.Test;
import org.magneton.module.wechat.platform.mobile.MobileApp;
import org.magneton.module.wechat.platform.website.WebsiteApp;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
class WechatBuilderTest {

	@Test
	void test() {
		WechatConfig wechatConfig = new WechatConfig();
		Wechat wechat = WechatBuilder.newBuilder(wechatConfig).build();

		WebsiteApp website = wechat.website();

		MobileApp mobile = wechat.mobile();

	}

}