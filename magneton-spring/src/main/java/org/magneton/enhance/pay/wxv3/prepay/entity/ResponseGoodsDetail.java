package org.magneton.enhance.pay.wxv3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

/**
 * @author zhangmsh 2022/4/6
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class ResponseGoodsDetail {

	/**
	 * 商品编码 goods_id string[1,32] 是 商品编码 示例值：M1006
	 **/
	@JsonProperty("goods_id")
	private String goodsId;

	/**
	 * 商品数量 quantity int 是 用户购买的数量 示例值：1
	 **/
	private int quantity;

	/**
	 * 商品单价 unit_price int 是 商品单价，单位为分 示例值：100
	 **/
	@JsonProperty("unit_price")
	private int unitPrice;

	/**
	 * 商品优惠金额 discount_amount int 是 商品优惠金额 示例值：0
	 **/
	@JsonProperty("discount_amount")
	private int discountAmount;

	/**
	 * 商品备注 goods_remark string[1,128] 否 商品备注信息 示例值：商品备注信息
	 **/
	@Nullable
	@JsonProperty("goods_remark")
	private String goodsRemark;

}
