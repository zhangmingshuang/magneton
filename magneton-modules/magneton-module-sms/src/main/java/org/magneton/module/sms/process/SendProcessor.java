package org.magneton.module.sms.process;

import org.magneton.core.Consequences;
import org.magneton.module.sms.entity.SmsToken;

/**
 * .
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 */
public interface SendProcessor {

	/**
	 * 发送短信
	 * @param mobile 手机号
	 * @return 如果发送成功，则返回此次的Token。
	 */
	Consequences<SmsToken> send(String mobile);

}
