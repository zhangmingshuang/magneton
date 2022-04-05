package org.magneton.module.pay.wechat.v3;

import java.util.Map;
import org.magneton.core.Consequences;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.wechat.v3.entity.WechatPayQueryOrderReq;
import org.magneton.module.pay.wechat.v3.entity.WechatPayQueryOrderRes;
import org.magneton.module.pay.wechat.v3.entity._WPPayCallbackRes;
import org.magneton.module.pay.wechat.v3.prepay.WechatAppV3Prepay;

/**
 * 微信支付.
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface WechatV3Pay extends Pay {

	WechatAppV3Prepay appPay();

	/**
	 * 查询订单
	 * @param req 业务请求数据
	 * @return 订单数据
	 */
	Consequences<WechatPayQueryOrderRes> queryOrder(WechatPayQueryOrderReq req);

	/**
	 * 回调处理数据校验并解析响应数据
	 * @param httpHeaders 请求头
	 * @param body 请求Body
	 * @return 解析后的处理数据
	 */
	_WPPayCallbackRes callback(Map<String, String> httpHeaders, String body);

}
