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
