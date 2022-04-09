package org.magneton.module.pay.wechat.v3.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class WechatV3PayOrderQuery {

	private String reqId;

	private Type reqIdType = Type.TRANSACTION_ID;

	public enum Type {

		/**
		 * 微信支付系统生成的订单号 示例值：1217752501201407033233368018
		 */
		TRANSACTION_ID,
		/**
		 * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。 特殊规则：最小字符长度为6
		 * 示例值：1217752501201407033233368018
		 */
		OUT_TRADE_NO

	}

}
