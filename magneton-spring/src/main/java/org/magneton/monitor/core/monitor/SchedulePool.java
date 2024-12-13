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

import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.task.ScheduleTask;
import org.magneton.monitor.core.task.ScheduleTasks;
import com.google.common.base.Strings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;

/**
 * 调度数据池
 * <p>
 * 用来存放相应的调度数据
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see ScheduleTask
 * @see ScheduleTasks
 */
@Slf4j
public class SchedulePool {

	private final PriorityBlockingQueue<ScheduleData> queue = new PriorityBlockingQueue<>(32, (o1, o2) -> {
		long t1 = Objects.isNull(o1) ? 0 : o1.getCheckTime();
		long t2 = Objects.isNull(o2) ? 0 : o2.getCheckTime();
		return Long.compare(t1, t2); // 修正溢出问题
	});

	/**
	 * 映射列表，用来快速的检索相应的调度数据
	 */
	private final Map<String, ScheduleData> ref = new ConcurrentHashMap<>((int) (1.3 * 1024));

	@Setter
	private int maxSize = 2048;

	/**
	 * 所属sessionId
	 */
	@Getter
	private final String sessionId;

	public SchedulePool(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 添加，如果存在则忽略
	 * @param data 数据
	 * @return 如果存在则返回存在的数据，否则返回null
	 */
	public ScheduleData addIfAbsent(ScheduleData data) {
		if (data == null) {
			log.warn("[monitor] data is null. Skip add.");
			return null;
		}
		if (this.getQueue().size() >= this.maxSize) {
			log.error("out of size. max size:{}", this.maxSize);
			return null;
		}
		if (!data.validate()) {
			log.warn("[monitor] data : [{}] is invalid. Skip add.", data);
			return null;
		}
		ScheduleData oldData = this.ref.computeIfAbsent(data.getDataId(), dataId -> {
			data.setOnTask(true);
			this.getQueue().add(data);
			return data;
		});
		return oldData == data ? null : oldData;
	}

	/**
	 * 从列队移除
	 * @param data 数据
	 */
	public void remove(ScheduleData data) {
		if (data == null) {
			log.warn("[monitor] data is null. Skip remove.");
			return;
		}
		this.ref.computeIfPresent(data.getDataId(), (dataId, oldData) -> {
			oldData.setRemoved(true); // 标记为删除的
			// noinspection ResultOfMethodCallIgnored
			this.getQueue().remove(oldData);
			return null;
		});
	}

	/**
	 * 更新检查时间
	 * @param data 数据
	 * @param checkTime 检查时间
	 */
	public void updateCheckTime(ScheduleData data, long checkTime) {
		if (data == null) {
			log.warn("[monitor] data is null. Skip updateCheckTime.");
			return;
		}
		this.ref.computeIfPresent(data.getDataId(), (dataId, checkData) -> {
			// noinspection ResultOfMethodCallIgnored
			this.getQueue().remove(checkData);
			checkData.checkTime = checkTime;
			checkData.checkCount += 1;
			this.getQueue().add(checkData);
			return checkData;
		});
	}

	/**
	 * 更新下次执行时间
	 * @param data 数据
	 * @param nextTime 下次执行时间
	 */
	public void updateNextTime(ScheduleData data, long nextTime) {
		if (data == null) {
			log.warn("[monitor] data is null. Skip updateNextTime.");
			return;
		}
		this.ref.computeIfPresent(data.getDataId(), (dataId, updateData) -> {
			// noinspection ResultOfMethodCallIgnored
			this.getQueue().remove(updateData);
			updateData.nextTime = nextTime;
			updateData.checkTime = nextTime;
			this.getQueue().add(updateData);
			return updateData;
		});
	}

	/**
	 * 获取队列头部数据
	 * @return 队列头部数据，如果队列为空，则返回null
	 */
	@Nullable
	public ScheduleData peek() {
		while (!this.getQueue().isEmpty()) {
			ScheduleData scheduleData = this.getQueue().peek();
			if (scheduleData != null && scheduleData.isRemoved()) {
				this.getQueue().poll();
				this.ref.remove(scheduleData.getDataId());
			}
			else {
				return scheduleData;
			}
		}
		return null;
	}

	/**
	 * 获取队列头部数据，并从队列中移除
	 * @return 队列头部数据，如果队列为空，则返回null
	 */
	@Nullable
	public ScheduleData poll() {
		ScheduleData scheduleData = this.getQueue().poll();
		if (scheduleData != null) {
			this.ref.remove(scheduleData.getDataId());
			while (scheduleData.isRemoved()) {
				scheduleData = this.getQueue().poll();
				if (scheduleData == null) {
					break;
				}
				this.ref.remove(scheduleData.getDataId());
			}
		}
		return scheduleData;
	}

	/**
	 * 获取队列
	 * @return 队列
	 */
	public PriorityBlockingQueue<ScheduleData> getQueue() {
		return this.queue;
	}

	/**
	 * 获取队列大小
	 * @return 队列大小
	 */
	public int size() {
		return this.ref.size();
	}

	/**
	 * 获取调度数据
	 * @param dataId id
	 * @return 调度数据
	 */
	@Nullable
	public ScheduleData get(String dataId) {
		if (Strings.isNullOrEmpty(dataId)) {
			log.warn("[monitor] id is null. Skip get.");
			return null;
		}
		return this.ref.get(dataId);
	}

	public void clean() {
		this.ref.clear();
		this.queue.clear();
	}

	@Getter
	@ToString
	public static class ScheduleData {

		private final String dataId;

		private ScheduleTrigger trigger;

		@Getter
		private String triggerId;

		private final Biz biz;

		@Setter
		private CronData cronData;

		/**
		 * 下次执行时间
		 */
		private long nextTime;

		/**
		 * 间隔描述
		 */
		// @Setter
		// private String intervalTime;

		/**
		 * 检查时间
		 */
		@Setter
		private long checkTime;

		/**
		 * 检查次数
		 */
		@Setter
		private int checkCount = 1;

		/**
		 * Session级别的监控上下文
		 */
		@Setter
		private MonitorContext sessionMonitorContext;

		// before send.
		@Setter
		private Consumer<MonitorContext> asyncTriggerMonitorContextConsumer;

		@Setter(AccessLevel.PROTECTED)
		private volatile boolean removed;

		/**
		 * 是否在队列中，如果不是，则在缓冲区中
		 */
		@Setter(AccessLevel.PROTECTED)
		private volatile boolean onTask;

		public ScheduleData(String dataId, Biz biz) {
			this.dataId = dataId;
			this.biz = biz;
		}

		public void setNextTime(long nextTime) {
			this.nextTime = nextTime;
			if (this.checkTime <= 0) {
				this.checkTime = nextTime;
			}
		}

		public void setTrigger(ScheduleTrigger trigger) {
			this.trigger = trigger;
			this.triggerId = trigger.getTriggerId();
		}

		public boolean validate() {
			return this.nextTime > 0 && this.checkTime > 0;
		}

	}

}