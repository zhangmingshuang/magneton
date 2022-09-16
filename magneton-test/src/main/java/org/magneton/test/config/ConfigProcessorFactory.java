/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.config;

import java.util.List;
import java.util.Map;

import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.AfterAutowrited;
import org.magneton.test.core.ChaosContext;
import org.magneton.test.core.InjectType;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;

/**
 * .
 *
 * @author zhangmsh 2021/8/19
 * @since 2.0.0
 */
@TestComponent
public class ConfigProcessorFactory implements AfterAutowrited {

	private final Map<InjectType, ConfigProcessor> cacheConfigProcessors = Maps.newHashMap();

	@TestAutowired
	private List<ConfigProcessor> configProcessors;

	@TestAutowired(required = false)
	private List<ConfigPostProcessor> configPostProcessors;

	public static ConfigProcessor of(InjectType injectType) {
		ConfigProcessorFactory configProcessorFactory = ChaosContext.getComponent(ConfigProcessorFactory.class);
		Verify.verifyNotNull(configProcessorFactory, "inject type factory found");

		ConfigProcessor injectTypeProcessor = configProcessorFactory.cacheConfigProcessors.get(injectType);
		Verify.verifyNotNull(injectTypeProcessor, "inject type %s not config processor found", injectType);
		return new ConfigPostProcessorAdapter(injectTypeProcessor, configProcessorFactory.configPostProcessors)
				.getProxyConfigProcessor();
	}

	@Override
	public void afterAutowrited() {
		Verify.verifyNotNull(this.configProcessors, "not inject type config processor found");
		for (ConfigProcessor injectTypeProcessor : this.configProcessors) {
			ConfigWith configWith = injectTypeProcessor.getClass().getAnnotation(ConfigWith.class);
			if (configWith == null) {
				continue;
			}
			this.cacheConfigProcessors.put(configWith.value(), injectTypeProcessor);
		}
	}

}
