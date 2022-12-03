package org.magneton.module.pay.wechat.v3.refund.entity;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 退款商品
 *
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class RefundGoodsDetailReq {

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
