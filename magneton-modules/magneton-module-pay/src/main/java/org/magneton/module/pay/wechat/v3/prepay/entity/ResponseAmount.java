package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/4/6
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class ResponseAmount {

	/**
	 * 总金额 total int 是 订单总金额，单位为分。 示例值：100
	 **/
	private int total;

	/**
	 * 用户支付金额 payer_total int 是 用户支付金额，单位为分。 示例值：100
	 **/
	@JsonProperty("payer_total")
	private int payerTotal;

	/**
	 * 货币类型 currency string[1,16] 是 CNY：人民币，境内商户号仅支持人民币。 示例值：CNY
	 **/
	private String currency;

	/**
	 * 用户支付币种 payer_currency string[1,16] 是 用户支付币种 示例值：CNY
	 **/
	@JsonProperty("payer_currency")
	private String payerCurrency;

}
