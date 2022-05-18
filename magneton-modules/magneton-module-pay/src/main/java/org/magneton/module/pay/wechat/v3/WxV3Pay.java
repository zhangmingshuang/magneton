package org.magneton.module.pay.wechat.v3;

import java.util.Map;
import org.magneton.core.Consequences;
import org.magneton.core.base.Strings;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.wechat.v3.entity.WxPayNotification;
import org.magneton.module.pay.wechat.v3.entity.WxPayOrder;
import org.magneton.module.pay.wechat.v3.entity.WxPayOrderQuery;
import org.magneton.module.pay.wechat.v3.prepay.AppPrepay;
import org.magneton.module.pay.wechat.v3.prepay.H5Prepay;
import org.magneton.module.pay.wechat.v3.prepay.JSAPIPrepay;

/**
 * 微信支付.
 * 文档地址：{@code https://pay.weixin.qq.com/wiki/doc/apiv3_partner/open/pay/chapter2_5_2.shtml}
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface WxV3Pay extends Pay {

	static String callbackResult(boolean success) {
		// @formatter:off
		return Strings.format("{"
			+ "\"code\": \"%s\","
			+ "\"message\": \"%s\""
			+ "}", success ? "SUCCESS" : "FAIL", success ? "OK" : "FAIL");
		// @formatter:on
	}

	/**
	 * APP预下单处理
	 * @return 处理器
	 */
	AppPrepay appPrepay();

	/**
	 * JSAPI预下单处理
	 * @return 处理器
	 */
	JSAPIPrepay jsapiPrepay();

	/**
	 * H5支付
	 * @return
	 */
	H5Prepay h5Prepay();

	/**
	 * 查询订单
	 * @param query 业务请求数据
	 * @return 订单数据
	 */
	Consequences<WxPayOrder> queryOrder(WxPayOrderQuery query);

	/**
	 * 回调处理数据校验并解析响应数据
	 * @param httpHeaders HTTP请求头
	 * @param body 请求Body
	 * @return 解析后的处理数据
	 */
	WxPayNotification parsePaySuccessData(Map<String, String> httpHeaders, String body);

}
