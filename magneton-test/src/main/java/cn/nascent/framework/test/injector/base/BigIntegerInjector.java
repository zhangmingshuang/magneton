package cn.nascent.framework.test.injector.base;

import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.AbstractInjector;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
public class BigIntegerInjector extends AbstractInjector {

	@Override
	protected Object createValue(Config config, InjectType injectType, Inject inject) {
		return BigInteger.valueOf(ThreadLocalRandom.current().nextLong(config.getMinBigInterger().longValue(),
				config.getMaxBigInteger().longValue()));
	}

	@Override
	protected Object createArray(Config config, InjectType injectType, Inject inject, Integer length) {
		return new BigInteger[length];
	}

	@Override
	public Class[] getTypes() {
		return new Class[] { BigInteger.class, BigInteger[].class };
	}

}
