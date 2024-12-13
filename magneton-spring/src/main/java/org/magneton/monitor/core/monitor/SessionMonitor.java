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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.GMC;
import org.magneton.monitor.core.module.ModuleType;
import org.magneton.monitor.support.TimeWatch;

import javax.annotation.Nullable;

/**
 * 会话级监控
 *
 * @author zhangmsh
 * @since 4.0.0
 */
@Slf4j
public class SessionMonitor extends AbstractMonitor {

	/**
	 * SessionId
	 */
	@Getter
	private final String sessionId;

	private SessionStatMonitor sessionStatMonitor;

	/**
	 * 调度器，一个Session有仅只有一个调度器
	 */
	private volatile SessionScheduleMonitor sessionScheduleMonitor;

	public SessionMonitor() {
		super(Biz.of(GMC.nextId("session")));
		this.sessionId = this.biz.toBizId();
	}

	public void exception(String bizId, String message) {
		this.exception(Biz.of(bizId), message, null);
	}

	public void exception(Biz biz, String message) {
		this.exception(biz, message, null);
	}

	public void exception(String bizId, String message, Throwable cause) {
		this.exception(Biz.of(bizId), message, cause);
	}

	/**
	 * 异常
	 * @param biz 业务类型
	 * @param message 提示信息
	 * @param cause 异常
	 */
	public void exception(Biz biz, String message, @Nullable Throwable cause) {
		if (Strings.isNullOrEmpty(message)) {
			log.warn("[monitor] message is null. Skip exception.");
			return;
		}
		if (biz == null) {
			log.warn("[monitor] biz is null. Ignore exception.");
			return;
		}
		try {
			ExceptionMonitor.getInstance().exception(biz, this.getMonitorContext(), message, cause);
		}
		finally {
			this.afterSend();
		}
	}

	public void timeUsed(String bizId, long timeUsed) {
		this.timeUsed(Biz.of(bizId), timeUsed, null);
	}

	public void timeUsed(Biz biz, TimeWatch timeWatch) {
		long currentTotalTime = timeWatch.getCurrentTotalTime();
		this.timeUsed(biz, currentTotalTime, timeWatch.prettyPrint());
	}

	public void timeUsed(String bizId, long timeUsed, String message) {
		this.timeUsed(Biz.of(bizId), timeUsed, message);
	}

	/**
	 * 耗时
	 * @param biz 业务标识
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void timeUsed(Biz biz, long timeUsed, @Nullable String message) {
		if (biz == null) {
			log.warn("[monitor] biz is null. Ignore timeUsed.");
			return;
		}
		try {
			TimeUsedMonitor.getInstance().timeUsed(biz, this.getMonitorContext(), timeUsed, message);
		}
		finally {
			this.afterSend();
		}
	}

	public void alarm(String bizId, String message) {
		this.alarm(Biz.of(bizId), message);
	}

	/**
	 * 告警
	 * @param biz 业务标识
	 * @param message 提示信息
	 */
	public void alarm(Biz biz, String message) {
		if (biz == null) {
			log.warn("[monitor] biz is null. Ignore alarm.");
			return;
		}
		try {
			CommonMonitor.getInstance().common(ModuleType.ALARM, biz, this.getMonitorContext(), message);
		}
		finally {
			this.afterSend();
		}
	}

	/**
	 * 统计类监控
	 * @return 统计类监控
	 */
	public SessionStatMonitor stat() {
		if (this.sessionStatMonitor == null) {
			synchronized (SessionStatMonitor.class) {
				if (this.sessionStatMonitor == null) {
					this.sessionStatMonitor = new SessionStatMonitor(this);
				}
			}
		}
		return this.sessionStatMonitor;
	}

	/**
	 * 调度类监控
	 * @return 调度类监控
	 */
	public SessionScheduleMonitor schedule() {
		if (this.sessionScheduleMonitor == null) {
			synchronized (SessionScheduleMonitor.class) {
				if (this.sessionScheduleMonitor == null) {
					this.sessionScheduleMonitor = new SessionScheduleMonitor(this.sessionId, this);
				}
			}
		}
		return this.sessionScheduleMonitor;
	}

	protected void afterSend() {
		// pass
	}

	@Override
	public void shutdown() {
		if (this.sessionScheduleMonitor != null) {
			this.sessionScheduleMonitor.shutdownNow();
			this.sessionScheduleMonitor = null;
		}
		if (this.sessionStatMonitor != null) {
			this.sessionStatMonitor = null;
		}
		super.shutdown();
	}

}
