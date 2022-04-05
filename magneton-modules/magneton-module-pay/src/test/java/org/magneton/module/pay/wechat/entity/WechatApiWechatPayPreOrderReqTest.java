package org.magneton.module.pay.wechat.entity;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.pay.wechat.api._WPApiPreOrderReq;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
class WechatApiWechatPayPreOrderReqTest {

	@Test
	void test() {
		String str = "a";
		_WPApiPreOrderReq wechatApiPreOrderReq = new _WPApiPreOrderReq().setMchid(str).setAppid(str).setNotify_url(str)
				.setOut_trade_no(str).setDescription(str);

		wechatApiPreOrderReq.setAmount(new _WPApiPreOrderReq.Amount().setTotal(1));

		String s = JSONUtil.toJsonStr(wechatApiPreOrderReq);
		Assertions.assertEquals(
				"{\"amount\":{\"total\":1,\"currency\":\"CNY\"},\"mchid\":\"a\",\"description\":\"a\",\"notify_url\":\"a\",\"out_trade_no\":\"a\",\"appid\":\"a\"}",
				s);
	}

}