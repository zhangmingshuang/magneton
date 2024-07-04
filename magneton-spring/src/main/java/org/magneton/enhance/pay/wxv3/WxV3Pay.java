package org.magneton.enhance.pay.wxv3;

import com.google.common.base.Strings;
import org.magneton.enhance.Result;
import org.magneton.enhance.pay.Pay;
import org.magneton.enhance.pay.wxv3.prepay.AppPrepay;
import org.magneton.enhance.pay.wxv3.prepay.H5Prepay;
import org.magneton.enhance.pay.wxv3.prepay.JSAPIPrepay;
import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayNotification;
import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayOrder;
import org.magneton.enhance.pay.wxv3.prepay.entity.WxPayOrderQuery;
import org.magneton.enhance.pay.wxv3.profitsharing.ProfitSharing;

import java.util.Map;

/**
 * 微信支付.
 * 文档地址：{@code https://pay.weixin.qq.com/wiki/doc/apiv3_partner/open/pay/chapter2_5_2.shtml}
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface WxV3Pay extends Pay {

	/**
	 * 回调结果响应文本处理
	 * @param success 是否成功
	 * @return 回调结果响应文本
	 */
	static String callbackResult(boolean success) {
		// @formatter:off
		return Strings.lenientFormat("{"
			+ "\"code\": \"%s\","
			+ "\"message\": \"%s\""
			+ "}", success ? "SUCCESS" : "FAIL", success ? "OK" : "FAIL");
		// @formatter:on
	}

	/**
	 * APP预下单处理
	 * @return APP预下单处理API
	 */
	AppPrepay appPrepay();

	/**
	 * JSAPI预下单处理
	 * @return JSAPI预下单处理API
	 */
	JSAPIPrepay jsapiPrepay();

	/**
	 * H5支付
	 * @return H5支付API
	 */
	H5Prepay h5Prepay();

	/**
	 * 分账
	 * @return 分账API
	 */
	ProfitSharing profitSharing();

	/**
	 * 查询订单
	 * @param query 业务请求数据
	 * @return 订单数据
	 */
	Result<WxPayOrder> queryOrder(WxPayOrderQuery query);

	/**
	 * 回调处理数据校验并解析响应数据
	 * @param httpHeaders HTTP请求头
	 * @param body 请求Body
	 * @return 解析后的处理数据
	 */
	WxPayNotification parsePaySuccessData(Map<String, String> httpHeaders, String body);

}
