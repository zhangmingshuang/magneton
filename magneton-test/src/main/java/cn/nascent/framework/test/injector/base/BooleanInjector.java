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
 * @since
 */
@TestComponent
public class BooleanInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return ThreadLocalRandom.current().nextBoolean();
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (boolean[].class.isAssignableFrom(inject.getInectType())) {
			return new boolean[length];
		}
		if (Boolean[].class.isAssignableFrom(inject.getInectType())) {
			return new Boolean[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { boolean.class, Boolean.class, boolean[].class, Boolean[].class };
	}

}
