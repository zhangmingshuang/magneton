package org.magneton.enhance.safedog.geetest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 极验验证请求体.
 *
 * @author zhangmsh
 * @since 2.0.8
 * @see GeeTestBehaviorValidator
 */
@Setter
@Getter
@ToString
public class GeeTestBody {

	/**
	 * 验证流水号
	 */
	private String lotNumber;

	/**
	 * 验证输出信息
	 */
	private String captchaOutput;

	/**
	 * 验证通过标识
	 */
	private String passToken;

	/**
	 * 验证通过时间戳
	 */
	private String genTime;

}