package org.magneton.module.sms.monitor;

import org.magneton.foundation.spi.SPI;

/**
 * 短信监控.
 *
 * @author zhangmsh.
 * @since 2003.9
 */
@SPI
public interface SmsMonitor {

	/**
	 * 短信发送失败.
	 * @param mobile 手机号
	 * @param failureType 失败类型
	 */
	void onFailure(String mobile, FailureType failureType);

	/**
	 * 短信发送成功.
	 * @param mobile 手机号
	 */
	void onSuccess(String mobile);

}
