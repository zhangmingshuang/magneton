package org.magneton.module.safedog.access;

/**
 * 时间计算器.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public interface AccessTimeCalculator {

	/**
	 * 计算锁定时间.
	 * @param name 访问器名称
	 * @param wrongs 当前错误次数
	 * @param accessConfig 访问配置
	 * @return 锁定时间，单位毫秒
	 */
	long calculate(String name, long wrongs, AccessConfig accessConfig);

}
