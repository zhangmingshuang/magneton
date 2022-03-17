package org.magneton.module.sms;

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
	 * @param context 短信内容
	 * @return 是否发送成功
	 */
	default Consequences<SendStatus> trySend(String mobile, String context) {
		return this.trySend(mobile, DEFAULT_GROUP, context);
	}

	/**
	 * 尝试发送短信
	 * @param mobile 手机号
	 * @param group 手机号分组
	 * @param context 短信内容
	 * @return 是否发送成功
	 */
	Consequences<SendStatus> trySend(String mobile, String group, String context);

	/**
	 * 获取下次可发送短信间隔
	 * @param mobile 手机号
	 * @return 下次可以发送短信的时间间隔，单位秒。
	 */
	long ttl(String mobile);

}
