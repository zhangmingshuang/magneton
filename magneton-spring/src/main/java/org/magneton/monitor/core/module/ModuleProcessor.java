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

import cn.nascent.tech.gaia.annotation.SPI;

/**
 * 模型扩展处理器
 * <p>
 * 用来对模型进行加工处理
 * <p>
 * <pre>{@code
 *   public class ModuleProcessorImpl implements ModuleProcessor {
 *    &#64;Override
 *    public boolean processable(Module module) {
 *    	return module instanceof BaseModule;
 *    }
 *    &#64;Override
 *    public <T extends Module> T process(Module module) {
 *    	BaseModule baseModule = (BaseModule) module;
 *    	baseModule.setBizId("123");
 *    	return (T) baseModule;
 *    }
 *    }}</pre>
 *
 * @author zhangmsh.
 * @since 1.0.0
 * @see ModuleProcessors
 */
@SPI
public interface ModuleProcessor {

	/**
	 * 可处理
	 * @param module 模型
	 * @return 是否可处理
	 */
	boolean processable(Module module);

	/**
	 * 处理
	 * @param module 模型
	 * @param <T> T
	 * @return 处理后的模型
	 */
	<T extends Module> T process(Module module);

	/**
	 * 顺序
	 * <p>
	 * 顺序越小越先执行
	 * @return 顺序
	 */
	default int order() {
		return Integer.MIN_VALUE;
	}

}
