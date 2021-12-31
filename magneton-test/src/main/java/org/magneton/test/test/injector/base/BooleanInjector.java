package org.magneton.test.test.injector.base;

import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.config.ConfigProcessorFactory;
import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.injector.AbstractInjector;
import org.magneton.test.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class BooleanInjector extends AbstractInjector {

	@Override
	public Class[] getTypes() {
		return new Class[] { boolean.class, Boolean.class, boolean[].class, Boolean[].class };
	}

	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		return ConfigProcessorFactory.of(injectType).nextBoolean(config, definition);
	}

}
