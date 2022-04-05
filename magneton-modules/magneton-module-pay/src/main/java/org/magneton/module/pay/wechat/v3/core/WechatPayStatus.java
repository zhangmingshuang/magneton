package org.magneton.module.pay.wechat.v3.core;

/**
 * 微信支付狀態 .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
public enum WechatPayStatus {

	SUCCESS(0, "支付成功"),

	REFUND(1, "转入退款"),

	NOTPAY(2, "未支付"),

	CLOSED(3, "已关闭"),
	/**
	 * （仅付款码支付会返回）
	 */
	REVOKED(4, "已撤销"),
	/**
	 * （仅付款码支付会返回）
	 */
	USERPAYING(5, "用户支付中"),
	/**
	 * （仅付款码支付会返回）
	 */
	PAYERROR(6, "支付失败");

	private final int code;

	private final String description;

	WechatPayStatus(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return this.code;
	}

	public String getDescription() {
		return this.description;
	}

}
