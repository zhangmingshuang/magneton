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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.GMC;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.annotation.Nullable;
import java.util.Date;

/**
 * 调度监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class SessionScheduleMonitor {

	/**
	 * 所属的sessionId
	 * @see SessionMonitor
	 */
	@Getter
	private final String sessionId;

	private final SessionMonitor sessionMonitor;

	/**
	 * 监控处理任务，一个Session有仅只有一个任务
	 */
	@Getter
	protected final SessionScheduleMonitorTask task;

	@Getter
	private final TriggerBuffer triggerBuffer;

	/**
	 * 最小检查次数，当触发器的检查次数小于该值时，不触发警告
	 */
	@Setter
	@Getter
	private int minCheckCount = 0;

	protected SessionScheduleMonitor(String sessionId, SessionMonitor sessionMonitor) {
		this(sessionId, sessionMonitor,
				new SessionScheduleMonitorTask(sessionId, GMC.nextId(sessionId, "task"), new SchedulePool(sessionId)));
	}

	protected SessionScheduleMonitor(String sessionId, SessionMonitor sessionMonitor,
			SessionScheduleMonitorTask sessionScheduleMonitorTask) {
		this.sessionId = sessionId;
		this.sessionMonitor = sessionMonitor;
		this.task = sessionScheduleMonitorTask;

		this.triggerBuffer = new TriggerBuffer(sessionId);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 * @return 触发器
	 */
	public ScheduleTrigger registerTrigger(Biz biz, String expression) {
		return this.registerTrigger(biz, expression, SessionScheduleMonitorTask.MIN_DELAY, null);
	}

	public ScheduleTrigger registerTrigger(String bizId, String expression) {
		return this.registerTrigger(Biz.of(bizId), expression, SessionScheduleMonitorTask.MIN_DELAY, null);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 * @param message 提示信息
	 * @return 触发器
	 */
	public ScheduleTrigger registerTrigger(String bizId, String expression, @Nullable String message) {
		return this.registerTrigger(Biz.of(bizId), expression, SessionScheduleMonitorTask.MIN_DELAY, message);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 * @param message 提示信息
	 * @return 触发器
	 */
	public ScheduleTrigger registerTrigger(Biz biz, String expression, @Nullable String message) {
		return this.registerTrigger(biz, expression, SessionScheduleMonitorTask.MIN_DELAY, message);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位秒
	 * @return 触发器
	 */
	public ScheduleTrigger registerTrigger(String bizId, String expression, long delay) {
		return this.registerTrigger(Biz.of(bizId), expression, delay, null);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位毫秒
	 * @return 触发器
	 */
	@Nullable
	public ScheduleTrigger registerTrigger(Biz biz, String expression, long delay) {
		return this.registerTrigger(biz, expression, delay, null);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位毫秒
	 * @param message 提示信息
	 * @return 触发器
	 */
	@Nullable
	public ScheduleTrigger registerTrigger(String bizId, String expression, long delay, @Nullable String message) {
		return this.registerTrigger(Biz.of(bizId), expression, delay, message);
	}

	/**
	 * 注册触发器
	 * @param biz 业务ID标识
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间
	 * @param message 提示信息
	 * @return 触发器
	 */
	@Nullable
	public ScheduleTrigger registerTrigger(Biz biz, String expression, long delay, @Nullable String message) {
		if (Strings.isNullOrEmpty(biz.getId())) {
			log.warn("[monitor] bizId is null. Skip register trigger.");
			return null;
		}
		if (Strings.isNullOrEmpty(expression)) {
			log.warn("[monitor] expression is null. Skip register trigger.");
			return null;
		}

		if (delay < SessionScheduleMonitorTask.MIN_DELAY) {
			delay = SessionScheduleMonitorTask.MIN_DELAY;
		}

		String triggerId = GMC.nextId(this.sessionId, "trigger");
		String dataId = GMC.nextId(triggerId, "data");
		SchedulePool.ScheduleData data = new SchedulePool.ScheduleData(dataId, biz);
		// 获取同步线程的监控上下文
		data.setSessionMonitorContext(this.sessionMonitor.getMonitorContext());

		CronSequenceGenerator sequenceGenerator = new CronSequenceGenerator(expression);
		// CronParser parser = new
		// CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
		// CronDescriptor descriptor = CronDescriptor.instance(Locale.CHINA);
		// describe很耗CPU，后续优化 ---- //
		// String intervalTime = descriptor.describe(parser.parse(expression));
		// data.setIntervalTime(intervalTime);

		data.setCronData(new CronData(expression, sequenceGenerator, delay, message));
		data.setNextTime(sequenceGenerator.next(new Date()).getTime());
		data.setCheckTime(data.getNextTime() + delay);

		return new ScheduleTrigger(this.sessionId, triggerId, this, data);
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.task.setPoolMaxSize(maxPoolSize);
	}

	public void shutdownNow() {
		if (this.task != null) {
			this.task.shutdown();
		}
		this.triggerBuffer.shutdown();
	}

}