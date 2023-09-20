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

package org.magneton.module.monitor.core;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.magneton.module.monitor.core.core.Biz;
import org.magneton.module.monitor.core.core.module.ModuleType;
import org.magneton.module.monitor.core.core.monitor.*;

import javax.annotation.Nullable;

/**
 * 监控
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class Monitor {

	private Monitor() {
		// private
	}

	/**
	 * 统计类监控
	 * @return 统计类监控
	 */
	public static StatMonitor stat() {
		return StatMonitor.getInstance();
	}

	/**
	 * 调度类监控
	 * @return 调度类监控
	 */
	public static CronMonitor schedule() {
		return CronMonitor.getInstance();
	}

	/**
	 * 异常
	 * @param bizId 业务id
	 * @param message 提示信息
	 */
	public static void exception(String bizId, String message) {
		exception(bizId, message, null);
	}

	/**
	 * 异常
	 * @param biz 业务类型
	 * @param message 提示信息
	 */
	public static void exception(Biz biz, String message) {
		exception(biz, message, null);
	}

	/**
	 * 异常
	 * @param bizId 业务类型
	 * @param message 提示信息
	 * @param cause 异常
	 */
	public static void exception(String bizId, String message, @Nullable Throwable cause) {
		ExceptionMonitor.getInstance().exception(bizId, null, null,
				// exception info.
				message, cause);
	}

	/**
	 * 异常
	 * @param biz 业务类型
	 * @param message 提示信息
	 * @param cause 异常
	 */
	public static void exception(Biz biz, String message, @Nullable Throwable cause) {
		Preconditions.checkNotNull(biz, "biz is null");
		ExceptionMonitor.getInstance().exception(biz.getId(), biz.getUseCase(), biz.getScenario(),
				// exception info.
				message, cause);
	}

	/**
	 * 耗时
	 * @param bizId 业务id
	 * @param timeUsed 耗时，毫秒
	 */
	public static void timeUsed(String bizId, long timeUsed) {
		TimeUsedMonitor.getInstance().timeUsed(bizId, null, null, timeUsed, null);
	}

	/**
	 * 耗时
	 * @param bizId 业务id
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public static void timeUsed(String bizId, long timeUsed, @Nullable String message) {
		TimeUsedMonitor.getInstance().timeUsed(bizId, null, null, timeUsed, message);
	}

	/**
	 * 耗时
	 * @param biz 业务类型
	 * @param timeUsed 耗时，毫秒
	 */
	public static void timeUsed(Biz biz, long timeUsed) {
		Preconditions.checkNotNull(biz, "biz is null");
		TimeUsedMonitor.getInstance().timeUsed(biz.getId(), biz.getUseCase(), biz.getScenario(), timeUsed, null);
	}

	/**
	 * 耗时
	 * @param biz 业务类型
	 * @param timeUsed 耗时，毫秒
	 * @param message 提示信息
	 */
	public static void timeUsed(Biz biz, long timeUsed, @Nullable String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		TimeUsedMonitor.getInstance().timeUsed(biz.getId(), biz.getUseCase(), biz.getScenario(), timeUsed, message);
	}

	/**
	 * 告警
	 * @param bizId 业务id
	 * @param message 提示信息
	 */
	public static void alarm(String bizId, String message) {
		CommonMonitor.getInstance().common(ModuleType.ALARM, bizId, null, null, message);
	}

	/**
	 * 告警
	 * @param biz 业务类型
	 * @param message 提示信息
	 */
	public static void alarm(Biz biz, String message) {
		Preconditions.checkNotNull(biz, "biz is null");
		CommonMonitor.getInstance().common(ModuleType.ALARM, biz.getId(), biz.getUseCase(), biz.getScenario(), message);
	}

}
