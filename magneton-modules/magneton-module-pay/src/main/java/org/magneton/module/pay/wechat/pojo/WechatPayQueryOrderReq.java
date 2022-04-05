package org.magneton.module.pay.wechat.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@Accessors(chain = true)
public class WechatPayQueryOrderReq {

	/**
	 * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。 特殊规则：最小字符长度为6 示例值：1217752501201407033233368018
	 */
	private String outTradeNo;

	/**
	 * 服务商户号 string[1,32] 是 query 服务商户号，由微信支付生成并下发 示例值：1230000109
	 **/
	private String spMchId;

	/**
	 * 子商户号 string[1,32] 是 query 子商户的商户号，由微信支付生成并下发。 示例值：1900000109
	 **/
	private String subMchId;

}
