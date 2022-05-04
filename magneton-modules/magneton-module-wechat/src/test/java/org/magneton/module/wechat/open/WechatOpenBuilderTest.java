package org.magneton.module.wechat.open;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
class WechatOpenBuilderTest {

	@Test
	void test() {
		WechatOpenConfig wechatOpenConfig = new WechatOpenConfig();
		WechatOpen wechatOpen = WechatOpenBuilder.newBuilder(wechatOpenConfig).build();
		Assertions.assertNotNull(wechatOpen);
	}

}