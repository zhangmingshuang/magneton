package org.magneton.test.injector;

import java.lang.reflect.Array;

import javax.annotation.Nullable;

import lombok.extern.slf4j.Slf4j;
import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigProcessor;
import org.magneton.test.config.ConfigProcessorFactory;
import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.PrimitiveUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@Slf4j
public abstract class AbstractInjector implements Injector {

	@Override
	public <T> T inject(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		ConfigProcessor configProcessor = ConfigProcessorFactory.of(injectType);
		if (configProcessor.nullable(config, definition)) {
			return (T) PrimitiveUtil.defaultValue(clazz);
		}
		if (clazz.isArray()) {
			Class<?> componentType = clazz.getComponentType();
			int length = configProcessor.nextSize(config, definition);
			if (length < 1) {
				return null;
			}
			Object array = Array.newInstance(componentType, length);
			for (int i = 0; i < length; i++) {
				Object value = this.injectValue(definition.resetClazz(componentType), config, injectType);
				Array.set(array, i, value);
			}
			return (T) array;
		}
		return (T) this.injectValue(definition, config, injectType);
	}

	@Nullable
	protected Object injectValue(Definition definition, Config config, InjectType injectType) {
		Class clazz = definition.getClazz();
		ConfigProcessor configProcessor = ConfigProcessorFactory.of(injectType);
		if (configProcessor.nullable(config, definition)) {
			return PrimitiveUtil.defaultValue(clazz);
		}
		return this.createValue(definition, config, injectType);
	}

	@Nullable
	protected abstract Object createValue(Definition definition, Config config, InjectType injectType);

}
