package org.magneton.test.validate;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
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

	@RepeatedTest(100)
	void testT1() {
		T1 t1 = ChaosTest.createExcepted(T1.class);
		System.out.println(t1);
		Assertions.assertTrue(t1.getB2().compareTo(new BigDecimal(1.2)) != -1);
		Assertions.assertTrue(t1.getB3().compareTo(new BigDecimal(1.3)) != 1);
		Assertions.assertTrue(
				t1.getB4().compareTo(new BigDecimal(1.2)) != -1 && t1.getB4().compareTo(new BigDecimal(1.3)) != 1);
	}

	@Data
	public static class T1 {

		private BigDecimal b1;

		@DecimalMin("1.2")
		private BigDecimal b2;

		@DecimalMax("1.3")
		private BigDecimal b3;

		@DecimalMin("1.2")
		@DecimalMax("1.3")
		private BigDecimal b4;

	}

}
