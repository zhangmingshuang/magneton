package org.magneton.module.pay.wechat.v3.prepay;

import org.magneton.core.Consequences;
import org.magneton.module.pay.wechat.v3.entity.WechatAppV3PreOrder;
import org.magneton.module.pay.wechat.v3.entity.WechatV3PayPreOrderReq;

/**
 * APP支付
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/open/pay/chapter2_5_0.shtml
 *
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
public interface WechatAppV3Prepay extends V3Prepay {

	/**
	 * 预下单
	 * @param req 下单请求
	 * @return 下单结果
	 */
	Consequences<WechatAppV3PreOrder> preOrder(WechatV3PayPreOrderReq req);

}
