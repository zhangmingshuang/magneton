package cn.nascent.framework.test.injector.base;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.exception.NoSupportTypeCreateException;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.util.ConfigUtil;
import cn.nascent.framework.test.util.RandomStringUtils;
import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@TestComponent
public class CharInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		int length = ConfigUtil.getRandomSize(config);
		return RandomStringUtils.randomAlphanumeric(length).charAt(ThreadLocalRandom.current().nextInt(length));
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (char[].class.isAssignableFrom(inject.getInectType())) {
			return new char[length];
		}
		if (Character[].class.isAssignableFrom(inject.getInectType())) {
			return new Character[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { char.class, Character.class, char[].class, Character[].class };
	}

}
