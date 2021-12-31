package org.magneton.test.test.injector.base;

import org.magneton.test.test.annotation.TestComponent;
import org.magneton.test.test.config.Config;
import org.magneton.test.test.config.ConfigProcessorFactory;
import org.magneton.test.test.core.InjectType;
import org.magneton.test.test.injector.AbstractInjector;
import org.magneton.test.test.parser.Definition;

import java.math.BigDecimal;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
public class BigDecimalInjector extends AbstractInjector {

	@Override
	public Class[] getTypes() {
		return new Class[] { BigDecimal.class, BigDecimal[].class };
	}

	@Nullable
	@Override
	protected Object createValue(Definition definition, Config config, InjectType injectType) {
		return ConfigProcessorFactory.of(injectType).nextBigDecimal(config, definition);
	}

}
