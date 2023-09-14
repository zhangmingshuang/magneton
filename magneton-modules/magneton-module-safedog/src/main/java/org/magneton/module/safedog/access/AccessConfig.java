package org.magneton.module.safedog.access;

import lombok.*;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
@Setter(AccessLevel.PROTECTED)
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccessConfig {

	/**
	 * 允许的错误次数
	 */
	private int numberOfWrongs = 5;

	/**
	 * 错误的遗忘时间, 单位毫秒.
	 */
	private long wrongTimeToForget = 5 * 60 * 1000;

	/**
	 * 琐定时间，单位毫秒.
	 */
	private long lockTime = 5 * 60 * 1000;

	/**
	 * 时间计算器.
	 */
	private AccessTimeCalculator accessTimeCalculator;

}
