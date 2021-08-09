package cn.nascent.framework.test.injector.base;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.exception.NoSupportTypeCreateException;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.util.ConfigUtil;
import cn.nascent.framework.test.util.RandomStringUtils;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class StringInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return RandomStringUtils.randomAlphanumeric(ConfigUtil.getRandomSize(config));
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (String[].class.isAssignableFrom(inject.getInectType())) {
			return new String[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { String.class, String[].class, CharSequence.class };
	}

}
