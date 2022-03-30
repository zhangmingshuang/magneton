package org.magneton.module.pay.wechat;

import org.magneton.core.Consequences;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.wechat.pojo.WechatOrderQueryReq;
import org.magneton.module.pay.wechat.pojo.WechatOrderQueryRes;
import org.magneton.module.pay.wechat.pojo.WechatPreOrderReq;
import org.magneton.module.pay.wechat.pojo.WechatPreOrderRes;

/**
 * 微信支付.
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface WechatPay extends Pay {

	/**
	 * 查询订单
	 * @param outTradeNo 业务订单号
	 * @return 订单数据
	 */
	Consequences<WechatOrderQueryRes> queryOrder(WechatOrderQueryReq req);

	/**
	 * 预下单
	 * @param req 下单请求
	 * @return 预下单结果
	 */
	Consequences<WechatPreOrderRes> preOrder(WechatPreOrderReq req);

}
