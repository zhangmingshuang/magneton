/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.monitor.core.monitor;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.module.ModuleType;
import org.magneton.monitor.support.TimeWatch;

import javax.annotation.Nullable;

/**
 * 一次性监控
 * <p>
 * 一次性监控在一次调用后，会清除所有相关上下文。
 *
 * @author zhangmsh
 * @since 4.0.0
 */
@Slf4j
public class OnceMonitor extends AbstractMonitor {

	public OnceMonitor(Biz biz) {
		super(biz);
	}

	/**
	 * 异常
	 * @param message 提示信息
	 */
	public void exception(String message) {
		this.exception(message, null);
	}

	/**
	 * 异常
	 * @param message 提示信息
	 * @param cause 异常
	 */
	public void exception(String message, @Nullable Throwable cause) {
		try {
			if (Strings.isNullOrEmpty(message)) {
				log.warn("[monitor] message is null. Skip exception stat.");
				return;
			}
			ExceptionMonitor.getInstance().exception(this.biz, this.getMonitorContext(), message, cause);
		}
		finally {
			this.afterSend();
		}
	}

	/**
	 * 耗时
	 * @param timeUsed 耗时，毫秒
	 */
	public void timeUsed(long timeUsed) {
		this.timeUsed(timeUsed, null);
	}

	public void timeUsed(TimeWatch timeWatch) {
		long currentTotalTime = timeWatch.getCurrentTotalTime();
		this.timeUsed(currentTotalTime, timeWatch.prettyPrint());
	}

	/**
	 * 耗时
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void timeUsed(long timeUsed, @Nullable String message) {
		try {
			TimeUsedMonitor.getInstance().timeUsed(this.biz, this.getMonitorContext(), timeUsed, message);
		}
		finally {
			this.afterSend();
		}
	}

	/**
	 * 告警
	 * @param message 提示信息
	 */
	public void alarm(String message) {
		try {
			CommonMonitor.getInstance().common(ModuleType.ALARM, this.biz, this.getMonitorContext(), message);
		}
		finally {
			this.afterSend();
		}
	}

	/**
	 * 统计类监控
	 * @return 统计类监控
	 */
	public OnceStatMonitor stat() {
		return new OnceStatMonitor(this);
	}

	private void afterSend() {
		this.shutdown();
	}

}
