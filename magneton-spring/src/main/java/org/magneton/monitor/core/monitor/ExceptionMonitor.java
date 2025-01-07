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
import cn.nascent.tech.gaia.biz.monitor.core.module.ExceptionModule;
import cn.nascent.tech.gaia.biz.monitor.core.module.ModuleType;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;

/**
 * 异常监控
 * <p>
 * 用来业务发生异常时执行相应的错误信息的上报
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class ExceptionMonitor {

	/**
	 * Singleton instance.
	 */
	private static final ExceptionMonitor INSTANCE = new ExceptionMonitor();

	/**
	 * Get singleton instance.
	 * @return singleton instance
	 */
	public static ExceptionMonitor getInstance() {
		return INSTANCE;
	}

	protected ExceptionMonitor() {
	}

	/**
	 * 异常
	 * @param biz 业务ID
	 * @param monitorContext 监控上下文
	 * @param message 异常信息
	 * @param cause 异常
	 */
	public void exception(Biz biz, MonitorContext monitorContext, String message, @Nullable Throwable cause) {
		if (Strings.isNullOrEmpty(biz.getId())) {
			log.warn("[monitor] bizId is null.Ignore exception stat.");
			return;
		}
		if (Strings.isNullOrEmpty(message)) {
			message = "-";
		}

		ExceptionModule module = new ExceptionModule(biz);
		module.setModuleType(ModuleType.EXCEPTION);
		module.setMonitorContext(monitorContext);
		module.setMessage(message);
		module.setCause(cause);

		MonitorSenders.getInstance().send(module);
	}

}