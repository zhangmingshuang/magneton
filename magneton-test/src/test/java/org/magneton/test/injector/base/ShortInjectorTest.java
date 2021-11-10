package org.magneton.test.injector.base;

import org.magneton.test.config.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.core.InjectType;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since
 */
class ShortInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinShort((short) 1).setMaxShort((short) 1);
		Short s = ChaosTest.create(short.class, config, type);
		Assertions.assertEquals((short) 1, s);
		s = ChaosTest.create(Short.class, config, type);
		Assertions.assertEquals((short) 1, s);
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1).setMinShort((short) 1).setMaxShort((short) 1);
		short[] ints = ChaosTest.create(short[].class, config, type);
		Assertions.assertEquals(1, ints.length);
		Assertions.assertEquals(1, ints[0]);
	}

}
