package org.magneton.module.kit.access;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
@Getter
@ToString
@AllArgsConstructor
public class AccessConfig {

	/**
	 * 允许的错误次数
	 */
	private int numberOfWrongs = 5;

	/**
	 * 错误的遗忘时间, 单位毫秒.
	 */
	private int wrongTimeToForget = 5 * 60 * 1000;

	/**
	 * 琐定时间，单位毫秒.
	 */
	private long lockTime = 5 * 60 * 1000;

	/**
	 * 时间计算器.
	 */
	private AccessTimeCalculator accessTimeCalculator;

}
