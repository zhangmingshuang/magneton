package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmsh 2022/4/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class WxPayNotification {

	/**
	 * 应用ID string[1,32] 是 直连商户申请的公众号或移动应用appid。 示例值：wxd678efh567hg6787
	 **/
	private String appid;

	/**
	 * 商户号 string[1,32] 是 商户的商户号，由微信支付生成并下发。 示例值：1230000109
	 **/
	private String mchid;

	/**
	 * 商户订单号 string[6,32] 是 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。 特殊规则：最小字符长度为6
	 * 示例值：1217752501201407033233368018
	 **/
	@JsonProperty("out_trade_no")
	private String outTradeNo;

	/**
	 * 微信支付订单号 string[1,32] 是 微信支付系统生成的订单号。 示例值：1217752501201407033233368018
	 **/
	@JsonProperty("transaction_id")
	private String transactionId;

	/**
	 * 交易类型 string[1,16] 是 交易类型，枚举值： JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付
	 * MWEB：H5支付 FACEPAY：刷脸支付 示例值：MICROPAY
	 **/
	@JsonProperty("trade_type")
	private String tradeType;

	/**
	 * 交易状态 string[1,32] 是 交易状态，枚举值： SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭
	 * REVOKED：已撤销（付款码支付） USERPAYING：用户支付中（付款码支付） PAYERROR：支付失败(其他原因，如银行返回失败) 示例值：SUCCESS
	 **/
	@JsonProperty("trade_state")
	private String tradeState;

	/**
	 * 交易状态描述 string[1,256] 是 交易状态描述 示例值：支付成功
	 **/
	@JsonProperty("trade_state_desc")
	private String tradeStateDesc;

	/**
	 * 付款银行 string[1,32] 是 银行类型，采用字符串类型的银行标识。银行标识请参考《银行类型对照表》 示例值：CICBC_DEBIT
	 **/
	@JsonProperty("bank_type")
	private String bankType;

	/**
	 * 附加数据 string[1,128] 否 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 * 示例值：自定义数据
	 **/
	@Nullable
	private String attach;

	/**
	 * 支付完成时间 string[1,64] 是
	 * 支付完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC
	 * 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
	 * 示例值：2018-06-08T10:34:56+08:00
	 **/
	@JsonProperty("success_time")
	private String successTime;

	/** +支付者 object 是 支付者信息 **/
	private Payer payer;

	/** +订单金额 object 是 订单金额信息 **/
	private ResponseAmount amount;

	/** +场景信息 object 否 支付场景信息描述 **/
	@JsonProperty("scene_info")
	@Nullable
	private ResponseSceneInfo sceneInfo;

	/** -优惠功能 array 否 优惠功能，享受优惠时返回该字段。 **/
	@JsonProperty("promotion_detail")
	private List<PromotionDetail> promotionDetail;

}
