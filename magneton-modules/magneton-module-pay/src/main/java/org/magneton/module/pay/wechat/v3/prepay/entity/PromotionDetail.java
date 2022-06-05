package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 优惠功能 promotion_detail array 否 优惠功能，享受优惠时返回该字段。
 *
 * @author zhangmsh 2022/4/6
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class PromotionDetail {

	/**
	 * 券ID coupon_id string[1,32] 是 券ID 示例值：109519
	 **/
	@JsonProperty("coupon_id")
	private String couponId;

	/**
	 * 优惠名称 name string[1,64] 否 优惠名称 示例值：单品惠-6
	 **/
	@Nullable
	private String name;

	/**
	 * 优惠范围 scope string[1,32] 否 GLOBAL：全场代金券 SINGLE：单品优惠 示例值：XmlElementDecl.GLOBAL
	 **/
	@Nullable
	private String scope;

	/**
	 * 优惠类型 type string[1,32] 否 CASH- 代金券，需要走结算资金的预充值型代金券 NOCASH- 优惠券，不走结算资金的免充值型优惠券
	 * 示例值：CASH
	 **/
	@Nullable
	private String type;

	/**
	 * 优惠券面额 amount int 是 优惠券面额 示例值：100
	 **/
	private int amount;

	/**
	 * 活动ID stock_id string[1,32] 否 活动ID 示例值：931386
	 **/
	@Nullable
	@JsonProperty("stock_id")
	private String stockId;

	/**
	 * 微信出资 wechatpay_contribute int 否 微信出资，单位为分 示例值：0
	 **/
	@JsonProperty("wechatpay_contribute")
	private int wechatpayContribute;

	/**
	 * 商户出资 merchant_contribute int 否 商户出资，单位为分 示例值：0
	 **/
	@JsonProperty("merchant_contribute")
	private int merchantContribute;

	/**
	 * 其他出资 other_contribute int 否 其他出资，单位为分 示例值：0
	 **/
	@JsonProperty("other_contribute")
	private int otherContribute;

	/**
	 * 优惠币种 currency string[1,16] 否 CNY：人民币，境内商户号仅支持人民币。 示例值：CNY
	 **/

	@Nullable
	private String currency;

	/** +单品列表 goods_detail array 否 单品列表信息 **/
	@JsonProperty("goods_detail")
	private ResponseGoodsDetail goodsDetail;

}
