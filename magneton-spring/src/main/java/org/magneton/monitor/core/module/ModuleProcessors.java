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

package org.magneton.monitor.core.module;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.magneton.spring.core.foundation.spi.SPILoader;

import java.util.Collections;
import java.util.List;

/**
 * 模型处理器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class ModuleProcessors {

	/**
	 * 单例
	 */
	private static final ModuleProcessors INSTANCE = new ModuleProcessors();

	/**
	 * 获取单例
	 * @return 单例
	 */
	public static ModuleProcessors getInstance() {
		return INSTANCE;
	}

	/**
	 * 模型处理器
	 */
	private final List<ModuleProcessor> processors = Lists.newArrayListWithCapacity(8);

	protected ModuleProcessors() {
		this.addAndSort(SPILoader.loadInstance(ModuleProcessor.class));
	}

	/**
	 * 注册模型处理器
	 * @param processor 模型处理器
	 */
	public void registerProcessor(ModuleProcessor processor) {
		Preconditions.checkNotNull(processor, "processor is null");

		this.addAndSort(Collections.singletonList(processor));
	}

	/**
	 * 处理模型
	 * @param module 模型
	 * @param <T> T
	 * @return 处理后的模型
	 */
	public <T extends Module> T process(T module) {
		Preconditions.checkNotNull(module, "module is null");

		if (CollectionUtil.isEmpty(this.processors)) {
			return module;
		}
		T processedModule = module;
		for (ModuleProcessor moduleProcessor : this.processors) {
			if (moduleProcessor.processable(processedModule)) {
				processedModule = moduleProcessor.process(processedModule);
			}
		}
		return processedModule;
	}

	/**
	 * 获取模型处理器列表
	 * @return 模型处理器
	 */
	public List<ModuleProcessor> getProcessors() {
		return this.processors;
	}

	/**
	 * 清空
	 */
	public void clean() {
		this.processors.clear();
	}

	private void addAndSort(List<ModuleProcessor> processor) {
		if (CollectionUtil.isEmpty(processor)) {
			return;
		}
		this.processors.addAll(processor);
		this.processors.sort((o1, o2) -> {
			if (o1.order() > o2.order()) {
				return 1;
			}
			else if (o1.order() < o2.order()) {
				return -1;
			}
			return 0;
		});
	}

}