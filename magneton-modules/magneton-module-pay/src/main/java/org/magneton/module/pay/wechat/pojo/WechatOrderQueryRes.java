package org.magneton.module.pay.wechat.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.pay.core.PayTypes;
import org.magneton.module.pay.wechat.WechatPayStatus;
import org.magneton.module.pay.wechat.WechatTradeType;

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

}
