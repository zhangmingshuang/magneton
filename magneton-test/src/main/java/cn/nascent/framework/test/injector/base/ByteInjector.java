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
public class ByteInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return (byte) ThreadLocalRandom.current().nextInt(config.getMinByte(), config.getMaxByte());
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		if (byte[].class.isAssignableFrom(inject.getInectType())) {
			return new byte[length];
		}
		if (Byte[].class.isAssignableFrom(inject.getInectType())) {
			return new Byte[length];
		}
		throw new NoSupportTypeCreateException(inject.getName());
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { byte.class, Byte.class, byte[].class, Byte[].class };
	}

}
