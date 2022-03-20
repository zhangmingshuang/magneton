package org.magneton.support.api.auth.constant;

import org.magneton.core.ResponseMessage;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
public enum CommonError implements ResponseMessage {

	SIGN_PARAM_MISS("1002001", "签名参数丢失"),

	SIGN_ERROR("1002002", "签名有误"),

	SIGN_NEED_PARAM_MISS("1002003", "签名必须参数丢失"),

	TIME_OUT("1002004", "时间超出限制"),

	SECRET_KEY_ID_PARAM_MISS("1002005", "SecretKeyId参数丢失"),

	SECRET_KEY_MISS("1002006", "SecretKey丢失"),

	SECRET_TIME_OUT("1002101", "获取签名摘要接口时间超出限制"),

	SECRET_SIGN_ERROR("1002102", "获取签名摘要接口签名有误");

	private String code;

	private String message;

	CommonError(String code, String message) {
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
