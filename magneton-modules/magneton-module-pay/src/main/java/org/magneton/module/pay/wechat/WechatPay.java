package org.magneton.module.pay.wechat;

import java.util.Map;
import org.magneton.core.Consequences;
import org.magneton.module.pay.Pay;
import org.magneton.module.pay.wechat.pojo.WechatPayCallbackRes;
import org.magneton.module.pay.wechat.pojo.WechatPayPreOrderReq;
import org.magneton.module.pay.wechat.pojo.WechatPayPreOrderRes;
import org.magneton.module.pay.wechat.pojo.WechatPayQueryOrderReq;
import org.magneton.module.pay.wechat.pojo.WechatPayQueryOrderRes;

/**
 * 微信支付.
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public interface WechatPay extends Pay {

	/**
	 * 预下单
	 * @param req 下单请求
	 * @return 预下单结果
	 */
	Consequences<WechatPayPreOrderRes> preOrder(WechatPayPreOrderReq req);

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
	WechatPayCallbackRes callback(Map<String, String> httpHeaders, String body);

}
