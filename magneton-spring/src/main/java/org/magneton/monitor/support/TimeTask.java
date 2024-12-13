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

package org.magneton.monitor.support;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间步长
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class TimeTask {

	private TimeWatch timeWatch;

	private String currentTaskName;

	private long startTimeNanos;

	private long stopTimeNanos;

	private List<TimeTask> subTasks;

	protected void start(TimeWatch timeWatch, String taskName) {
		this.timeWatch = timeWatch;
		this.currentTaskName = taskName;
		this.startTimeNanos = System.nanoTime();
		this.timeWatch.addRef(this);
	}

	public void restart() {
		this.startTimeNanos = System.nanoTime();
	}

	public void stop() {
		this.stopTimeNanos = System.nanoTime();
		if (this.subTasks != null) {
			for (TimeTask subTask : this.subTasks) {
				if (subTask.getStopTimeNanos() == 0) {
					subTask.stop();
				}
			}
		}
	}

	public TimeTask startSubTask(String taskName) {
		TimeTask subTask = new TimeTask();
		subTask.start(this.timeWatch, taskName);
		if (this.subTasks == null) {
			this.subTasks = new ArrayList<>();
		}
		this.subTasks.add(subTask);
		return subTask;
	}

}
