package org.magneton.enhance.sms;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public enum SendStatus {

	/**
	 * 成功
	 */
	SUCCESS,
	/**
	 * 风险
	 */
	RISK,
	/**
	 * 次数上限
	 */
	COUNT_CAPS,
	/**
	 * 发送间隔
	 */
	SEND_GAP,
	/**
	 * 临时禁用
	 */
	TEMPORARILY_DISABLE,
	/**
	 * 失败
	 */
	FAILURE;

}
