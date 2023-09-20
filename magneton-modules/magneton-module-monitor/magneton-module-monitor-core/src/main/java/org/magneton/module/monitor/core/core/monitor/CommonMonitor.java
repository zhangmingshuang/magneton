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
import org.magneton.module.monitor.core.core.module.CommonModule;
import org.magneton.module.monitor.core.core.module.ModuleType;

import javax.annotation.Nullable;

/**
 * 通用型监控
 * <p>
 * 该告警器将发出告警信息，告警信息将被告警处理器处理
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class CommonMonitor {

	/**
	 * 单例
	 */
	public static final CommonMonitor INSTANCE = new CommonMonitor();

	/**
	 * 获取单例
	 * @return 单例
	 */
	public static CommonMonitor getInstance() {
		return INSTANCE;
	}

	protected CommonMonitor() {
		// private
	}

	/**
	 * 通用监控
	 * @param moduleType 模块类型
	 * @param bizId 业务ID
	 * @param useCase 使用用例
	 * @param scenario 场景
	 * @param message 提示信息
	 */
	public void common(ModuleType moduleType, String bizId, @Nullable String useCase, @Nullable String scenario,
			String message) {
		Preconditions.checkNotNull(moduleType, "moduleType is null");
		Preconditions.checkNotNull(bizId, "bizId is null");
		Preconditions.checkNotNull(message, "message is null");

		CommonModule module = new CommonModule();
		module.setBizId(bizId);
		module.setUseCase(useCase);
		module.setScenario(scenario);
		module.setMessage(message);
		module.setModuleType(moduleType);

		MonitorSenders.getInstance().send(module);
	}

}
