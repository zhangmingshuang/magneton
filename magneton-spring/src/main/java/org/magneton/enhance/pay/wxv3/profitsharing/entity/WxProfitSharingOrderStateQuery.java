package org.magneton.enhance.pay.wxv3.profitsharing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账结果查询
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WxProfitSharingOrderStateQuery {

	/**
	 * 微信订单号 transaction_id string[1, 32] 是 query微信支付订单号 示例值：4208450740201411110007820472
	 */
	@JsonProperty("transaction_id")
	private String transactionId;

	/**
	 * 商户分账单号 out_order_no string[1,64] 是 path查询分账结果，输入申请分账时的商户分账单号；
	 * 查询分账完结执行的结果，输入发起分账完结时的商户分账单号。 示例值：P20150806125346
	 */
	@JsonProperty("out_order_no")
	private String outOrderNo;

}
