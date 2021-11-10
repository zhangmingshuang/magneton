package org.magneton.test.injector.base;

import org.magneton.test.config.Config;
import java.math.BigDecimal;
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
class BigDecimalInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinBigDecimal(BigDecimal.ONE).setMaxBigDecimal(BigDecimal.ONE);
		BigDecimal b = ChaosTest.create(BigDecimal.class, config, type);
		Assertions.assertEquals(1, b.longValue());
	}

	@Test
	void testDemon() {
		InjectType type = InjectType.ANTI_EXPECTED;
		Config config = new Config();
		config.setMinBigDecimal(BigDecimal.ONE).setMaxBigDecimal(BigDecimal.ONE);
		BigDecimal b = ChaosTest.create(BigDecimal.class, config, type);
		System.out.println(b);
		Assertions.assertNotEquals(1, b.longValue());
	}

	@Test
	void testDefaultValue() {
		InjectType type = InjectType.DEFAULT_VALUE;
		Config config = new Config();
		config.setMinBigDecimal(BigDecimal.ONE).setMaxBigDecimal(BigDecimal.ONE);

		BigDecimal b = ChaosTest.create(BigDecimal.class, config, type);
		System.out.println(b);
		Assertions.assertNull(b);
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1).setMinBigDecimal(BigDecimal.ONE).setMaxBigDecimal(BigDecimal.ONE);
		BigDecimal[] ints = ChaosTest.create(BigDecimal[].class, config, type);
		Assertions.assertEquals(1, ints.length);
		Assertions.assertEquals(1, ints[0].longValue());
	}

}
