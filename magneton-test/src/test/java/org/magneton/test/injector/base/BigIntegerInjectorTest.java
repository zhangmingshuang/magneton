package org.magneton.test.injector.base;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since
 */
class BigIntegerInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinBigInteger(BigInteger.ONE).setMaxBigInteger(BigInteger.ONE);
		BigInteger b = ChaosTest.create(BigInteger.class, config, type);
		Assertions.assertEquals(1, b.longValue());
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1).setMinBigInteger(BigInteger.ONE).setMaxBigInteger(BigInteger.ONE);
		BigInteger[] ints = ChaosTest.create(BigInteger[].class, config, type);
		Assertions.assertEquals(1, ints.length);
		Assertions.assertEquals(1, ints[0].longValue());
	}

}
