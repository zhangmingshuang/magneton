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

		WxPayAppPrepayReq jsonReq = WechatBaseV3Pay.json().readValue(
				"{\n" + "\t\"mchid\": \"1900006XXX\",\n" + "\t\"out_trade_no\": \"APP1217752501201407033233368018\",\n"
						+ "\t\"appid\": \"wxb4ba3c02aa476XXX\",\n" + "\t\"description\": \"Image形象店-深圳腾大-QQ公仔\",\n"
						+ "\t\"notify_url\": \"https://weixin.qq.com/\",\n" + "\t\"amount\": {\n"
						+ "\t\t\"total\": 1,\n" + "\t\t\"currency\": \"CNY\"\n" + "\t}\n" + "}",
				WxPayAppPrepayReq.class);
		System.out.println(WechatBaseV3Pay.json().writeValueAsString(jsonReq));
	}

}