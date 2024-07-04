package org.magneton.enhance.pay.wxv3.refund.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 金额信息
 *
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
public class RefundAmount {

	/**
	 * 订单金额 total int 是 订单总金额，单位为分 示例值：100
	 */
	private int total;

	/**
	 * 退款金额 refund int 是 退款标价金额，单位为分，可以做部分退款 示例值：100
	 */
	private int refund;

	/**
	 * 退款出资账户及金额 from array 否 退款出资的账户类型及金额信息
	 */
	@Nullable
	private List<From> from;

	/**
	 * 优惠退款信息 promotion_detail array 否 优惠退款信息
	 */

	/**
	 * 退款出资的账户类型及金额信息
	 */
	@Setter
	@Getter
	@ToString
	public static class From {

		/**
		 * 用户支付金额 payer_total int 是 现金支付金额，单位为分，只能为整数 示例值：90
		 */
		@JsonProperty("payer_total")
		private int payerTotal;

		/**
		 * 用户退款金额 payer_refund int 是 退款给用户的金额，不包含所有优惠券金额 示例值：90
		 */
		@JsonProperty("payer_refund")
		private int payerRefund;

		/**
		 * 应结退款金额 settlement_refund int 是
		 * 去掉非充值代金券退款金额后的退款金额，单位为分，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额 示例值：100
		 */
		@JsonProperty("settlement_refund")
		private int settlementRefund;

		/**
		 * 应结订单金额 settlement_total int 是 应结订单金额=订单金额-免充值代金券金额，应结订单金额<=订单金额，单位为分 示例值：100
		 */
		@JsonProperty("settlement_total")
		private int settlementTotal;

		/**
		 * 优惠退款金额 discount_refund int 是
		 * 优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠，单位为分 示例值：10
		 */
		@JsonProperty("discount_refund")
		private int discountRefund;

		/**
		 * 退款币种 currency string[1, 16] 是 符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY。 示例值：CNY
		 */
		private String currency = "CNY";

	}

}
