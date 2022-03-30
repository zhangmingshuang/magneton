package org.magneton.module.pay.wechat.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 预下单请求.
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class WechatPreOrderReq {

	/**
	 * 业务订单号
	 */
	private String outTradeNo;

	/**
	 * 订单金额, 分
	 */
	private int amount;

	/**
	 * 描述信息
	 */
	private String description;

}
