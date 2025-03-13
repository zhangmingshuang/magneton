package org.magneton.framework.core.safedog.access;

/**
 * 访问器.
 *
 * @apiNote 该接口用来实现访问的控制。
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 * @see SafeDogAccessorProcessor
 */
public interface SafeDogAccessor {

	/**
	 * 锁定判断
	 * @return 如果被锁定，返回{@code true} ，否则返回 {@code false}.
	 */
	boolean locked();

	/**
	 * 获取被锁定的Key的剩余锁定时间
	 * @return 被锁定的Key的剩余锁定时间，毫秒。如果没有锁定，则返回 {@code -1} .
	 */
	long ttl();

	/**
	 * 记录错误
	 * @return 剩余的可错误次数。如果没有次数，则返回 {@code 0}。此时已经被锁定。
	 */
	int onError();

	/**
	 * 重置
	 */
	void reset();

}
