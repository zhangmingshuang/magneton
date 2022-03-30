package org.magneton.module.pay.wechat.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信预下单的响应结果.
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class WechatPreOrderRes {

	/**
	 * 预支付交易会话标识。用于后续接口调用中使用，该值有效期为2小时
	 *
	 * 示例值：wx201410272009395522657a690389285100
	 */
	private String prepay_id;

}
