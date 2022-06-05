package org.magneton.module.pay.wechat.v3.profitsharing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 微信分账处理结果
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WxProfitSharingOrders {

	/**
	 * 微信订单号 transaction_id string[1, 32] 是 微信支付订单号 示例值：4208450740201411110007820472
	 */

	@JsonProperty("transaction_id")
	private String transactionId;

	/**
	 * 商户分账单号 out_order_no string[1, 64] 是
	 * 商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@ 示例值：P20150806125346
	 */
	@JsonProperty("out_order_no")
	private String outOrderNo;

	/**
	 * 微信分账单号 order_id string[1, 64] 是 微信分账单号，微信支付系统返回的唯一标识
	 * 示例值：3008450740201411110007820472
	 */
	@JsonProperty("order_id")
	private String orderId;

	/**
	 * 分账单状态 state string[1, 32] 是 分账单状态（每个接收方的分账结果请查看receivers中的result字段），枚举值：
	 * 1、PROCESSING：处理中 2、FINISHED：分账完成 示例值：FINISHED
	 * @see org.magneton.module.pay.wechat.v3.core.WxProfitSharingState
	 */
	private String state;

	/**
	 * +分账接收方列表 receivers array 否 分账接收方列表
	 */
	@Nullable
	private List<ResponseReceiver> receivers;

}
