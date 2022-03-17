package org.magneton.module.sms.process;

import org.magneton.core.Consequences;
import org.magneton.module.sms.SendStatus;

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
	 * @param context 短信内容
	 * @return 发送状态
	 */
	Consequences<SendStatus> send(String mobile, String context);

}
