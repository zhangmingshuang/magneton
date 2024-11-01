package org.magneton.enhance.auth;

import javax.annotation.Nullable;

/**
 * Token鉴权
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public interface MTokenAuth {

	/**
	 * 登录并返回Token
	 * @param obj 保存对象
	 * @param <A> 泛型
	 * @return 对应Token
	 */
	default <A> String login(A obj) {
		return this.login(obj, 3600);
	}

	/**
	 * 登录并返回Token
	 * @param obj 保存对象
	 * @param keepTime 保持时间，单位秒
	 * @param <A> 泛型
	 * @return 对应Token
	 */
	<A> String login(A obj, int keepTime);

	/**
	 * 使用指定Token登录
	 * @param token 指定Token
	 * @param obj 保存对象
	 * @param <A> 泛型
	 * @return 如果指定的Token已经登录，则会覆盖原来的登录信息，返回原来的登录信息，否则返回null
	 */
	@Nullable
	default <A> A login(String token, A obj) {
		return this.login(token, obj, 3600);
	}

	/**
	 * 使用指定Token登录
	 * @param token 指定Token
	 * @param obj 保存对象
	 * @param keepTime 保持时间，单位秒
	 * @param <A> 泛型
	 * @return 如果指定的Token已经登录，则会覆盖原来的登录信息，返回原来的登录信息，否则返回null
	 */
	<A> A login(String token, A obj, int keepTime);

	/**
	 * 判断Token是否有登录
	 * @param token Token
	 * @return 如果已经登录，则返回true，否则返回false
	 */
	boolean isLogin(String token);

	/**
	 * 获取登录信息
	 * @param token 指定Token
	 * @param clazz 登录信息类型
	 * @param <A> 泛型
	 * @return 如果已经登录，则返回登录信息，否则返回null
	 */
	@Nullable
	<A> A getLogin(String token, Class<A> clazz);

	/**
	 * 登出
	 * @param token 指定Token
	 */
	void logout(String token);

}