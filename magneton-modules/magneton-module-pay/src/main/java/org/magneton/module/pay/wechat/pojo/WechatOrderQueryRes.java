package org.magneton.module.pay.wechat.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.pay.core.PayTypes;
import org.magneton.module.pay.wechat.WechatPayStatus;
import org.magneton.module.pay.wechat.WechatTradeType;
import org.magneton.module.pay.wechat.api._WechatApiPayQueryRes;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class WechatOrderQueryRes {

	private _WechatApiPayQueryRes target;

	/**
	 * 支付类型
	 */
	private PayTypes payTypes;

	/**
	 * 交易类型
	 */
	private WechatTradeType tradeType;

	/**
	 * 支付狀態
	 */
	private WechatPayStatus payStatus;

	/**
	 * 订单总金额，单位为分
	 **/
	private int total;

	/**
	 * 用户支付金额，单位为分。（指使用优惠券的情况下，这里等于总金额-优惠券金额）
	 **/
	private int payerTotal;

}
