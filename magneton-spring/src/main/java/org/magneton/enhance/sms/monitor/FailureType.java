package org.magneton.enhance.sms.monitor;

/**
 * 失败类型.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public enum FailureType {

	/**
	 * 发送间隔内
	 */
	SEND_GAP,
	/**
	 * 分组风控
	 */
	GROUP_RISK,
	/**
	 * 天上限
	 */
	DAY_COUNT_CAPS,
	/**
	 * 小时上限
	 */
	HOUR_COUNT_CAPS,
	/**
	 * 手机号格式错误
	 */
	MOBILE_REGEX

}
