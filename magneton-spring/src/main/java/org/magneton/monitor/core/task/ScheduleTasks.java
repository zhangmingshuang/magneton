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

package org.magneton.monitor.core.task;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.nascent.tech.gaia.biz.monitor.core.monitor.MonitorHeartbeatMonitorTask;
import cn.nascent.tech.gaia.foundation.spi.SPILoader;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 调度任务执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see ScheduleTask
 */
@Slf4j
public class ScheduleTasks {

	private static final TransmittableThreadLocal<TTLContext> TTL = new TransmittableThreadLocal<>();

	/**
	 * 带名称的线程池工厂
	 */
	public static final NamedThreadFactory NAMED_THREAD_FACTORY = new NamedThreadFactory("monitor-schedule-timer",
			false) {
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = super.newThread(r);
			thread.setPriority(10);
			return thread;
		}
	};

	private static List<ScheduleTaskProcessor> POST_PROCESSORS = new ArrayList<>();

	/**
	 * 调度服务
	 *
	 */
	@Nonnull
	private static final ScheduledExecutorService SERVICE;

	/**
	 * 优先级调度服务
	 */
	@Nonnull
	private static final ScheduledExecutorService PRIORITY_SERVICE;

	/**
	 * 调度监控任务
	 */
	private static final Map<String, ScheduleTask> SCHEDULE_TASKS = new ConcurrentHashMap<>(32);

	private static final ScheduleTask HEARTBEAT_TASK = new MonitorHeartbeatMonitorTask();

	private static final Map<ScheduleTask, AtomicInteger> TIME_USED_REF = new ConcurrentHashMap<>(32);

	/**
	 * 初始化延迟
	 */
	public static final int INITIAL_DELAY = 100;

	/**
	 * 执行周期
	 */
	public static final int PERIOD = 1500;

	// ms > min
	private static volatile long MIN_TIME = System.currentTimeMillis() / 60000;

	@Nonnull
	private static final ExecutorService FAST_TRIGGER_POOL;

	private static ExecutorService SLOW_TRIGGER_POOL = null;

	static {
		List<ScheduleTask> scheduleTasks = SPILoader.loadInstance(ScheduleTask.class);
		for (ScheduleTask scheduleTask : scheduleTasks) {
			SCHEDULE_TASKS.put(scheduleTask.taskId(), scheduleTask);
		}
		ScheduledThreadPoolExecutor stpeService = new ScheduledThreadPoolExecutor(1, NAMED_THREAD_FACTORY) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				TTLContext.remove();
				super.afterExecute(r, t);
			}
		};
		SERVICE = TtlExecutors.getTtlScheduledExecutorService(stpeService);
		SERVICE.scheduleAtFixedRate(new ScheduleRunner(SCHEDULE_TASKS), INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);

		ScheduledThreadPoolExecutor stpePriorityService = new ScheduledThreadPoolExecutor(1, NAMED_THREAD_FACTORY) {
			@Override
			protected void afterExecute(Runnable r, Throwable t) {
				TTLContext.remove();
				super.afterExecute(r, t);
			}
		};
		PRIORITY_SERVICE = TtlExecutors.getTtlScheduledExecutorService(stpePriorityService);

		PRIORITY_SERVICE.scheduleAtFixedRate(new ScheduleRunner(Collections.singletonMap("heartbeat", HEARTBEAT_TASK)),
				INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);

		ThreadPoolExecutor fastPool = new ThreadPoolExecutor(4, 4, 60L, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(1000), r -> {
					Thread thread = new Thread(r, "monitor-schedule-fast-" + r.hashCode());
					thread.setPriority(7);
					return thread;
				});
		FAST_TRIGGER_POOL = TtlExecutors.getTtlExecutorService(fastPool);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.warn("[monitor] shutdown monitor on shutdown hook.");
				SERVICE.shutdownNow();
				PRIORITY_SERVICE.shutdownNow();

				FAST_TRIGGER_POOL.shutdownNow();
				if (SLOW_TRIGGER_POOL != null) {
					SLOW_TRIGGER_POOL.shutdownNow();
				}
			}
		});
	}

	public static ExecutorService createOrGetSlowTriggerPool() {
		if (SLOW_TRIGGER_POOL == null) {
			synchronized (ScheduleTasks.class) {
				if (SLOW_TRIGGER_POOL == null) {
					ThreadPoolExecutor slowPool = new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS,
							new LinkedBlockingQueue<Runnable>(2000), r -> {
								int i = r.hashCode();
								Thread thread = new Thread(r, "monitor-schedule-slow-" + i);
								thread.setPriority(7);
								return thread;
							});
					SLOW_TRIGGER_POOL = TtlExecutors.getTtlExecutorService(slowPool);
				}
			}
		}
		return SLOW_TRIGGER_POOL;
	}

	public static void addPostProcessor(ScheduleTaskProcessor postProcessor) {
		POST_PROCESSORS.add(postProcessor);
	}

	/**
	 * 注册监控
	 * @param scheduleTask 监控任务
	 * @return 是否注册成功, 可能是重复注册
	 */
	public static boolean registerMonitor(ScheduleTask scheduleTask) {
		if (scheduleTask == null) {
			return false;
		}
		String id = scheduleTask.taskId();
		ScheduleTask oldTask = SCHEDULE_TASKS.computeIfAbsent(id, k -> scheduleTask);
		return oldTask == scheduleTask;
	}

	/**
	 * 注销监控
	 * @param scheduleTask 监控任务
	 */
	public static void unregisterMonitor(ScheduleTask scheduleTask) {
		if (scheduleTask == null) {
			return;
		}
		SCHEDULE_TASKS.computeIfPresent(scheduleTask.taskId(), (k, v) -> {
			v.shutdown();
			return null;
		});
	}

	public static class ScheduleRunner implements Runnable {

		private final Map<String, ScheduleTask> scheduleTasks;

		public ScheduleRunner(Map<String, ScheduleTask> scheduleTasks) {
			this.scheduleTasks = scheduleTasks;
		}

		@Override
		public void run() {
			try {
				List<Future<?>> futures = new ArrayList<>(this.scheduleTasks.size());
				for (ScheduleTask scheduleTask : this.scheduleTasks.values()) {
					if (SERVICE.isShutdown() || PRIORITY_SERVICE.isShutdown() || scheduleTask.isShutdown()) {
						return;
					}
					ExecutorService triggerPool = FAST_TRIGGER_POOL;
					AtomicInteger jobTimeoutCount = TIME_USED_REF.get(scheduleTask);
					// job-timeout 10 times in 1 min
					if (triggerPool.isShutdown() || (jobTimeoutCount != null && jobTimeoutCount.get() > 10)) {
						triggerPool = createOrGetSlowTriggerPool();
						if (triggerPool.isShutdown()) {
							log.warn("triggerPool is shutdown.");
							continue;
						}
					}

					Future<?> submit = triggerPool.submit(() -> {
						long start = System.currentTimeMillis();
						try {
							if (!POST_PROCESSORS.isEmpty()) {
								for (ScheduleTaskProcessor postProcessor : POST_PROCESSORS) {
									if (!postProcessor.supports(scheduleTask)) {
										continue;
									}
									boolean next = postProcessor.preRunProcess(scheduleTask);
									if (!next) {
										return;
									}
								}
							}
							scheduleTask.run();
						}
						catch (Exception e) {
							log.error("schedule task {} error.", scheduleTask, e);
						}
						finally {
							// check timeout-count-map
							long minTim_now = System.currentTimeMillis() / 60000;
							if (MIN_TIME != minTim_now) {
								MIN_TIME = minTim_now;
								TIME_USED_REF.clear();
							}

							// incr timeout-count-map
							long cost = System.currentTimeMillis() - start;
							if (cost > 30) { // ob-timeout threshold
								AtomicInteger timeoutCount = TIME_USED_REF.putIfAbsent(scheduleTask,
										new AtomicInteger(1));
								if (timeoutCount != null) {
									timeoutCount.incrementAndGet();
								}
							}
						}
					});
					futures.add(submit);
				}
				for (Future<?> future : futures) {
					future.get();
				}
			}
			catch (Throwable e) {
				log.warn("schedule task error.", e);
			}

		}

	}

	@Setter
	@Getter
	@ToString
	public static class TTLContext {

		private boolean transientBreak;

		public static TTLContext getThreadTtlContext() {
			TTLContext ttlContext = TTL.get();
			if (ttlContext == null) {
				ttlContext = new TTLContext();
				TTL.set(ttlContext);
			}
			return ttlContext;
		}

		public static TTLContext currentThreadTtlContext() {
			return TTL.get();
		}

		public static void remove() {
			TTL.remove();
		}

	}

}