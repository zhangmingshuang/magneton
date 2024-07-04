package org.magneton.enhance.sms.monitor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.magneton.foundation.spi.SPILoader;

import java.util.List;

/**
 * 短信监控
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class SmsMonitors {

	/**
	 * 短信监控列表
	 */
	private static final List<SmsMonitor> SMS_MONITORS;

	private static final Notifier NOTIFIER = new Notifier();

	static {
		List<SmsMonitor> smsMonitors = SPILoader.loadInstance(SmsMonitor.class);
		SMS_MONITORS = Lists.newArrayList(smsMonitors);
	}

	public static Notifier notifier() {
		return NOTIFIER;
	}

	/**
	 * 短信监控器注册
	 * @param smsMonitor 短信监控器
	 */
	public static boolean register(SmsMonitor smsMonitor) {
		Preconditions.checkNotNull(smsMonitor, "smsMonitor must not be null");

		return SMS_MONITORS.add(smsMonitor);
	}

	/**
	 * 短信监控器注销
	 * @param smsMonitor 短信监控器
	 */
	public static boolean unregister(SmsMonitor smsMonitor) {
		Preconditions.checkNotNull(smsMonitor, "smsMonitor must not be null");

		return SMS_MONITORS.remove(smsMonitor);
	}

	public static class Notifier {

		public void onFailure(String mobile, FailureType failureType) {
			SMS_MONITORS.forEach(smsMonitor -> smsMonitor.onFailure(mobile, failureType));
		}

		public void onSuccess(String mobile) {
			SMS_MONITORS.forEach(smsMonitor -> smsMonitor.onSuccess(mobile));
		}

	}

}
