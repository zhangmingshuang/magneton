package org.magneton.enhance.pay.wxv3.profitsharing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.enhance.pay.wxv3.core.BaseV3Data;

import java.util.List;

/**
 * 微信分账请求
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WxProfitSharingOrdersReq extends BaseV3Data {

	/**
	 * 微信订单号 transaction_id string[1, 32] 是 body微信支付订单号 示例值：4208450740201411110007820472
	 */
	@JsonProperty("transaction_id")
	private String transactionId;

	/**
	 * 商户分账单号 out_order_no string[1,64] 是
	 * body商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@ 示例值：P20150806125346
	 */
	@JsonProperty("out_order_no")
	private String outOrderNo;

	/**
	 * +分账接收方列表 receivers array 是 body分账接收方列表，可以设置出资商户作为分账接受方，最多可有50个分账接收方
	 *
	 */
	private List<Receiver> receivers;

	/**
	 * 是否解冻剩余未分资金 unfreeze_unsplit boolean 是 body
	 *
	 * 1、如果为true，该笔订单剩余未分账的金额会解冻回分账方商户；
	 * <p>
	 * 2、如果为false，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。
	 *
	 * 示例值：true
	 */
	@JsonProperty("unfreeze_unsplit")
	private boolean unfreezeUnsplit;

}
