package org.magneton.enhance.safedog;

import com.google.common.base.Strings;
import org.magneton.core.ResultBody;

/**
 * 模型错误代码
 *
 * @author zhangmsh.
 * @since M2024
 */
public enum SafeDogErr implements ResultBody {

	// ============== 签名 ==============

	SIGN_USED("200101", "签名已经被使用过了"),

	SIGN_INCONSISTENT("200102", "签名不一致"),

	SIGN_REQUIRE_KEY_MISS("200103", "缺少必须的key: %s"),

	// ============== 行为验证 ==============
	GEETEST_RESPONSE_EMPTY("200201", "极验响应为空"),

	GEETEST_RESPONSE_ERROR("200202", "极验响应未知数据错误");

	private final String code;

	private final String message;

	SafeDogErr(String code, String message) {
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

	public <E> ResultBody<E> withArgs(Object... missKeys) {
		return ResultBody.valueOf(this.code, Strings.lenientFormat(this.message, missKeys));
	}

}