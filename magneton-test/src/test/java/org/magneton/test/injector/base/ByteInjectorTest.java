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
 * @since
 */
class ByteInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinByte((byte) 0B1).setMaxByte((byte) 0B1);
		Byte b = ChaosTest.create(byte.class, config, type);
		Assertions.assertEquals((byte) 0B1, b);
		b = ChaosTest.create(Byte.class, config, type);
		Assertions.assertEquals((byte) 0B1, b);
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1).setMinByte((byte) 1).setMaxByte((byte) 1);
		byte[] ints = ChaosTest.create(byte[].class, config, type);
		Assertions.assertEquals(1, ints.length);
		Assertions.assertEquals(1, ints[0]);
	}

}
