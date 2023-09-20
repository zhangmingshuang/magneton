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
import org.magneton.module.monitor.core.core.MonitorSenders;
import org.magneton.module.monitor.core.core.module.ModuleType;
import org.magneton.module.monitor.core.core.module.TimeUsedModule;

import javax.annotation.Nullable;

/**
 * 时间使用监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class TimeUsedMonitor {

	private static final TimeUsedMonitor INSTANCE = new TimeUsedMonitor();

	public static TimeUsedMonitor getInstance() {
		return INSTANCE;
	}

	protected TimeUsedMonitor() {
	}

	/**
	 * 耗时
	 * @param bizId 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public void timeUsed(String bizId, @Nullable String useCase, @Nullable String scenario,
			// time info.
			long timeUsed, @Nullable String message) {
		Preconditions.checkNotNull(bizId, "bizId is null");
		Preconditions.checkArgument(timeUsed >= 0, "timeUsed must be >= 0");

		TimeUsedModule module = new TimeUsedModule();
		module.setBizId(bizId).setUseCase(useCase).setScenario(scenario).setMessage(message)
				.setModuleType(ModuleType.TIME_USED);

		module.setTimeUsed(timeUsed);

		MonitorSenders.getInstance().send(module);
	}

}
