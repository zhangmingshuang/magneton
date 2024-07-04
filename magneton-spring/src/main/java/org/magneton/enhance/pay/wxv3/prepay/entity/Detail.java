package org.magneton.enhance.pay.wxv3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.List;

// -----------------------------------------
@Setter
@Getter
@ToString
@Accessors(chain = true)
// 优惠功能
public class Detail {

	/**
	 * 订单原价
	 *
	 * 1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
	 *
	 * 2、当订单原价与支付金额不相等，则不享受优惠。
	 *
	 * 3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。
	 *
	 * 示例值：608800
	 */
	@Nullable
	@JsonProperty("cost_price")
	private Integer costPrice;

	/**
	 * 商品小票ID string[1,32] 示例值：微信123
	 */
	@Nullable
	@JsonProperty("invoice_id")
	private String invoiceId;

	/**
	 * 单品列表信息 条目个数限制：【1，6000】
	 */
	@Nullable
	@JsonProperty("goods_detail")
	private List<GoodsDetail> goodsDetail;

}
