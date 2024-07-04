package org.magneton.enhance.pay.wxv3.refund.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 退款
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_9.shtml
 *
 * 请求方式：POST 接口频率：150qps
 *
 * @author zhangmsh 2022/7/7
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class DomesticRefundsReq {

	/**
	 * 微信支付订单号 transaction_id string[1, 32] 二选一 body原支付交易对应的微信订单号
	 * 示例值：1217752501201407033233368018
	 **/
	@JsonProperty("transaction_id")
	private String transactionId;

	/**
	 * 商户订单号 out_trade_no string[6, 32] body原支付交易对应的商户订单号 示例值：1217752501201407033233368018
	 **/
	@JsonProperty("out_trade_no")
	private String outTradeNo;

	/**
	 * 商户退款单号 out_refund_no string[1, 64] 是 body商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@
	 * ，同一退款单号多次请求只退一笔。 示例值：1217752501201407033233368018
	 */
	@JsonProperty("out_refund_no")
	private String outRefundNo;

	/**
	 * 退款原因 reason string[1, 80] 否 body若商户传入，会在下发给用户的退款消息中体现退款原因 示例值：商品已售完
	 **/
	@Nullable
	private String reason;

	/**
	 * 退款结果回调url notify_url string[8, 256] 否
	 * body异步接收微信支付退款结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	 * 如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效，优先回调当前传的这个地址。 示例值：https://weixin.qq.com
	 **/
	@Nullable
	@JsonProperty("notify_url")
	private String notifyUrl;

	/**
	 * 退款资金来源 funds_account string[1,32] 否
	 * body若传递此参数则使用对应的资金账户退款，否则默认使用未结算资金退款（仅对老资金流商户适用） 枚举值： AVAILABLE：可用余额账户
	 * 示例值：AVAILABLE
	 **/
	@Nullable
	@JsonProperty("funds_account")
	private String fundsAccount;

	/** 金额信息 amount object 是 body订单金额信息 */
	private RefundAmountReq amount;

	/** 退款商品 goods_detail array 否 body指定商品退款需要传此参数，其他场景无需传递 */
	@Nullable
	@JsonProperty("goods_detail")
	private List<RefundGoodsDetailReq> goodsDetails;

}
