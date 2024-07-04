package org.magneton.enhance.pay.wxv3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.magneton.enhance.pay.wxv3.core.BaseV3PayIdData;

import javax.annotation.Nullable;

/**
 * 微服支付的预下单请求.
 *
 * 字段与微信API保持一致，所以可能不符合标准的JAVA名称定义。
 *
 * 请求JSON示例：{@code https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_2_1.shtml} <pre>
 * {
 * 	"mchid": "1900006XXX",
 * 	"out_trade_no": "APP1217752501201407033233368018",
 * 	"appid": "wxb4ba3c02aa476XXX",
 * 	"description": "Image形象店-深圳腾大-QQ公仔",
 * 	"notify_url": "https://weixin.qq.com/",
 * 	"amount": {
 * 		"total": 1,
 * 		"currency": "CNY"
 *        }
 * }
 * </pre>
 *
 * @author zhangmsh 28/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@Accessors(chain = true)
@ToString(callSuper = true)
public class WxPayAppPrepayReq extends BaseV3PayIdData {

	/**
	 * 商品描述 string[1,127]
	 *
	 * 示例值：Image形象店-深圳腾大-QQ公仔
	 */
	private String description;

	/**
	 * 商户订单号 string[6,32]
	 *
	 * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
	 *
	 * 示例值：1217752501201407033233368018
	 */
	@JsonProperty("out_trade_no")
	private String outTradeNo;

	/**
	 * 交易结束时间 string[1,64]
	 *
	 * 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，
	 *
	 * yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，
	 *
	 * TIMEZONE表示时区（+08:00表示东八区时间，领先UTC8小时，即北京时间）。
	 *
	 * 例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
	 *
	 * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，
	 *
	 * 所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则 。
	 *
	 * 建议：最短失效时间间隔大于1分钟
	 *
	 * 示例值：2018-06-08T10:34:56+08:00
	 */
	@Nullable
	@JsonProperty("time_expire")
	private String timeExpire;

	/**
	 * 附加数据 string[1,128]
	 *
	 * 在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
	 *
	 * 示例值：自定义数据
	 */
	@Nullable
	private String attach;

	/**
	 * 通知地址 string[1,256]
	 *
	 * 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
	 *
	 * 格式：URL
	 *
	 * 示例值：https://www.weixin.qq.com/wxpay/pay.php
	 *
	 * 如果为{@code null}则使用配置的URL地址
	 */
	@Nullable
	@JsonProperty("notify_url")
	private String notifyUrl;

	/**
	 * 订单优惠标记 string[1,32]
	 *
	 * 示例值：WXG
	 */
	@Nullable
	@JsonProperty("goods_tag")
	private String goodsTag;

	/**
	 * 订单金额信息
	 */
	private Amount amount;

	/**
	 * 优惠功能
	 */
	@Nullable
	private Detail detail;

	/**
	 * 支付场景描述
	 */
	@Nullable
	@JsonProperty("scene_info")
	private SceneInfo sceneInfo;

	/**
	 * 结算信息
	 */
	@Nullable
	@JsonProperty("settle_info")
	private SettleInfo settleInfo;

}
