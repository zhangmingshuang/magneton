package org.magneton.test.test.config;

import java.util.List;
import java.util.Map;

import org.magneton.test.test.annotation.TestAutowired;
import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.core.AfterAutowrited;
import org.magneton.test.test.core.ChaosContext;
import org.magneton.test.test.core.InjectType;
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
