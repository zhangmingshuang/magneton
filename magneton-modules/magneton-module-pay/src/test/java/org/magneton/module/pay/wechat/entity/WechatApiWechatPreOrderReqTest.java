package org.magneton.module.pay.wechat.entity;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.pay.wechat.api._WechatApiPreOrderReq;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
class WechatApiWechatPreOrderReqTest {

	@Test
	void test() {
		String str = "a";
		_WechatApiPreOrderReq wechatApiPreOrderReq = new _WechatApiPreOrderReq().setMchid(str).setAppid(str)
				.setNotify_url(str).setOut_trade_no(str).setDescription(str);

		wechatApiPreOrderReq.setAmount(new _WechatApiPreOrderReq.Amount().setTotal(1));

		String s = JSONUtil.toJsonStr(wechatApiPreOrderReq);
		Assertions.assertEquals(
				"{\"amount\":{\"total\":1,\"currency\":\"CNY\"},\"mchid\":\"a\",\"description\":\"a\",\"notify_url\":\"a\",\"out_trade_no\":\"a\",\"appid\":\"a\"}",
				s);
	}

}