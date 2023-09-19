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

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.magneton.module.monitor.core.core.exception.DataException;
import org.magneton.module.monitor.core.core.exception.OutOfPoolSizeException;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

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

	/**
	 * 最大的调度数据数量,默认100000
	 */
	public static final int MAX_SIZE = Integer
			.parseInt(MoreObjects.firstNonNull(System.getProperty("monitor.schedule.pool.size"), "100000"));

	/**
	 * 优先级队列，按照检查时间从小到大排序
	 */
	private final PriorityQueue<ScheduleData> queue = new PriorityQueue<>(32, (o1, o2) -> {
		long t1 = Objects.isNull(o1) ? 0 : o1.getCheckTime();
		long t2 = Objects.isNull(o2) ? 0 : o2.getCheckTime();
		return (int) (t1 - t2);
	});

	/**
	 * 映射列表，用来快速的检索相应的调度数据
	 */
	private final Map<String, ScheduleData> ref = Maps.newConcurrentMap();

	/**
	 * 添加
	 * @param id id
	 * @param data 数据
	 * @return 如果存在，则返回原来的数据，否则返回null
	 */
	@Nullable
	@CanIgnoreReturnValue
	public ScheduleData add(String id, ScheduleData data) {
		Preconditions.checkNotNull(data, "data is null");
		Preconditions.checkNotNull(id, "id is null");

		if (this.getQueue().size() >= MAX_SIZE) {
			throw new OutOfPoolSizeException(String.format("out of size. max size:%s", MAX_SIZE));
		}

		data.id = id;

		if (!data.validate()) {
			throw new DataException("data is invalid");
		}

		ScheduleData old = this.ref.get(id);
		if (old == null) {
			this.getQueue().add(data);
		}
		else {
			this.getQueue().remove(old);
			this.getQueue().add(data);
		}
		return this.ref.put(id, data);
	}

	/**
	 * 如果不存在，则添加
	 * @param id id
	 * @param data 数据
	 */
	public void addIfAbsent(String id, ScheduleData data) {
		Preconditions.checkNotNull(data, "data is null");
		Preconditions.checkNotNull(id, "id is null");

		this.ref.computeIfAbsent(id, k -> {
			this.getQueue().add(data);
			return data;
		});
	}

	/**
	 * 从列队移除
	 * @param id id
	 */
	public void remove(String id) {
		Preconditions.checkNotNull(id, "id is null");

		this.ref.computeIfPresent(id, (k, data) -> {
			this.getQueue().remove(data);
			this.ref.remove(id);
			return null;
		});
	}

	/**
	 * 更新检查时间
	 * @param id id
	 * @param checkTime 检查时间
	 */
	public void updateCheckTime(String id, long checkTime) {
		Preconditions.checkNotNull(id, "id is null");
		this.ref.computeIfPresent(id, (k, data) -> {
			this.getQueue().remove(data);
			data.checkTime = checkTime;
			data.checkCount += 1;
			this.getQueue().add(data);
			return data;
		});
	}

	/**
	 * 更新下次执行时间
	 * @param id id
	 * @param nextTime 下次执行时间
	 */
	public void updateNextTime(String id, long nextTime) {
		id = Preconditions.checkNotNull(id, "id is null");
		this.ref.computeIfPresent(id, (k, data) -> {
			this.getQueue().remove(data);
			data.nextTime = nextTime;
			data.checkTime = nextTime;
			this.getQueue().add(data);
			return data;
		});
	}

	/**
	 * 获取队列头部数据
	 * @return 队列头部数据，如果队列为空，则返回null
	 */
	@Nullable
	public ScheduleData peek() {
		return this.getQueue().peek();
	}

	/**
	 * 获取队列头部数据，并从队列中移除
	 * @return 队列头部数据，如果队列为空，则返回null
	 */
	@Nullable
	public ScheduleData poll() {
		synchronized (ScheduleData.class) {
			ScheduleData scheduleData = this.getQueue().poll();
			if (scheduleData != null) {
				this.ref.remove(scheduleData.id);
			}
			return scheduleData;
		}
	}

	/**
	 * 获取队列
	 * @return 队列
	 */
	public PriorityQueue<ScheduleData> getQueue() {
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
	 * @param id id
	 * @return 调度数据
	 */
	@Nullable
	public ScheduleData get(String id) {
		Preconditions.checkNotNull(id, "bizId is null");
		return this.ref.get(id);
	}

	public void clean() {
		this.ref.clear();
		this.queue.clear();
	}

	@Getter
	@ToString
	public static class ScheduleData {

		private String id;

		@Setter
		private String bizId;

		@Setter
		@Nullable
		private String useCase;

		@Setter
		@Nullable
		private String scenario;

		@Setter
		@Nullable
		private Object data;

		/**
		 * 下次执行时间
		 */
		private long nextTime;

		/**
		 * 间隔描述
		 */
		@Setter
		private String intervalTime;

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

		public void setNextTime(long nextTime) {
			this.nextTime = nextTime;
			if (this.checkTime <= 0) {
				this.checkTime = nextTime;
			}
		}

		public boolean validate() {
			return !Strings.isNullOrEmpty(this.id) && !Strings.isNullOrEmpty(this.bizId) && this.nextTime > 0
					&& this.checkTime > 0;
		}

	}

}
