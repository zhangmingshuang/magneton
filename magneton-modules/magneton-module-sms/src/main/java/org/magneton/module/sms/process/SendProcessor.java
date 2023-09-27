package org.magneton.module.sms.process;

import org.magneton.core.Result;
import org.magneton.module.sms.entity.SmsToken;

/**
 * 短信发送处理器.
 *
 * @author zhangmsh 16/03/2022
 * @since 2.0.7
 * @see org.magneton.module.sms.process.aliyun.AbstractAliyunSmsProcessor
 */
public interface SendProcessor {

	/**
	 * 发送短信
	 * @param mobile 手机号
	 * @return 如果发送成功，则返回此次的Token。
	 */
	Result<SmsToken> send(String mobile);

}
