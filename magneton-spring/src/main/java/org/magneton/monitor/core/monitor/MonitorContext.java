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

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 监控上下文
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class MonitorContext {

	private static final MonitorContext EMPTY = new MonitorContext();

	public static MonitorContext empty() {
		EMPTY.clear();
		return EMPTY;
	}

	/**
	 * 全局变量
	 */
	private final Map<String, String> context = Maps.newConcurrentMap();

	public static MonitorContext valueOf(String key, String value) {
		MonitorContext monitorContext = new MonitorContext();
		monitorContext.put(key, value);
		return monitorContext;
	}

	public static MonitorContext valueOf(Map<String, String> map) {
		MonitorContext monitorContext = new MonitorContext();
		monitorContext.putAll(map);
		return monitorContext;
	}

	public Map<String, String> getContext() {
		return this.context;
	}

	public void putAll(Map<String, String> map) {
		this.context.putAll(map);
	}

	public void put(String key, String value) {
		this.context.put(key, value);
	}

	public void remove(String key) {
		this.context.remove(key);
	}

	public void clear() {
		this.context.clear();
	}

	public boolean isEmpty() {
		return this.context.isEmpty();
	}

}
