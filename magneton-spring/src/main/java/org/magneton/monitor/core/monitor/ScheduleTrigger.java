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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.monitor.core.task.ScheduleTasks;
import org.magneton.monitor.support.Switcher;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Schedule Trigger
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@Setter(AccessLevel.PROTECTED)
@Getter
@ToString
public class ScheduleTrigger implements IMonitor {

	/**
	 * 所属sessionId
	 * @see SessionMonitor
	 */
	private final String sessionId;

	/**
	 * TriggerId
	 */
	private final String triggerId;

	private final Switcher switcher = new Switcher();

	private final SchedulePool.ScheduleData data;

	private final SessionScheduleMonitor sessionScheduleMonitor;

	private final AtomicLong appointCount = new AtomicLong();

	/**
	 * 异步的监控上下文消费者，需要注意使用时的线程不一致问题
	 */
	private Consumer<MonitorContext> asyncMonitorContextConsumer;

	/**
	 * 最小检查次数，当触发器的检查次数小于该值时，不触发警告
	 */
	@Setter
	@Getter
	private int minCheckCount = 1;

	public ScheduleTrigger(String sessionId, String triggerId, SessionScheduleMonitor sessionScheduleMonitor,
			SchedulePool.ScheduleData data) {
		this.sessionId = sessionId;
		this.triggerId = triggerId;

		this.data = data;
		this.data.setTrigger(this);
		this.sessionScheduleMonitor = sessionScheduleMonitor;
	}

	public void transientBreak() {
		ScheduleTasks.TTLContext.getThreadTtlContext().setTransientBreak(true);
	}

	public long appoint() {
		return this.appointCount.incrementAndGet();
	}

	public long disappoint() {
		long point = this.appointCount.decrementAndGet();
		if (point < 0) {
			return this.appointCount.addAndGet(-point);
		}
		return point;
	}

	public boolean isAppoint() {
		return this.appointCount.get() > 0;
	}

	@Override
	public void attachContext(MonitorContext monitorContext) {
		if (monitorContext == null) {
			return;
		}
		MonitorContext sessionMonitorContext = this.data.getSessionMonitorContext();
		if (sessionMonitorContext == null) {
			this.data.setSessionMonitorContext(monitorContext);
		}
		else {
			sessionMonitorContext.putAll(monitorContext.getContext());
		}
	}

	@Override
	public void attachContext(Supplier<MonitorContext> supplier) {
		if (supplier != null) {
			MonitorContext monitorContext = supplier.get();
			if (monitorContext != null) {
				this.attachContext(monitorContext);
			}
		}
	}

	public void asyncContextProcess(Consumer<MonitorContext> consumer) {
		this.asyncMonitorContextConsumer = consumer;
		this.data.setAsyncTriggerMonitorContextConsumer(context -> {
			if (this.asyncMonitorContextConsumer != null) {
				this.asyncMonitorContextConsumer.accept(context);
			}
		});
	}

	public boolean start() {
		return this.switcher.start(() -> {
			// 在Start的时候，将该Trigger注册到Buffer中
			// 等待Buffer的TaskRunner来处理
			this.sessionScheduleMonitor.getTriggerBuffer().add(this);
		});
	}

	public void shutdownNow() {
		this.getData().setRemoved(true);
		this.switcher.shutdownNow(() -> {
			// 清除资源
			this.sessionScheduleMonitor.getTriggerBuffer().remove(this);
			this.appointCount.set(0);
		});
	}

	public boolean isShutdown() {
		return this.switcher.isShutdown();
	}

	public boolean touch() {
		if (!this.switcher.isStarted() || this.data.isRemoved()) {
			return false;
		}
		CronData cronData = this.data.getCronData();
		CronSequenceGenerator sequenceGenerator = cronData.getSequenceGenerator();
		long nextTime = sequenceGenerator.next(new Date()).getTime();
		long delay = Math.max(SessionScheduleMonitorTask.MIN_DELAY, cronData.getDelay());
		this.data.setNextTime(nextTime + delay);
		this.data.setCheckTime(this.data.getNextTime());

		if (this.data.isOnTask() && !this.data.isRemoved()) {
			SessionScheduleMonitorTask task = this.sessionScheduleMonitor.getTask();
			task.touch(this.data);
		}
		return true;
	}

}
