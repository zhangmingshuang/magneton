package org.magneton.framework;

/**
 * 有序.
 *
 * @author zhangmsh.
 * @since 2024
 */
public interface Ordered {

	/**
	 * 最高优先级.
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * 最低优先级.
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

	/**
	 * 获取排序.
	 * @return 排序
	 */
	int getOrder();

}