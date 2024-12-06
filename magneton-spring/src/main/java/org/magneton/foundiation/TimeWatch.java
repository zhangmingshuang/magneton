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

package org.magneton.foundiation;

import com.google.common.base.Strings;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TimeWatch {

	// private static final TransmittableThreadLocal<TimeWatch> TTL_TIME_WATCH = new
	// TransmittableThreadLocal<>();
	private final Map<String, TimeTask> taskNameRef = new HashMap<>();

	private TimeTask totalTask;

	private TimeTask lastTask;

	public static TimeWatch createStarted() {
		return new TimeWatch();
	}

	public static TimeWatch createStarted(String taskName) {
		return new TimeWatch(taskName);
	}

	private TimeWatch() {
		this(null);
	}

	private TimeWatch(String currentTaskName) {
		this.totalTask = new TimeTask();
		this.totalTask.start(this, currentTaskName);
		this.addRef(this.totalTask);
		this.lastTask = this.totalTask;
	}

	public void start() {
		this.start(null);
	}

	public void start(String taskName) {
		if (this.totalTask.getStartTimeNanos() == 0) {
			this.totalTask.setStartTimeNanos(System.nanoTime());
			this.lastTask = this.totalTask;
			return;
		}
		TimeTask task = new TimeTask();
		task.start(this, taskName);
		this.lastTask = task;
	}

	public void stop() {
		this.totalTask.stop();

		if (this.lastTask != null && this.lastTask.getStopTimeNanos() == 0) {
			this.lastTask.stop();
		}

		this.taskNameRef.forEach((k, v) -> {
			if (v.getStopTimeNanos() == 0) {
				v.stop();
			}
		});
	}

	@Nullable
	public TimeTask getTask(String taskName) {
		return this.taskNameRef.get(taskName);
	}

	public TimeTask getCurrentTask() {
		return this.lastTask;
	}

	public long getCurrentTotalTime(TimeUnit timeUnit) {
		long totalTime = 0;
		if (this.totalTask.getStopTimeNanos() == 0) {
			totalTime = System.nanoTime() - this.totalTask.getStartTimeNanos();
		}
		else {
			totalTime = this.totalTask.getStopTimeNanos() - this.totalTask.getStartTimeNanos();
		}
		if (totalTime < 1) {
			totalTime = 1;
		}
		return timeUnit.convert(totalTime, TimeUnit.NANOSECONDS);
	}

	public long getCurrentTotalTime() {
		return this.getCurrentTotalTime(TimeUnit.MILLISECONDS);
	}

	protected void addRef(TimeTask task) {
		if (task != null && !Strings.isNullOrEmpty(task.getCurrentTaskName())) {
			this.taskNameRef.put(task.getCurrentTaskName(), task);
		}
	}

	public String shortSummary() {
		StringBuilder sb = new StringBuilder();
		sb.append("TimeWatch ");
		if (!Strings.isNullOrEmpty(this.totalTask.getCurrentTaskName())) {
			sb.append(this.totalTask.getCurrentTaskName());
			sb.append(" : ");
		}
		long totalTime = this.totalTask.getStopTimeNanos() - this.totalTask.getStartTimeNanos();
		sb.append("running time = ");
		sb.append(TimeUnit.MILLISECONDS.convert(totalTime, TimeUnit.NANOSECONDS));
		sb.append(" ").append(this.getTimeUnitName(TimeUnit.MILLISECONDS));
		return sb.toString();
	}

	public String stopAndPrettyPrint() {
		this.stop();
		return this.prettyPrint();
	}

	public String stopAndPrettyPrint(TimeUnit timeUnit) {
		this.stop();
		return this.prettyPrint(timeUnit);
	}

	public String prettyPrint() {
		return this.prettyPrint(TimeUnit.MILLISECONDS);
	}

	public String prettyPrint(TimeUnit timeUnit) {
		StringBuilder sb = new StringBuilder(this.shortSummary());
		sb.append("\n");
		List<TimeTask> subTasks = this.totalTask.getSubTasks();
		if (subTasks != null && !subTasks.isEmpty()) {
			long totalTime = this.totalTask.getStopTimeNanos() - this.totalTask.getStartTimeNanos();
			String timeName = this.getTimeUnitName(timeUnit);
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(2);
			nf.setGroupingUsed(false);
			NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(2);
			pf.setGroupingUsed(false);
			for (TimeTask task : subTasks) {
				long taskStopTimeNanos = task.getStopTimeNanos();
				if (taskStopTimeNanos == 0) {
					taskStopTimeNanos = System.nanoTime();
				}
				long taskTimeNanosUsed = taskStopTimeNanos - task.getStartTimeNanos();
				if (taskTimeNanosUsed < 1) {
					taskTimeNanosUsed = 1;
				}
				sb.append(" --- ");
				sb.append(pf.format((double) taskTimeNanosUsed / totalTime));
				sb.append(" [");
				sb.append(nf.format(timeUnit.convert(taskTimeNanosUsed, TimeUnit.NANOSECONDS)));
				sb.append(timeName).append("] ");
				String taskName = task.getCurrentTaskName();
				if (Strings.isNullOrEmpty(taskName)) {
					taskName = "-";
				}
				sb.append(taskName).append("\n");
				this.doSubTaskAppendTo(task, taskTimeNanosUsed, timeUnit, pf, nf, sb, null);
			}
		}

		String toPrint = sb.toString();
		return toPrint.replace("\n\n", "\n");
	}

	private void doSubTaskAppendTo(TimeTask task, long taskTimeNanosUsed, TimeUnit timeUnit, NumberFormat pf,
			NumberFormat nf, StringBuilder sb, String prefix) {
		List<TimeTask> subTasks = task.getSubTasks();
		if (subTasks == null || subTasks.isEmpty()) {
			return;
		}
		String timeName = this.getTimeUnitName(timeUnit);
		if (Strings.isNullOrEmpty(prefix)) {
			prefix = "  ";
		}
		for (TimeTask subTask : subTasks) {
			long subTaskTimeNanosUsed = subTask.getStopTimeNanos() - subTask.getStartTimeNanos();
			sb.append("\n").append(prefix);
			sb.append(" --- ");
			sb.append(pf.format((double) subTaskTimeNanosUsed / taskTimeNanosUsed));
			sb.append(" [");
			sb.append(nf.format(timeUnit.convert(subTaskTimeNanosUsed, TimeUnit.NANOSECONDS)));
			sb.append(timeName).append("] ");
			String taskName = subTask.getCurrentTaskName();
			if (Strings.isNullOrEmpty(taskName)) {
				taskName = "-";
			}
			sb.append(taskName);
			this.doSubTaskAppendTo(subTask, taskTimeNanosUsed, timeUnit, pf, nf, sb, prefix + "  ");
			sb.append("\n");
		}
	}

	private String getTimeUnitName(TimeUnit timeUnit) {
		switch (timeUnit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "us";
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s ";
			case MINUTES:
				return "m ";
			case HOURS:
				return "h ";
			case DAYS:
				return "d ";
			default:
				return "ms";
		}
	}

}
