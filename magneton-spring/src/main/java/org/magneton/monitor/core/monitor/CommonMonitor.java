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

import lombok.extern.slf4j.Slf4j;
import org.magneton.monitor.core.Biz;
import org.magneton.monitor.core.MonitorSenders;
import org.magneton.monitor.core.module.CommonModule;
import org.magneton.monitor.core.module.ModuleType;

/**
 * 通用型监控
 * <p>
 * 该告警器将发出告警信息，告警信息将被告警处理器处理
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
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
	 * @param biz 业务标识
	 * @param monitorContext 监控上下文
	 * @param message 提示信息
	 */
	public void common(ModuleType moduleType, Biz biz, MonitorContext monitorContext, String message) {
		if (moduleType == null) {
			log.warn("[monitor] moduleType is null. Skip common stat.");
			return;
		}
		if (biz.getId() == null) {
			log.warn("[monitor] bizId is null. Skip common stat.");
			return;
		}
		if (message == null) {
			log.warn("[monitor] message is null. Skip common stat.");
			return;
		}
		CommonModule module = new CommonModule(biz);
		module.setMonitorContext(monitorContext);
		module.setMessage(message);
		module.setModuleType(moduleType);

		MonitorSenders.getInstance().send(module);
	}

}