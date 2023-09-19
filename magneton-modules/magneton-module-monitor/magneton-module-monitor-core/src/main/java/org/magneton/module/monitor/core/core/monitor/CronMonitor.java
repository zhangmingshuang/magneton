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

package org.magneton.module.monitor.core.core.monitor;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.google.common.base.Preconditions;
import org.magneton.module.monitor.core.core.Biz;
import org.magneton.module.monitor.core.core.task.SchedulePool;
import org.magneton.module.monitor.core.core.task.ScheduleTasks;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Locale;

/**
 * 调度监控
 * <p>
 * 该监控用来监控Cron表达的对应任务的执行情况。
 * <p>
 * 该监控器分为两个步骤：
 * <ul>
 * <li>1. 注册触发器， 如 {@link #registerTrigger}，注册一个Cron表达式触发器</li>
 * <li>2. 触发，使用方法 {@link #touch}</li>
 * </ul>
 * 注册的触发器会根据Cron表达式的规则，如果对应的业务没有在预期的Cron周期内被触发，则会进行预警信息的上报。
 * <p>
 * 如果在Cron的周期内，{@link #touch}方法被调用，则会重置该业务的触发点， 当下次执行周期到达时，如果没有再次被触发，则会进行预警信息的上报。
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class CronMonitor {

	/**
	 * Singleton instance.
	 */
	private static final CronMonitor INSTANCE = new CronMonitor();

	protected final CronMonitorScheduleTask task;

	/**
	 * Get singleton instance.
	 * @return singleton instance
	 */
	public static CronMonitor getInstance() {
		return INSTANCE;
	}

	protected CronMonitor() {
		this(new CronMonitorScheduleTask(new SchedulePool()));
	}

	protected CronMonitor(CronMonitorScheduleTask cronMonitorScheduleTask) {
		this.task = Preconditions.checkNotNull(cronMonitorScheduleTask, "cronMonitorScheduleTask");
		ScheduleTasks.registerMonitor(cronMonitorScheduleTask);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 */
	public void registerTrigger(String bizId, String expression) {
		this.registerTrigger(bizId, null, null, expression, CronMonitorScheduleTask.MIN_DELAY, null);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 */
	public void registerTrigger(Biz biz, String expression) {
		this.registerTrigger(biz, expression, CronMonitorScheduleTask.MIN_DELAY, null);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 * @param message 提示信息
	 */
	public void registerTrigger(String bizId, String expression, @Nullable String message) {
		this.registerTrigger(bizId, null, null, expression, CronMonitorScheduleTask.MIN_DELAY, message);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 * @param message 提示信息
	 */
	public void registerTrigger(Biz biz, String expression, @Nullable String message) {
		this.registerTrigger(biz, expression, CronMonitorScheduleTask.MIN_DELAY, message);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位秒
	 */
	public void registerTrigger(String bizId, String expression, long delay) {
		this.registerTrigger(bizId, null, null, expression, delay, null);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位秒
	 */
	public void registerTrigger(Biz biz, String expression, long delay) {
		this.registerTrigger(biz, expression, delay, null);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位秒
	 * @param message 提示信息
	 */
	public void registerTrigger(String bizId, String expression, long delay, @Nullable String message) {
		this.registerTrigger(bizId, null, null, expression, delay, message);
	}

	/**
	 * 注册触发器
	 * @param biz 业务
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间，最小值为5000，单位秒
	 * @param message 提示信息
	 */
	public void registerTrigger(Biz biz, String expression, long delay, @Nullable String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.registerTrigger(biz.getId(), biz.getUseCase(), biz.getScenario(), expression, delay, message);
	}

	/**
	 * 注册触发器
	 * @param bizId 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @param expression cron表达式
	 * @param delay 促发预警后，下一次的延时时间
	 * @param message 提示信息
	 */
	private void registerTrigger(String bizId, @Nullable String useCase, @Nullable String scenario,
			// schedule infos.
			String expression, long delay, @Nullable String message) {
		Preconditions.checkNotNull(bizId, "bizId is null");
		Preconditions.checkNotNull(expression, "expression is null");
		Preconditions.checkArgument(delay >= CronMonitorScheduleTask.MIN_DELAY,
				String.format("delay must be greater than %s", CronMonitorScheduleTask.MIN_DELAY));

		SchedulePool.ScheduleData data = new SchedulePool.ScheduleData();
		data.setBizId(bizId);
		data.setUseCase(useCase);
		data.setScenario(scenario);

		CronSequenceGenerator sequenceGenerator = new CronSequenceGenerator(expression);
		CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ));
		CronDescriptor descriptor = CronDescriptor.instance(Locale.CHINA);
		String intervalTime = descriptor.describe(parser.parse(expression));
		data.setIntervalTime(intervalTime);
		data.setData(new CronData(expression, sequenceGenerator, delay, message));
		data.setNextTime(sequenceGenerator.next(new Date()).getTime());
		data.setCheckTime(data.getNextTime());

		String id = Biz.valueOfBizId(bizId, useCase, scenario);
		this.task.getSchedulePool().add(id, data);
	}

	/**
	 * 触发
	 * <p>
	 * 注册的触发器对应的触发节点未被触发时，会及时上报预警信息，该方法用来告知触发器当前的任务已经被触发。
	 * <p>
	 * 对应的触发器被触发之后，会重新计算下一次的触发时间。
	 * @param biz 业务
	 */
	public void touch(Biz biz) {
		Preconditions.checkNotNull(biz, "biz is null");
		this.touch(biz.toBizId());
	}

	/**
	 * 触发
	 * <p>
	 * 注册的触发器对应的触发节点未被触发时，会及时上报预警信息，该方法用来告知触发器当前的任务已经被触发。
	 * <p>
	 * 对应的触发器被触发之后，会重新计算下一次的触发时间。
	 * @param bizId 业务ID
	 */
	public void touch(String bizId) {
		this.task.touch(bizId);
	}

}
