package org.magneton.module.sms;

import javax.annotation.Nullable;

import org.magneton.core.Consequences;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public interface Sms {

	String DEFAULT_GROUP = "default";

	boolean isMobile(String mobile);

	/**
	 * 尝试发送短信
	 * @param mobile 手机号
	 * @return 是否发送成功
	 */
	default Consequences<SendStatus> trySend(String mobile) {
		return this.trySend(mobile, DEFAULT_GROUP);
	}

	/**
	 * 尝试发送短信
	 * @param mobile 手机号
	 * @param group 手机号分组
	 * @return 是否发送成功
	 */
	Consequences<SendStatus> trySend(String mobile, String group);

	/**
	 * 获取下次可发送短信间隔
	 * @param mobile 手机号
	 * @return 下次可以发送短信的时间间隔，单位秒。
	 */
	long ttl(String mobile);

	/**
	 * 剩余可错误次数
	 * @param mobile 手机号
	 * @return 剩余的可错误次数，如果不限制则返回-1，返回0表示没有可错误次数了，此时手机号表示为临时禁用状态{@link SendStatus#TEMPORARILY_DISABLE}。
	 */
	long remainErrors(String mobile);

	/**
	 * 发送短信
	 * @param mobile 手机号
	 * @return 发送状态
	 */
	Consequences<SendStatus> send(String mobile);

	@Nullable
	default String token(String mobile) {
		return this.token(mobile, DEFAULT_GROUP);
	}

	/**
	 * 获取最后发送成功的Token
	 * @param mobile 手机号
	 * @param group 手机号分组
	 * @return 最后发送成功的Token，如果Token不存在或者已经过期，则返回 {@code null}
	 */
	@Nullable
	String token(String mobile, String group);

	/**
	 * 校验
	 * @param token 授权码
	 * @param mobile 手机号
	 * @param code 登录验证码
	 * @return 是否有效。
	 */
	boolean validate(String token, String mobile, String code);

}
