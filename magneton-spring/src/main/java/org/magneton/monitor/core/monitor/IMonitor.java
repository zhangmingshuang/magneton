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

import java.util.function.Supplier;

/**
 * 监控
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public interface IMonitor {

	/**
	 * 添加静态的上下文
	 * @param monitorContext 监控上下文
	 */
	void attachContext(MonitorContext monitorContext);

	/**
	 * 添加动态的上下文
	 * <p>
	 * 动态上下文的数据优先于静态上下文。
	 * @param supplier 监控上下文提供者
	 */
	void attachContext(Supplier<MonitorContext> supplier);

}
