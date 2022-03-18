package org.magneton.support.api.auth.constant;

import org.magneton.core.ResponseMessage;

/**
 * .
 *
 * @author zhangmsh 18/03/2022
 * @since 2.0.7
 */
public enum LoginError implements ResponseMessage {

	AUTO_LOGIN_ERROR("1001001", "自动登录失败"),

	ACCOUNT_DISABLE("1001002", "账号被禁用"),

	LOGIN_EXPIRE("1001003", "登录已过期，请重新登录"),

	HEADER_MISS("1001004", "请求头丢失参数");

	private String code;

	private String message;

	LoginError(String code, String message) {
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
