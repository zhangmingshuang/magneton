package org.magneton.module.pay.wechat.v3.prepay.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
// 订单金额
public class Amount {

	/**
	 * 总金额
	 *
	 * 订单总金额，单位为分。 示例值：100
	 */
	private int total;

	/**
	 * 货币类型 string[1,16]
	 *
	 * CNY：人民币，境内商户号仅支持人民币。 示例值：CNY
	 */
	private String currency = "CNY";

}