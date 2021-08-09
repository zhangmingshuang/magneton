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
 * @since 2.0.0
 */
@TestComponent
public class FloatInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(config.getMinFloat(), config.getMaxFloat()))
				.setScale(config.getFloatScale(), config.getFloatRoundingModel()).floatValue();
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (float[].class.isAssignableFrom(inject.getInectType())) {
			return new float[length];
		}
		if (Float[].class.isAssignableFrom(inject.getInectType())) {
			return new Float[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { float.class, Float.class, float[].class, Float[].class };
	}

}
