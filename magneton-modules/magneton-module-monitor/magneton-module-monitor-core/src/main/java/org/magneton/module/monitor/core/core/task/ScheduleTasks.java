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

package org.magneton.module.monitor.core.core.task;

import cn.hutool.core.thread.NamedThreadFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.magneton.foundation.spi.SPILoader;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 调度任务执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see ScheduleTask
 */
@Slf4j
public class ScheduleTasks {

	/**
	 * 带名称的线程池工厂
	 */
	private static final NamedThreadFactory NAMED_THREAD_FACTORY = new NamedThreadFactory("monitor-schedule-timer",
			false);

	/**
	 * 调度服务
	 */
	private static final ScheduledExecutorService SERVICE = new ScheduledThreadPoolExecutor(1, NAMED_THREAD_FACTORY);

	/**
	 * 调度监控任务
	 */
	private static final List<ScheduleTask> SCHEDULE_TASKS;

	/**
	 * 初始化延迟
	 */
	public static final int INITIAL_DELAY = 100;

	/**
	 * 执行周期
	 */
	public static final int PERIOD = 1500;

	static {
		SCHEDULE_TASKS = SPILoader.loadInstance(ScheduleTask.class);
		init();
	}

	protected static void init() {
		SERVICE.scheduleAtFixedRate(new ScheduleRunner(), INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);
		Runtime.getRuntime().addShutdownHook(new Thread(SERVICE::shutdown));
	}

	/**
	 * 注册监控
	 * @param scheduleTask 监控任务
	 */
	public static void registerMonitor(ScheduleTask scheduleTask) {
		Preconditions.checkNotNull(scheduleTask, "scheduleTask is null");
		SCHEDULE_TASKS.add(scheduleTask);
	}

	/**
	 * 注销监控
	 * @param scheduleTask 监控任务
	 * @return 是否注销成功
	 */
	public static boolean unregisterMonitor(ScheduleTask scheduleTask) {
		Preconditions.checkNotNull(scheduleTask, "scheduleTask is null");
		return SCHEDULE_TASKS.remove(scheduleTask);
	}

	/**
	 * 节点筛选
	 * @param filter 筛选器
	 */
	@VisibleForTesting
	protected static void removeIf(Function<ScheduleTask, Boolean> filter) {
		Preconditions.checkNotNull(filter, "filter is null");
		SCHEDULE_TASKS.removeIf(filter::apply);
	}

	public static class ScheduleRunner implements Runnable {

		@Override
		public void run() {
			for (ScheduleTask scheduleTask : SCHEDULE_TASKS) {
				if (SERVICE.isShutdown()) {
					return;
				}
				try {
					scheduleTask.run();
				}
				catch (Exception e) {
					log.error(String.format("schedule task %s error.", scheduleTask), e);
				}
			}
		}

	}

}
