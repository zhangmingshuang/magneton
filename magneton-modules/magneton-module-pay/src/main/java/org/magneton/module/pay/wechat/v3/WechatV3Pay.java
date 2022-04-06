package org.magneton.module.pay.wechat.v3;

import java.util.Map;
import org.magneton.core.Consequences;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayNotification;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayOrder;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayOrderReq;
import org.magneton.module.pay.wechat.v3.prepay.WechatAppV3Prepay;

/**
 * 微信支付.
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface WechatV3Pay extends Pay {

	/**
	 * APP预下单处理
	 * @return 处理器
	 */
	WechatAppV3Prepay appPrepay();

	/**
	 * 查询订单
	 * @param req 业务请求数据
	 * @return 订单数据
	 */
	Consequences<WechatV3PayOrder> queryOrder(WechatV3PayOrderReq req);

	/**
	 * 回调处理数据校验并解析响应数据
	 * @param httpHeaders HTTP请求头
	 * @param body 请求Body
	 * @return 解析后的处理数据
	 */
	WechatV3PayNotification parsePaySuccessData(Map<String, String> httpHeaders, String body);

}
