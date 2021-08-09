package cn.nascent.framework.test.injector.base;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
public class BigDecimalInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		double value = ThreadLocalRandom.current().nextDouble(config.getMinBigDecimal().doubleValue(),
				config.getMaxBigDecimal().doubleValue());
		return new BigDecimal(value).setScale(config.getBigDecimalScale(), config.getBigDecimalRoundingModel());
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		return new BigDecimal[length];
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { BigDecimal.class, BigDecimal[].class };
	}

}
