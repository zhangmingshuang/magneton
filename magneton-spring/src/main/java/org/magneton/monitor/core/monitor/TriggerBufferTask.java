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

import cn.nascent.tech.gaia.biz.monitor.core.task.ScheduleTasks;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Trigger Buffer Task
 * <p>
 * 触发器缓冲任务，用于解决在高并发的添加删除触发器时，对真实的发送处理队列造成性能影响
 *
 * @author zhangmsh
 * @since 4.0.0
 * @see TriggerBuffer
 */
@Slf4j
public class TriggerBufferTask {

	private static final ScheduledThreadPoolExecutor SERVICE = new ScheduledThreadPoolExecutor(1,
			ScheduleTasks.NAMED_THREAD_FACTORY);

	private static final Map<String, TriggerBuffer> TRIGGER_BUFFERS = new ConcurrentHashMap<>();

	static {
		SERVICE.scheduleAtFixedRate(new TaskRunner(), 100, 1000, TimeUnit.MILLISECONDS);
	}

	public static void add(TriggerBuffer triggerBuffer) {
		TRIGGER_BUFFERS.put(triggerBuffer.getSessionId(), triggerBuffer);
	}

	public static void remove(TriggerBuffer triggerBuffer) {
		TRIGGER_BUFFERS.remove(triggerBuffer.getSessionId());
	}

	public static boolean contains(TriggerBuffer triggerBuffer) {
		return TRIGGER_BUFFERS.containsKey(triggerBuffer.getSessionId());
	}

	public static void removeDataIfPresent(SchedulePool.ScheduleData data) {
		SessionScheduleMonitorTask task = data.getTrigger().getSessionScheduleMonitor().getTask();
		if (task != null) {
			task.removeIfPresent(data);
		}
	}

	private static class TaskRunner implements Runnable {

		@Override
		public void run() {
			try {
				List<Future<?>> futures = null;
				for (TriggerBuffer triggerBuffer : TRIGGER_BUFFERS.values()) {
					if (triggerBuffer.isShutdown()) {
						TRIGGER_BUFFERS.remove(triggerBuffer.getSessionId());
						continue;
					}

					Map<String, ScheduleTrigger> triggers = triggerBuffer.getTriggers();
					if (triggers == null || triggers.isEmpty()) {
						continue;
					}

					int size = triggers.size();
					if (size > 100) {
						futures = new ArrayList<>();
						ExecutorService pool = ScheduleTasks.createOrGetSlowTriggerPool();
						if (!pool.isShutdown()) {
							futures.add(pool.submit(() -> this.triggerJudge(triggers)));
						}
						else {
							this.triggerJudge(triggers);
						}
					}
					else {
						this.triggerJudge(triggers);
					}
				}

				if (futures != null) {
					for (Future<?> future : futures) {
						future.get();
					}
				}
			}
			catch (Throwable e) {
				log.warn("[monitor] trigger buffer task error.", e);
			}
		}

		private void triggerJudge(Map<String, ScheduleTrigger> triggers) {
			for (ScheduleTrigger scheduleTrigger : triggers.values()) {
				if (scheduleTrigger.isShutdown()) {
					continue;
				}

				SchedulePool.ScheduleData data = scheduleTrigger.getData();
				if (data == null || data.isRemoved()) {
					scheduleTrigger.shutdownNow();
					continue;
				}

				long checkTime = data.getCheckTime();
				if (checkTime <= System.currentTimeMillis() - 500) {
					SessionScheduleMonitorTask task = scheduleTrigger.getSessionScheduleMonitor().getTask();
					task.addIfAbsent(data);
					task.startIfNeed();
				}
			}
		}

	}

}
