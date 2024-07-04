package org.magneton.enhance.auth;

import javax.annotation.Nullable;

/**
 * Token鉴权
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public interface TokenAuth {

	/**
	 * 登录并返回Token
	 * @param obj 保存对象
	 * @param <A> 泛型
	 * @return 对应Token
	 */
	<A> String login(A obj);

	/**
	 * 使用指定Token登录
	 * @param token 指定Token
	 * @param obj 保存对象
	 * @param <A> 泛型
	 * @return 如果指定的Token已经登录，则会覆盖原来的登录信息，返回原来的登录信息，否则返回null
	 */
	@Nullable
	<A> A login(String token, A obj);

	/**
	 * 判断Token是否有登录
	 * @param token Token
	 * @return 如果已经登录，则返回true，否则返回false
	 */
	boolean isLogin(String token);

	/**
	 * 获取登录信息
	 * @param token 指定Token
	 * @param <A> 泛型
	 * @return 如果已经登录，则返回登录信息，否则返回null
	 */
	@Nullable
	<A> A getLogin(String token);

}