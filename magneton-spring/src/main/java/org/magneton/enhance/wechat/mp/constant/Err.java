package org.magneton.enhance.wechat.mp.constant;

import org.magneton.core.ResultBody;

/**
 * 全局错误码
 * <p>
 * <a href=
 * "https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Global_Return_Code.html">官方说明</a>
 *
 * @author zhangmsh.
 * @since 2024
 */
public enum Err implements ResultBody<Void> {

	/**
	 * 当signature, timestamp, nonce, echostr中有任意一个为空时，返回此错误
	 */
	PARAMETER_INVALID("200201", "请求参数非法，请核实!"),
	/**
	 * 当传入的appid进行配置切换时，未找到对应的appid配置时，返回此错误
	 */
	APPID_INVALID("200202", "未找到对应appid=[%s]的配置，请核实！"),

	/**
	 * 非法请求
	 */
	INVALID_REQUEST("200200", "非法请求"),

	/**
	 * 签名验证失败
	 */
	SIGNATURE_INVALID("200203", "签名验证失败"),
	/**
	 * 不是预期的加密类型
	 */
	ENCRYPT_TYPE_INVALID("200204", "加密类型非法");

	/**
	 * 错误Code
	 */
	private final String code;

	/**
	 * 错误信息
	 */
	private final String message;

	Err(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String code() {
		return this.code;
	}

	@Override
	public String message() {
		return this.message;
	}

	/**
	 * 系统繁忙，此时请开发者稍候再试
	 */
	public static final String SYSTEM_BUSY = "-1";

	/**
	 * 请求成功
	 */
	public static final String SUCCESS = "0";

}