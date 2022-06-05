package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Setter
@Getter
@ToString
@Accessors(chain = true)
// 单品列表
public class GoodsDetail {

	/**
	 * 商户侧商品编码 string[1,32 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。 示例值：1246464644
	 **/
	@JsonProperty("merchant_goods_id")
	private String merchantGoodsId;

	/**
	 * 微信支付商品编码 string[1,32] 微信支付定义的统一商品编号（没有可不传） 示例值：1001
	 **/
	@Nullable
	@JsonProperty("wechatpay_goods_id")
	private String wechatpayGoodsId;

	/**
	 * 商品名称 string[1,256] 商品的实际名称 示例值：iPhoneX 256G
	 **/
	@Nullable
	@JsonProperty("goods_name")
	private String goodsName;

	/**
	 * 商品数量 用户购买的数量 示例值：1
	 **/
	private int quantity;

	/**
	 * 商品单价 ，单位为分 示例值：828800
	 **/
	@JsonProperty("unit_price")
	private int unitPrice;

}