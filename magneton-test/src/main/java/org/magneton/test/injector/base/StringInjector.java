package org.magneton.test.injector.base;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigProcessorFactory;
import org.magneton.test.core.InjectType;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.parser.Definition;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class StringInjector extends AbstractInjector {

	@Override
	public Class[] getTypes() {
		return new Class[] { String.class, String[].class, CharSequence.class };
	}

	@Nullable
	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		return ConfigProcessorFactory.of(injectType).nextString(config, definition);
	}

}
