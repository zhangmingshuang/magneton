package org.magneton.module.wechat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
class WechatBuilderTest {

	@Test
	void test() {
		WechatConfig wechatConfig = new WechatConfig();
		Wechat wechat = WechatBuilder.newBuilder(wechatConfig).build();
		Assertions.assertNotNull(wechat);
	}

}