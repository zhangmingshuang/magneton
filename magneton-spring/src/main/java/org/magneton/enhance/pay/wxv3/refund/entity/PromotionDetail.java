package org.magneton.enhance.pay.wxv3.refund.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 优惠退款信息
 *
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class PromotionDetail {

	/**
	 * 券ID promotion_id string[1, 32] 是 券或者立减优惠id 示例值：109519
	 */
	@JsonProperty("promotion_id")
	private String promotionId;

	/**
	 * 优惠范围 scope string[1, 32] 是 枚举值： GLOBAL：全场代金券 SINGLE：单品优惠 示例值：SINGLE
	 */
	private String scope;

	/**
	 * 优惠类型 type string[1, 32] 是 枚举值： COUPON：代金券，需要走结算资金的充值型代金券
	 * DISCOUNT：优惠券，不走结算资金的免充值型优惠券 示例值：DISCOUNT
	 */
	private String type;

	/**
	 * 优惠券面额 amount int 是 用户享受优惠的金额（优惠券面额=微信出资金额+商家出资金额+其他出资方金额 ），单位为分 示例值：5
	 */
	private int amount;

	/**
	 * 优惠退款金额 refund_amount int 是 优惠退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为用户支付的现金，说明详见代金券或立减优惠，单位为分
	 * 示例值：100
	 */
	@JsonProperty("refund_amount")
	private int refundAmount;

	/**
	 * 商品列表 goods_detail array 否 优惠商品发生退款时返回商品信息
	 */
	@Nullable
	@JsonProperty("goods_detail")
	private List<GoodsDetail> goodsDetails;

	/**
	 * 商品列表 优惠商品发生退款时返回商品信息
	 */
	@Setter
	@Getter
	@ToString
	public static class GoodsDetail {

		/**
		 * 商户侧商品编码 merchant_goods_id string[1, 32] 是 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成
		 * 示例值：1217752501201407033233368018
		 */
		@JsonProperty("merchant_goods_id")
		private String merchantGoodsId;

		/**
		 * 微信支付商品编码 wechatpay_goods_id string[1, 32] 否 微信支付定义的统一商品编号（没有可不传） 示例值：1001
		 */
		@Nullable
		@JsonProperty("wechatpay_goods_id")
		private String wechatpayGoodsId;

		/**
		 * 商品名称 goods_name string[1, 256] 否 商品的实际名称 示例值：iPhone6s 16G
		 */
		@Nullable
		@JsonProperty("goods_name")
		private String goodsName;

		/**
		 * 商品单价 unit_price int 是 商品单价金额，单位为分 示例值：528800
		 */
		@JsonProperty("unit_price")
		private int unitPrice;

		/**
		 * 商品退款金额 refund_amount int 是 商品退款金额，单位为分 示例值：528800
		 */
		@JsonProperty("refund_amount")
		private int refundAmount;

		/**
		 * 商品退货数量 refund_quantity int 是 单品的退款数量 示例值：1
		 */
		@JsonProperty("refund_quantity")
		private int refundQuantity;

	}

}
