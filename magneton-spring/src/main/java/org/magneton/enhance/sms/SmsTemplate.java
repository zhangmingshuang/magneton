package org.magneton.enhance.sms;

import org.magneton.core.Result;

import javax.annotation.Nullable;

/**
 * 短信服务.
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public interface SmsTemplate {

	String DEFAULT_GROUP = "default";

	/**
	 * 是否是手机号
	 * @param mobile 手机号
	 * @return 是否是手机号,如果是手机号，则返回 {@code true} 是，否则 {@code false} 不是。
	 */
	boolean isMobile(String mobile);

	/**
	 * 尝试发送短信
	 * @param mobile 手机号
	 * @return 是否发送成功
	 */
	default Result<SendStatus> trySend(String mobile) {
		return this.trySend(mobile, DEFAULT_GROUP);
	}

	/**
	 * 尝试发送短信
	 * @param mobile 手机号
	 * @param group 手机号分组
	 * @return 是否发送成功
	 */
	Result<SendStatus> trySend(String mobile, String group);

	/**
	 * 发送短信
	 * @apiNote 与{@link #trySend(String)}不同的是，此方法不会进行任何风控处理，直接发送短信。
	 * @param mobile 手机号
	 * @return 发送状态
	 */
	Result<SendStatus> send(String mobile);

	/**
	 * 获取下次可发送短信间隔
	 * @param mobile 手机号
	 * @return 下次可以发送短信的时间间隔，单位秒。
	 */
	long nextTime(String mobile);

	/**
	 * 剩余可错误次数
	 * @param mobile 手机号
	 * @return 剩余的可错误次数，如果不限制则返回-1，返回0表示没有可错误次数了，此时手机号表示为临时禁用状态{@link SendStatus#TEMPORARILY_DISABLE}。
	 */
	long remainErrors(String mobile);

	/**
	 * 获取最后发送成功的Token
	 * @param mobile 手机号
	 * @return 最后发送成功的Token，如果Token不存在或者已经过期，则返回 {@code null}
	 */
	@Nullable
	default String getToken(String mobile) {
		return this.getToken(mobile, DEFAULT_GROUP);
	}

	/**
	 * 获取最后发送成功的Token
	 * @param mobile 手机号
	 * @param group 手机号分组
	 * @return 最后发送成功的Token，如果Token不存在或者已经过期，则返回 {@code null}
	 */
	@Nullable
	String getToken(String mobile, String group);

	/**
	 * 校验
	 * @param token 授权码
	 * @param mobile 手机号
	 * @param code 登录验证码
	 * @return 是否有效。
	 */
	boolean validate(String token, String mobile, String code);

}