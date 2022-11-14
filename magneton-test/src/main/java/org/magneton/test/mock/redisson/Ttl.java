package org.magneton.test.mock.redisson;

/**
 * .
 *
 * @author zhangmsh
 * @since 2021/11/17
 */
public interface Ttl {

	/**
	 * TTL 没有过期
	 */
	int NON_EXPIRE = -1;

	/**
	 * TTL 不存在
	 */
	int NON_EXIST = -2;

}
