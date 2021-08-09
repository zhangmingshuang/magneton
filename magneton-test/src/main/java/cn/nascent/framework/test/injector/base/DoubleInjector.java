package cn.nascent.framework.test.injector.base;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.exception.NoSupportTypeCreateException;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@TestComponent
public class DoubleInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(config.getMinDouble(), config.getMaxDouble()))
				.setScale(config.getDoubleScale(), config.getDoubleRoundingModel()).doubleValue();
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (double[].class.isAssignableFrom(inject.getInectType())) {
			return new double[length];
		}
		if (Double[].class.isAssignableFrom(inject.getInectType())) {
			return new Double[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { double.class, Double.class, double[].class, Double[].class };
	}

}
