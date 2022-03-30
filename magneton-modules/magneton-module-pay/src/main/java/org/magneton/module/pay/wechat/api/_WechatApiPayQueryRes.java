package org.magneton.module.pay.wechat.api;

import java.util.List;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.pay.wechat.WechatTradeType;

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
public class _WechatApiPayQueryRes {

	/**
	 * 服务商应用ID string[1,32] 是 服务商申请的公众号或移动应用appid。 示例值：wx8888888888888888
	 **/
	private String sp_appid;

	/**
	 * 服务商户号 string[1,32] 是 服务商户号，由微信支付生成并下发 示例值：1230000109
	 **/
	private String sp_mchid;

	/**
	 * 子商户应用ID string[1,32] 否 子商户申请的公众号或移动应用appid。 示例值：wxd678efh567hg6999
	 **/
	@Nullable
	private String sub_appid;

	/**
	 * 子商户号 string[1,32] 是 子商户的商户号，由微信支付生成并下发。 示例值：1900000109
	 **/
	private String sub_mchid;

	/**
	 * 商户订单号 string[6,32] 是 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一，详见【商户订单号】。
	 *
	 * 特殊规则：最小字符长度为6 示例值：1217752501201407033233368018
	 **/
	private String out_trade_no;

	/**
	 * 微信支付订单号 string[1,32] 否 微信支付系统生成的订单号。 示例值：1217752501201407033233368018
	 **/
	@Nullable
	private String transaction_id;

	/**
	 * 交易类型 string[1,16] 否 交易类型，
	 *
	 * 枚举值： JSAPI：公众号支付 NATIVE：扫码支付 APP：APP支付 MICROPAY：付款码支付 MWEB：H5支付 FACEPAY：刷脸支付
	 * 示例值：MICROPAY
	 * @see WechatTradeType
	 **/
	@Nullable
	private String trade_type;

	/**
	 * 交易状态 string[1,32] 是 交易状态，
	 *
	 * 枚举值： SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付 CLOSED：已关闭 REVOKED：已撤销（仅付款码支付会返回）
	 * USERPAYING：用户支付中（仅付款码支付会返回） PAYERROR：支付失败（仅付款码支付会返回）
	 *
	 * 示例值：SUCCESS
	 **/
	private String trade_state;

	/**
	 * 交易状态描述 string[1,256] 是 交易状态描述 示例值：支付成功
	 **/
	private String trade_state_desc;

	/**
	 * 付款银行 string[1,32] 否 银行类型，采用字符串类型的银行标识。 银行标识请参考《银行类型对照表》 示例值：CMC
	 **/
	@Nullable
	private String bank_type;

	/**
	 * 附加数据 string[1,128] 否 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 * 示例值：自定义数据
	 **/
	@Nullable
	private String attach;

	/**
	 * 支付完成时间 string[1,64] 否
	 * 支付完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，
	 *
	 * T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，
	 *
	 * 领先UTC 8小时，即北京时间）。
	 *
	 * 例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
	 *
	 * 示例值：2018-06-08T10:34:56+08:00
	 **/
	@Nullable
	private String success_time;

	/** 支付者 object 是 支付者信息 **/
	private Player payer;

	/**
	 * 订单金额 object 否 订单金额信息，当支付成功时返回该字段。
	 */
	@Nullable
	private Amount amount;

	/**
	 * -场景信息 object 否 支付场景描述
	 */
	@Nullable
	private SceneInfo scene_info;

	/**
	 * 优惠功能 array 否 优惠功能，享受优惠时返回该字段。
	 */
	@Nullable
	private List<PromotionDetail> promotion_detail;

	// 优惠功能
	@Setter
	@Getter
	@ToString
	public static class PromotionDetail {

		/**
		 * 券ID string[1,32] 是 券ID 示例值：109519
		 **/
		private String coupon_id;

		/**
		 * 优惠名称 string[1,64] 否 优惠名称 示例值：单品惠-6
		 **/
		@Nullable
		private String name;

		/**
		 * 优惠范围 string[1,32] 否 GLOBAL：全场代金券 SINGLE：单品优惠 示例值：GLOBAL
		 **/
		@Nullable
		private String scope;

		/**
		 * 优惠类型 string[1,32] 否 CASH：充值 NOCASH：预充值 示例值：CASH
		 **/
		@Nullable
		private String type;

		/**
		 * 优惠券面额 int 是 优惠券面额 示例值：100
		 **/
		private int amount;

		/**
		 * 活动ID string[1,32] 否 活动ID 示例值：931386
		 **/
		@Nullable
		private String stock_id;

		/**
		 * 微信出资 int 否 微信出资，单位为分 示例值：0
		 **/
		private int wechatpay_contribute;

		/**
		 * 商户出资 int 否 商户出资，单位为分 示例值：0
		 **/
		private int merchant_contribute;

		/**
		 * 其他出资 int 否 其他出资，单位为分 示例值：0
		 **/
		private int other_contribute;

		/**
		 * 优惠币种 string[1,16] 否 CNY：人民币，境内商户号仅支持人民币。 示例值：CNY
		 **/
		@Nullable
		private String currency;

		/**
		 * -单品列表 array 否 单品列表信息
		 */
		@Nullable
		private List<GoodsDetail> goods_detail;

	}

	// 单品列表
	@Setter
	@Getter
	@ToString
	public static class GoodsDetail {

		/**
		 * 商品编码 string[1,32] 是 商品编码 示例值：M1006
		 **/
		private String goods_id;

		/**
		 * 商品数量 int 是 用户购买的数量 示例值：1
		 **/
		private int quantity;

		/**
		 * 商品单价 int 是 商品单价，单位为分 示例值：100
		 **/
		private int unit_price;

		/**
		 * 商品优惠金额 int 是 商品优惠金额 示例值：0
		 **/
		private int discount_amount;

		/**
		 * 商品备注 string[1,128] 否 商品备注信息 示例值：商品备注信息
		 **/
		@Nullable
		private String goods_remark;

	}

	// 场景信息
	@Setter
	@Getter
	@ToString
	public static class SceneInfo {

		/**
		 * 商户端设备号 string[1,32] 否 商户端设备号（发起扣款请求的商户服务器设备号）。 示例值：013467007045764
		 */
		private String device_id;

	}

	// 订单金额
	@Setter
	@Getter
	@ToString
	public static class Amount {

		/**
		 * 总金额 int 否 订单总金额，单位为分。 示例值：100
		 **/
		private int total;

		/**
		 * 用户支付金额 int 否 用户支付金额，单位为分。（指使用优惠券的情况下，这里等于总金额-优惠券金额） 示例值：100
		 **/
		private int payer_total;

		/**
		 * 货币类型 string[1,16] 否 CNY：人民币，境内商户号仅支持人民币。 示例值：CNY
		 **/
		private String currency;

		/**
		 * 用户支付币种 string[1,16] 否 用户支付币种 示例值：CNY
		 **/
		private String payer_currency;

	}

	// 支付者
	@Setter
	@Getter
	@ToString
	public static class Player {

		/**
		 * 用户服务标识 string[1,128] 是 用户在服务商appid下的唯一标识。 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
		 **/
		private String sp_openid;

		/**
		 * 用户子标识 string[1,128] 否 用户在子商户appid下的唯一标识。如果返回sub_appid，那么sub_openid一定会返回
		 * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
		 **/
		@Nullable
		private String sub_openid;

	}

}
