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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.magneton.module.monitor.core.core.Biz;
import org.magneton.module.monitor.core.core.MonitorSenders;
import org.magneton.module.monitor.core.core.exception.ScheduleMonitorException;
import org.magneton.module.monitor.core.core.module.CronModule;
import org.magneton.module.monitor.core.core.module.ModuleType;
import org.magneton.module.monitor.core.core.task.SchedulePool;
import org.magneton.module.monitor.core.core.task.ScheduleTask;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CronMonitorScheduleTask implements ScheduleTask {

	/**
	 * 默认延迟
	 */
	public static final int MIN_DELAY = 5000;

	private final SchedulePool schedulePool;

	public CronMonitorScheduleTask(SchedulePool schedulePool) {
		this.schedulePool = schedulePool;
	}

	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while (true) {
			SchedulePool.ScheduleData scheduleData = this.schedulePool.peek();
			if (scheduleData == null) {
				return;
			}
			CronData cronData = (CronData) scheduleData.getData();
			if (Objects.isNull(cronData)) {
				throw new ScheduleMonitorException("cronData is null");
			}
			long delay = Math.max(MIN_DELAY, cronData.getDelay());
			long checkTime = scheduleData.getCheckTime();
			if (checkTime >= this.current() - delay) {
				return;
			}
			long shouldTime = scheduleData.getNextTime();
			// 需要预警
			CronModule cronModule = new CronModule();
			cronModule.setBizId(scheduleData.getBizId());
			cronModule.setUseCase(scheduleData.getUseCase());
			cronModule.setScenario(scheduleData.getScenario());
			String message = cronData.getMessage();
			if (Strings.isNullOrEmpty(message)) {
				message = "-";
			}
			cronModule.setMessage(message);
			cronModule.setModuleType(ModuleType.SCHEDULE);

			cronModule.setCron(cronData.getExpression());
			cronModule.setShouldTime(sdf.format(new Date(shouldTime)));
			cronModule.setTotalInterval(System.currentTimeMillis() - shouldTime);
			cronModule.setInterval(scheduleData.getIntervalTime());
			cronModule.setIntervalCount(scheduleData.getCheckCount());

			MonitorSenders.getInstance().send(cronModule);

			String id = Biz.valueOfBizId(scheduleData.getBizId(), scheduleData.getUseCase(),
					scheduleData.getScenario());
			this.schedulePool.updateCheckTime(id, this.current() + delay);
		}
	}

	protected long current() {
		return System.currentTimeMillis();
	}

	protected SchedulePool getSchedulePool() {
		return this.schedulePool;
	}

	/**
	 * 触发
	 * <p>
	 * 注册的触发器对应的触发节点未被触发时，会及时上报预警信息，该方法用来告知触发器当前的任务已经被触发。
	 * <p>
	 * 对应的触发器被触发之后，会重新计算下一次的触发时间。
	 * @param bizId 业务ID
	 */
	protected void touch(String bizId) {
		Preconditions.checkNotNull(bizId, "bizId is null");

		SchedulePool.ScheduleData scheduleData = this.schedulePool.get(bizId);
		if (scheduleData == null) {
			return;
		}
		Object data = scheduleData.getData();
		if (!(data instanceof CronData)) {
			return;
		}
		CronData cronData = (CronData) data;
		CronSequenceGenerator sequenceGenerator = cronData.getSequenceGenerator();
		long nextTime = sequenceGenerator.next(new Date()).getTime();
		long delay = Math.max(CronMonitorScheduleTask.MIN_DELAY, cronData.getDelay());
		this.schedulePool.updateNextTime(bizId, nextTime + delay);
	}

}