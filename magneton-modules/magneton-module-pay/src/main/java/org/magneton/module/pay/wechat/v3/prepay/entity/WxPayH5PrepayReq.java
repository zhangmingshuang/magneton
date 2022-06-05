package org.magneton.module.pay.wechat.v3.prepay.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.magneton.module.pay.wechat.v3.core.BaseV3PayIdData;

/**
 * 微信H5预支付.
 *
 * https://pay.weixin.qq.com/wiki/doc/apiv3/apis/chapter3_3_1.shtml
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class WxPayH5PrepayReq extends BaseV3PayIdData {

	/**
	 * 商品描述 description string[1,127] 是 body 商品描述 示例值：Image形象店-深圳腾大-QQ公仔
	 **/
	private String description;

	/**
	 * 商户订单号 out_trade_no string[6,32] 是 body 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
	 * 示例值：1217752501201407033233368018
	 **/
	@JsonProperty("out_trade_no")
	private String outTradeNo;

	/**
	 * 交易结束时间 time_expire string[1,64] 否 body
	 * 订单失效时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日
	 * 13点29分35秒。
	 * 订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则
	 * 。 建议：最短失效时间间隔大于1分钟 示例值：2018-06-08T10:34:56+08:00
	 **/
	@Nullable
	@JsonProperty("time_expire")
	private String timeExpire;

	/**
	 * 附加数据 attach string[1,128] 否 body
	 * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。 示例值：自定义数据
	 **/
	@Nullable
	private String attach;

	/**
	 * 通知地址 notify_url string[1,256] 是 body异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
	 * 公网域名必须为https，如果是走专线接入，使用专线NAT IP或者私有回调域名可使用http
	 *
	 */
	@JsonProperty("notify_url")
	private String notifyUrl;

	/**
	 * 订单优惠标记 goods_tag string[1,32] 否 body 订单优惠标记 示例值：WXG
	 **/
	@Nullable
	@JsonProperty("goods_tag")
	private String goodsTag;

	/** +订单金额 amount object 是 body 订单金额信息 **/
	private Amount amount;

	/** +支付者 payer object 是 body 支付者信息 **/
	private Payer payer;

	/** +优惠功能 detail object 否 body 优惠功能 **/
	@Nullable
	private Detail detail;

	/** +场景信息 scene_info object 否 body 支付场景描述 **/
	@Nullable
	@JsonProperty("scene_info")
	private SceneInfo sceneInfo;

	/**
	 * +结算信息 settle_info object 否 body 结算信息
	 */
	@Nullable
	@JsonProperty("settle_info")
	private SettleInfo settleInfo;

}
