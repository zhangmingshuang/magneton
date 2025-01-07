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

import cn.nascent.tech.gaia.biz.monitor.core.Biz;
import cn.nascent.tech.gaia.biz.monitor.core.MonitorSenders;
import cn.nascent.tech.gaia.biz.monitor.core.module.ModuleType;
import cn.nascent.tech.gaia.biz.monitor.core.module.TimeUsedModule;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * 时间使用监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class TimeUsedMonitor {

	private static final TimeUsedMonitor INSTANCE = new TimeUsedMonitor();

	public static TimeUsedMonitor getInstance() {
		return INSTANCE;
	}

	protected TimeUsedMonitor() {

	}

	/**
	 * 耗时
	 * @param biz 业务标识
	 * @param timeUsed 耗时，毫秒
	 * @param monitorContext 监控上下文
	 * @param message 提示信息
	 */
	public void timeUsed(Biz biz, MonitorContext monitorContext, long timeUsed, @Nullable String message) {
		if (Strings.isNullOrEmpty(biz.getId())) {
			log.warn("[monitor] bizId is null. Skip timeUsed stat.");
			return;
		}
		TimeUsedModule module = new TimeUsedModule(biz);
		module.setModuleType(ModuleType.TIME_USED);
		module.setMonitorContext(monitorContext);
		module.setMessage(message);
		module.setTimeUsed(timeUsed);

		MonitorSenders.getInstance().send(module);
	}

}