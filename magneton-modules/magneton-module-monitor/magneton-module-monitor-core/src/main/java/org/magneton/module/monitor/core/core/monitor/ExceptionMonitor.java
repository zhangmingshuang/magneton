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
import org.magneton.module.monitor.core.core.module.ExceptionModule;
import org.magneton.module.monitor.core.core.module.ModuleType;

import javax.annotation.Nullable;

/**
 * 异常监控
 * <p>
 * 用来业务发生异常时执行相应的错误信息的上报
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
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
	 * R异常
	 * @param bizId 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @param message 异常信息
	 * @param cause 异常
	 */
	public void exception(String bizId, @Nullable String useCase, @Nullable String scenario,
			// exception info.
			String message, @Nullable Throwable cause) {
		Preconditions.checkNotNull(bizId, "bizId is null");
		Preconditions.checkNotNull(message, "message is null");

		ExceptionModule module = new ExceptionModule();
		module.setBizId(bizId).setUseCase(useCase).setScenario(scenario).setMessage(message)
				.setModuleType(ModuleType.EXCEPTION);

		module.setCause(cause);

		MonitorSenders.getInstance().send(module);
	}

}
