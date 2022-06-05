package org.magneton.module.pay.wechat.v3.profitsharing.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分账接收方
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class Receiver {

	/**
	 * 分账接收方类型 type string[1, 32] 是 示例值：MERCHANT_ID
	 * @see org.magneton.module.pay.wechat.v3.core.WxReceiverType
	 */
	private String type;

	/**
	 * 分账接收方账号 account string[1, 64] 是
	 *
	 * 分账接收方账号： 1、类型是MERCHANT_ID时，是商户号（mch_id或者sub_mch_id）
	 * <p>
	 * 2、类型是PERSONAL_OPENID时，是个人openid openid获取方法
	 *
	 * 示例值：86693852
	 */
	private String account;

	/**
	 * 分账个人接收方姓名 name string[1, 1024] 否 可选项，
	 *
	 * 在接收方类型为个人的时可选填，若有值，会检查与 name 是否实名匹配，不匹配会拒绝分账请求
	 *
	 * <ul>
	 * <li>1、分账接收方类型是PERSONAL_OPENID，是个人姓名的密文（选传，传则校验） 此字段的加密方法详见：敏感信息加密说明</li>
	 * <li>2、使用微信支付平台证书中的公钥</li>
	 * <li>3、使用RSAES-OAEP算法进行加密 4、将请求中HTTP头部的Wechatpay-Serial设置为证书序列号
	 * <li>
	 * </ul>
	 * 示例值：hu89ohu89ohu89o
	 */
	private String name;

	/**
	 * 分账金额 amount int 是 分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额 示例值：888
	 */
	private int amount;

	/**
	 * 分账描述 description string[1, 80] 是 分账的原因描述，分账账单中需要体现 示例值：分给商户A
	 */
	private String description;

}
