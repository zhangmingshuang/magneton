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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.magneton.test.parser.Definition;
import lombok.Getter;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
public class ConfigPostProcessorAdapter implements ConfigPostProcessor, InvocationHandler {

	private ConfigProcessor configProcessor;

	@Getter
	private ConfigProcessor proxyConfigProcessor;

	private List<ConfigPostProcessor> configPostProcessors;

	public ConfigPostProcessorAdapter(ConfigProcessor configProcessor, List<ConfigPostProcessor> configPostProcessors) {
		this.configProcessor = configProcessor;
		this.configPostProcessors = configPostProcessors;
		this.proxyConfigProcessor = (ConfigProcessor) Proxy.newProxyInstance(
				configProcessor.getClass().getClassLoader(), new Class[] { ConfigProcessor.class }, this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if ("toString".equals(method.getName())) {
			return "proxy string...";
		}
		Config config = Config.copyOf(((Config) args[0]));
		Definition definition = (Definition) args[1];
		this.beforeConfig(config, definition);
		Object result = method.invoke(this.configProcessor, config, definition);
		this.afterConfig(result, config, (Definition) args[1]);
		return result;
	}

	@Override
	public void beforeConfig(Config config, Definition definition) {
		if (this.configPostProcessors == null) {
			return;
		}
		for (ConfigPostProcessor configPostProcessor : this.configPostProcessors) {
			configPostProcessor.beforeConfig(config, definition);
		}
	}

	@Override
	public void afterConfig(Object result, Config config, Definition definition) {
		if (this.configPostProcessors == null) {
			return;
		}
		for (ConfigPostProcessor configPostProcessor : this.configPostProcessors) {
			configPostProcessor.afterConfig(result, config, definition);
		}
	}

}
