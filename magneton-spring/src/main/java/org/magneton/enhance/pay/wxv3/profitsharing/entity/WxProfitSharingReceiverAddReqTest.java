package org.magneton.enhance.pay.wxv3.profitsharing.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.enhance.pay.wxv3.core.BaseV3Api;
import org.magneton.enhance.pay.wxv3.core.WxReceiverType;

/**
 * @author zhangmsh 2022/6/9
 * @since 1.0.1
 */
class WxProfitSharingReceiverAddReqTest {

	@Test
	void test() throws JsonProcessingException {
		WxProfitSharingReceiverAddReq req = new WxProfitSharingReceiverAddReq();
		req.setType(WxReceiverType.MERCHANT_ID);
		String json = BaseV3Api.json().writeValueAsString(req);
		Assertions.assertEquals("{\"type\":\"MERCHANT_ID\"}", json);
	}

}