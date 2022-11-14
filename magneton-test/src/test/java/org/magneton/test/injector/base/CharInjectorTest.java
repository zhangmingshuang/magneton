package org.magneton.test.injector.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since 2.0.0
 * @see CharInjector
 */
class CharInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1);
		Character integer = ChaosTest.create(char.class, config, type);
		Assertions.assertEquals(1, integer.toString().length());
		integer = ChaosTest.create(Character.class, config, type);
		Assertions.assertEquals(1, integer.toString().length());
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1);
		char[] ints = ChaosTest.create(char[].class, config, type);
		Assertions.assertEquals(1, ints.length);
	}

}
