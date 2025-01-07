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

package org.magneton.monitor.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.nascent.tech.gaia.biz.monitor.core.module.Module;
import cn.nascent.tech.gaia.foundation.spi.SPILoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 监控处理器执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see MonitorProcessor
 */
@Slf4j
public class MonitorProcessors {

	/**
	 * 单例
	 */
	private static final MonitorProcessors INSTANCE = new MonitorProcessors();

	public static MonitorProcessors getInstance() {
		return INSTANCE;
	}

	/**
	 * 监控处理器列表
	 */
	private final List<MonitorProcessor> processors = Lists.newArrayListWithCapacity(16);

	protected MonitorProcessors() {
		this.processors.addAll(SPILoader.loadInstance(MonitorProcessor.class));
		this.reorder();
	}

	/**
	 * 重新排序, 由小到大排序
	 */
	private void reorder() {
		this.processors.sort((o1, o2) -> {
			int order1 = o1.getOrder();
			int order2 = o2.getOrder();
			return Integer.compare(order1, order2);
		});
	}

	/**
	 * 注册监控处理器
	 * @param monitorProcessor 监控处理器
	 */
	public void register(MonitorProcessor monitorProcessor) {
		Preconditions.checkNotNull(monitorProcessor, "monitorProcessor can not be null");
		this.processors.add(monitorProcessor);
		this.reorder();
	}

	/**
	 * 注销监控处理器
	 * @param monitorProcessor 监控处理器
	 */
	public void unregister(MonitorProcessor monitorProcessor) {
		Preconditions.checkNotNull(monitorProcessor, "monitorProcessor can not be null");
		this.processors.remove(monitorProcessor);
		this.reorder();
	}

	/**
	 * 前置发送处理
	 * <p>
	 * 该方法在所有的发送器发送前执行
	 * @param module 模型
	 * @return 是否继续发送， true 继续发送，false 不发送
	 */
	public boolean preSend(Module module) {
		Preconditions.checkNotNull(module, "module can not be null");
		if (CollectionUtil.isEmpty(this.processors)) {
			return true;
		}

		for (MonitorProcessor monitorProcessor : this.processors) {
			try {
				if (!monitorProcessor.preSend(module)) {
					return false;
				}
			}
			catch (Throwable e) {
				log.error("[monitor] preSend error", e);
				return false;
			}
		}
		return true;
	}

	/**
	 * 后置发送处理
	 * <p>
	 * 该方法在所有的发送器发送完成后执行
	 * @param module 模型
	 */
	public void postSend(Module module) {
		Preconditions.checkNotNull(module, "module can not be null");
		if (CollectionUtil.isEmpty(this.processors)) {
			return;
		}
		for (MonitorProcessor monitorProcessor : this.processors) {
			monitorProcessor.postSend(module);
		}
	}

	/**
	 * 获取监控处理器列表
	 * @return 监控处理器列表
	 */
	public List<MonitorProcessor> getProcessors() {
		if (this.processors.isEmpty()) {
			return Collections.emptyList();
		}
		return Lists.newArrayList(this.processors);
	}

	/**
	 * 清空监控处理器列表
	 */
	public void clean() {
		this.processors.clear();
	}

}