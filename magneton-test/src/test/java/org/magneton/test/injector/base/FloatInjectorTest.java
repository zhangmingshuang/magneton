package org.magneton.test.injector.base;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since
 */
class FloatInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinFloat(1F).setMaxFloat(1F);
		Float integer = ChaosTest.create(float.class, config, type);
		Assertions.assertEquals(1, integer);
		integer = ChaosTest.create(Float.class, config, type);
		Assertions.assertEquals(1, integer);
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1).setMinFloat(1.0F).setMaxFloat(1.0F);
		float[] ints = ChaosTest.create(float[].class, config, type);
		Assertions.assertEquals(1, ints.length);
		Assertions.assertEquals(1, ints[0]);
	}

}
