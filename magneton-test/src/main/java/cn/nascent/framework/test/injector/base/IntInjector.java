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
public class IntInjector extends AbstractInjector {

	@Override
	public Class[] getTypes() {
		return new Class[] { int.class, Integer.class, int[].class, Integer[].class };
	}

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return ThreadLocalRandom.current().nextInt(config.getMinInt(), config.getMaxInt());
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (int[].class.isAssignableFrom(inject.getInectType())) {
			return new int[length];
		}
		if (Integer[].class.isAssignableFrom(inject.getInectType())) {
			return new Integer[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

}
