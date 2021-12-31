package org.magneton.test.test.injector.base;

import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.config.ConfigProcessorFactory;
import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.injector.AbstractInjector;
import org.magneton.test.test.parser.Definition;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class LongInjector extends AbstractInjector {

	@Override
	public Class[] getTypes() {
		return new Class[] { long.class, Long.class, long[].class, Long[].class };
	}

	@Nullable
	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		return ConfigProcessorFactory.of(injectType).nextLong(config, definition);
	}

}
