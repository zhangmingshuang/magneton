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

	MOBILE_RISK("1000900", "手机号存在风险");

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

}
