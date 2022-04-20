package org.magneton.module.pay.wechat.v3.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.pay.wechat.v3.prepay.WechatBaseV3Pay;
import org.magneton.test.ChaosTest;

/**
 * @author zhangmsh 2022/4/20
 * @since 1.0.0
 */
class WxPayAppPrepayReqTest {

	@Test
	void test() throws JsonProcessingException {
		WxPayAppPrepayReq req = ChaosTest.createExcepted(WxPayAppPrepayReq.class);
		String json = WechatBaseV3Pay.json().writeValueAsString(req);
		Assertions.assertTrue(json.contains("appid") && json.contains("mchid"));
	}

}