package org.magneton.support.api.auth.constant;

import org.magneton.core.ResponseMessage;

/**
 * Api Auth Error Code.
 *
 * @author zhangmsh 15/03/2022
 * @since 2.0.7
 */
public enum SmsError implements ResponseMessage {

	PARAM_MOBILE_BLANK("1000001", "手机号不能为空"),

	PARAM_MOBILE_ERROR("1000002", "手机号格式不正确"),

	SMS_CODE_ERROR("1000003", "验证码有误，请重新输入"),

	MOBILE_RISK("1000004", "手机号存在风险"),

	MOBIL_SEND_GAP("1000005", "两次短信发送时间间隔太短");

	private String code;

	private String message;

	SmsError(String code, String message) {
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

	// for smart-doc
	public String getCode() {
		return this.code;
	}

	// for smart-doc
	public String getMessage() {
		return this.name() + " " + this.message;
	}

}
