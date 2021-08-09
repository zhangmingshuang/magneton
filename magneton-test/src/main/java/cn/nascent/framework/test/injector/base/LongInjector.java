package cn.nascent.framework.test.injector.base;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.exception.NoSupportTypeCreateException;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import java.util.concurrent.ThreadLocalRandom;

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

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return ThreadLocalRandom.current().nextLong(config.getMinLong(), config.getMaxLong());
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (long[].class.isAssignableFrom(inject.getInectType())) {
			return new long[length];
		}
		if (Long[].class.isAssignableFrom(inject.getInectType())) {
			return new Long[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

}
