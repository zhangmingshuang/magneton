package org.magneton.module.pay.wechat.entity;

import java.util.List;

import javax.annotation.Nullable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
@ToString
@Accessors(chain = true)
public class WechatPreOrderReq {

	/**
	 * 应用ID string[1,32]
	 *
	 * 由微信生成的应用ID，全局唯一。请求基础下单接口时请注意APPID的应用属性，直连模式下该id应为APP应用的id。
	 *
	 * 示例值：wxd678efh567hg6787
	 */
	private String appid;

	/**
	 * 直连商户号 string[1,32]
	 *
	 * 直连商户的商户号，由微信支付生成并下发。
	 *
	 * 示例值：1230000109
	 */
	private String mchid;

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
	private String out_trade_no;

	/**
	 * 通知地址 string[1,256]
	 *
	 * 通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
	 *
	 * 格式：URL
	 *
	 * 示例值：https://www.weixin.qq.com/wxpay/pay.php
	 */
	private String notify_url;

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
	private String time_expire;

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
	private SceneInfo scene_info;

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
	 * 订单优惠标记 string[1,32]
	 *
	 * 示例值：WXG
	 */
	private String goods_tag;

	// --------------------------------------------------------
	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	// 订单金额
	public static class Amount {

		/**
		 * 总金额
		 *
		 * 订单总金额，单位为分。 示例值：100
		 */
		private int total;

		/**
		 * 货币类型 string[1,16]
		 *
		 * CNY：人民币，境内商户号仅支持人民币。 示例值：CNY
		 */
		private String currency = "CNY";

	}

	// -----------------------------------------
	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	// 优惠功能
	public static class Detail {

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
		private Integer cost_price;

		/**
		 * 商品小票ID string[1,32] 示例值：微信123
		 */
		@Nullable
		private String invoice_id;

		/**
		 * 单品列表信息 条目个数限制：【1，6000】
		 */
		@Nullable
		private List<GoodsDetail> goods_detail;

	}

	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	// 单品列表
	public static class GoodsDetail {

		/**
		 * 商户侧商品编码 string[1,32 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。 示例值：1246464644
		 **/
		private String merchant_goods_id;

		/**
		 * 微信支付商品编码 string[1,32] 微信支付定义的统一商品编号（没有可不传） 示例值：1001
		 **/
		@Nullable
		private String wechatpay_goods_id;

		/**
		 * 商品名称 string[1,256] 商品的实际名称 示例值：iPhoneX 256G
		 **/
		@Nullable
		private String goods_name;

		/**
		 * 商品数量 用户购买的数量 示例值：1
		 **/
		private int quantity;

		/**
		 * 商品单价 ，单位为分 示例值：828800
		 **/
		private int unit_price;

	}
	// ---------------------------------------

	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	// 场景信息
	public static class SceneInfo {

		/**
		 * 用户终端IP string[1,45] 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。 示例值：14.23.150.211
		 **/
		private String payer_client_ip;

		/**
		 * 商户端设备号 string[1,32] 商户端设备号（门店号或收银设备ID）。 示例值：013467007045764
		 **/
		@Nullable
		private String device_id;

		/**
		 * 商户门店信息
		 */
		@Nullable
		private StoreInfo store_info;

	}

	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	// 商户门店信息
	public static class StoreInfo {

		/**
		 * 门店编号 string[1,32] 商户侧门店编号 示例值：0001
		 **/
		private String id;

		/**
		 * 门店名称 string[1,256] 商户侧门店名称 示例值：腾讯大厦分店
		 **/
		@Nullable
		private String name;

		/**
		 * 地区编码 string[1,32] 地区编码，详细请见省市区编号对照表。 示例值：440305
		 **/
		@Nullable
		private String area_code;

		/**
		 * 详细地址 string[1,512] 详细的商户门店地址 示例值：广东省深圳市南山区科技中一道10000号
		 **/
		@Nullable
		private String address;

	}

	// --------------------------------------

	@Setter
	@Getter
	@ToString
	@Accessors(chain = true)
	// 结算信息
	public static class SettleInfo {

		/**
		 * 是否指定分账 是否指定分账 示例值：false
		 **/
		private boolean profit_sharing;

	}

}
