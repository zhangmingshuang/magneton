package org.magneton.module.pay.wechat.entity;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
class WechatPreOrderReqTest {

	@Test
	void test() {
		String str = "a";
		WechatPreOrderReq wechatPreOrderReq = new WechatPreOrderReq().setMchid(str).setAppid(str).setNotify_url(str)
				.setOut_trade_no(str).setDescription(str);

		wechatPreOrderReq.setAmount(new WechatPreOrderReq.Amount().setTotal(1));

		String s = JSONUtil.toJsonStr(wechatPreOrderReq);
		Assertions.assertEquals(
				"{\"amount\":{\"total\":1,\"currency\":\"CNY\"},\"mchid\":\"a\",\"description\":\"a\",\"notify_url\":\"a\",\"out_trade_no\":\"a\",\"appid\":\"a\"}",
				s);
	}

}