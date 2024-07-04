package org.magneton.enhance.pay.wxv3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.enhance.pay.wxv3.core.BaseV3PayIdData;
import org.magneton.enhance.pay.wxv3.core.WxPayStatus;
import org.magneton.enhance.pay.wxv3.core.WxPayTradeType;

import javax.annotation.Nullable;

/**
 * 查询订单响应数据
 *
 * API文档：{@code https://pay.weixin.qq.com/wiki/doc/apiv3_partner/apis/chapter4_2_2.shtml}
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
public class WxPayOrder extends BaseV3PayIdData {

	/**
	 * 商户订单号 out_trade_no string[6,32] 是 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一，详见【商户订单号】。
	 * 示例值：1217752501201407033233368018
	 **/
	@JsonProperty("out_trade_no")
	private String outTradeNo;

	/**
	 * 微信支付订单号 transaction_id string[1,32] 否 微信支付系统生成的订单号。
	 * 示例值：1217752501201407033233368018
	 **/
	@Nullable
	@JsonProperty("transaction_id")
	private String transactionId;

	/**
	 * 交易类型 trade_type string[1,16] 否 交易类型，枚举值： JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付
	 * MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付 示例值：MICROPAY
	 * @see WxPayTradeType
	 **/
	@JsonProperty("trade_type")
	@Nullable
	private String tradeType;

	/**
	 * 交易状态 trade_state string[1,32] 是 交易状态，枚举值： SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付
	 * CLOSED：已关闭 REVOKED：已撤销（仅付款码支付会返回） USERPAYING：用户支付中（仅付款码支付会返回）
	 * PAYERROR：支付失败（仅付款码支付会返回） 示例值：SUCCESS
	 *
	 * @see WxPayStatus
	 **/
	@JsonProperty("trade_state")
	private String tradeState;

	/**
	 * 交易状态描述 trade_state_desc string[1,256] 是 交易状态描述 示例值：支付成功
	 **/
	@JsonProperty("trade_state_desc")
	private String tradeStateDesc;

	/**
	 * 付款银行 bank_type string[1,32] 否 银行类型，采用字符串类型的银行标识。银行标识请参考《银行类型对照表》 示例值：CMC
	 **/
	@Nullable
	@JsonProperty("bank_type")
	private String bankType;

	/**
	 * 附加数据 attach string[1,128] 否 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 * 示例值：自定义数据
	 **/
	@Nullable
	private String attach;

	/**
	 * 支付完成时间 success_time string[1,64] 否
	 * 支付完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
	 * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
	 * 示例值：2018-06-08T10:34:56+08:00
	 **/
	@Nullable
	@JsonProperty("success_time")
	private String successTime;

}
