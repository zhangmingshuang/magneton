package org.magneton.module.pay.wechat.entity;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayPreOrderReq;

/**
 * .
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
class WechatApiWechatV3PrepayPreOrderReqTest {

	@Test
	void test() {
		String str = "a";
		WechatV3PayPreOrderReq wechatApiPreOrderReq = new WechatV3PayPreOrderReq().setMchid(str).setAppid(str)
				.setNotify_url(str).setOut_trade_no(str).setDescription(str);

		wechatApiPreOrderReq.setAmount(new WechatV3PayPreOrderReq.Amount().setTotal(1));

		String s = JSONUtil.toJsonStr(wechatApiPreOrderReq);
		Assertions.assertEquals(
				"{\"amount\":{\"total\":1,\"currency\":\"CNY\"},\"mchid\":\"a\",\"description\":\"a\",\"notify_url\":\"a\",\"out_trade_no\":\"a\",\"appid\":\"a\"}",
				s);
	}

}