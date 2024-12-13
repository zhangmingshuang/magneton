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

import org.magneton.monitor.core.MonitorSenders;
import org.magneton.monitor.core.module.CronModule;
import org.magneton.monitor.core.module.ModuleType;
import org.magneton.monitor.core.task.ScheduleTask;
import org.magneton.monitor.core.task.ScheduleTasks;
import org.magneton.monitor.support.Switcher;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.ThreadSafe;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 监控处理任务，一个Session有仅只有一个任务
 *
 * @author zhangmsh
 * @since 4.0.0
 * @see SessionScheduleMonitor
 */
@Slf4j
public class SessionScheduleMonitorTask implements ScheduleTask {

	/**
	 * 默认延迟
	 */
	public static final int MIN_DELAY = 5000;

	/**
	 * 所属sessionId
	 * @see SessionMonitor
	 */
	@Getter
	private final String sessionId;

	private final String taskId;

	private final Switcher switcher = new Switcher();

	@ThreadSafe
	private final SchedulePool schedulePool;

	public SessionScheduleMonitorTask(String sessionId, String taskId, SchedulePool schedulePool) {
		this.sessionId = sessionId;
		this.taskId = taskId;
		this.schedulePool = schedulePool;
	}

	@Override
	public String taskId() {
		return this.taskId;
	}

	@Override
	public void run() {
		if (this.isShutdown()) {
			return;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while (true) {
				SchedulePool.ScheduleData scheduleData = this.schedulePool.peek();
				if (scheduleData == null) {
					return;
				}
				CronData cronData = scheduleData.getCronData();
				if (Objects.isNull(cronData)) {
					log.warn("scheduleDataId:{} cronData is null. data:{}", scheduleData.getBiz(), scheduleData);
					return;
				}
				long delay = Math.max(MIN_DELAY, cronData.getDelay());
				long checkTime = scheduleData.getCheckTime();
				if (checkTime >= this.current()) {
					return;
				}

				long shouldTime = scheduleData.getNextTime();
				// 需要预警
				CronModule cronModule = new CronModule(scheduleData.getBiz());
				MonitorContext monitorContext = scheduleData.getSessionMonitorContext();
				if (monitorContext == null) {
					monitorContext = new MonitorContext();
				}
				monitorContext.put("triggerId", scheduleData.getTriggerId());
				cronModule.setMonitorContext(monitorContext);

				Consumer<MonitorContext> asyncTMCConsumer = scheduleData.getAsyncTriggerMonitorContextConsumer();
				if (asyncTMCConsumer != null) {
					asyncTMCConsumer.accept(monitorContext);
				}

				String message = cronData.getMessage();
				if (Strings.isNullOrEmpty(message)) {
					message = "-";
				}
				cronModule.setMessage(message);
				cronModule.setModuleType(ModuleType.SCHEDULE);

				cronModule.setCron(cronData.getExpression());
				cronModule.setShouldTime(sdf.format(new Date(shouldTime)));
				cronModule.setTotalInterval(System.currentTimeMillis() - shouldTime);
				// cronModule.setInterval(scheduleData.getIntervalTime());
				cronModule.setIntervalCount(scheduleData.getCheckCount());

				ScheduleTasks.TTLContext ttlContext = ScheduleTasks.TTLContext.currentThreadTtlContext();
				boolean isTransientBreak = ttlContext != null && ttlContext.isTransientBreak();
				if (isTransientBreak || !scheduleData.getTrigger().isAppoint()) {
					ScheduleTrigger trigger = scheduleData.getTrigger();
					int minCheckCount = trigger.getMinCheckCount();
					if (minCheckCount < 1) {
						minCheckCount = trigger.getSessionScheduleMonitor().getMinCheckCount();
					}
					if (scheduleData.getCheckCount() > minCheckCount) {
						MonitorSenders.getInstance().send(cronModule);
					}
				}

				this.schedulePool.updateCheckTime(scheduleData, this.current() + delay);
			}
		}
		catch (Throwable e) {
			log.error("[monitor] CronMonitorScheduleTask error", e);
		}
	}

	protected long current() {
		return System.currentTimeMillis();
	}

	public void addIfAbsent(SchedulePool.ScheduleData data) {
		this.schedulePool.addIfAbsent(data);
	}

	/**
	 * 触发
	 * <p>
	 * 注册的触发器对应的触发节点未被触发时，会及时上报预警信息，该方法用来告知触发器当前的任务已经被触发。
	 * <p>
	 * 对应的触发器被触发之后，会重新计算下一次的触发时间。
	 * @param data 调度数据
	 */
	protected void touch(SchedulePool.ScheduleData data) {
		if (data == null) {
			log.warn("[monitor] data is null. Skip touch.");
			return;
		}
		if (this.isShutdown()) {
			return;
		}
		SchedulePool.ScheduleData scheduleData = this.schedulePool.get(data.getDataId());
		if (scheduleData == null) {
			return;
		}
		this.schedulePool.updateNextTime(scheduleData, scheduleData.getNextTime());
	}

	public void startIfNeed() {
		if (this.switcher.start()) {
			ScheduleTasks.registerMonitor(this);
		}
	}

	@Override
	public void shutdown() {
		this.switcher.shutdownNow(() -> {
			this.schedulePool.clean();
			ScheduleTasks.unregisterMonitor(this);
		});
	}

	@Override
	public boolean isShutdown() {
		return this.switcher.isShutdown();
	}

	public void setPoolMaxSize(int maxPoolSize) {
		this.schedulePool.setMaxSize(maxPoolSize);
	}

	public void removeIfPresent(SchedulePool.ScheduleData data) {
		if (data == null) {
			return;
		}
		this.schedulePool.remove(data);
	}

}