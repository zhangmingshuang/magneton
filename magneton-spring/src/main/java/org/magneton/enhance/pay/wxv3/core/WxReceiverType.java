package org.magneton.enhance.pay.wxv3.core;

/**
 * 分账接收方类型
 *
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
public enum WxReceiverType {

	/**
	 * 商户号
	 */
	MERCHANT_ID,
	/**
	 * 个人openid（由父商户APPID转换得到）
	 */
	PERSONAL_OPENID

}
