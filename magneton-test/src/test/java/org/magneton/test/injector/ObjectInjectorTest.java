package org.magneton.test.injector;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since 2.0.0
 * @see ObjectInjector
 */
class ObjectInjectorTest {

	private static final Config config = new Config();

	@BeforeAll
	private static void init() {
		config.setMinByte((byte) 1).setMaxByte((byte) 1).setMinShort((short) 1).setMaxShort((short) 1).setMinInt(1)
				.setMaxInt(1).setMinLong(1L).setMaxLong(1L).setMinFloat(1F).setMaxFloat(1F).setMinDouble(1D)
				.setMaxDouble(1D).setBooleanTrueProbability(100).setMinBigInteger(BigInteger.ONE)
				.setMaxBigInteger(BigInteger.ONE).setMinBigDecimal(BigDecimal.ONE).setMaxBigDecimal(BigDecimal.ONE);
	}

	@ToString
	public static class TestA {

		private byte b;

		private short s;

		private int i;

		private long l;

		private float f;

		private double d;

		private boolean bool;

		private BigInteger bigInteger;

		private BigDecimal bigDecimal;

	}

	@Test
	void testA() {
		InjectType type = InjectType.EXPECTED;

		TestA testInt = ChaosTest.create(TestA.class, config, type);

		this.assertionsTestA(testInt);
	}

	private void assertionsTestA(TestA testInt) {
		Assertions.assertEquals(1, testInt.b);
		Assertions.assertEquals(1, testInt.s);
		Assertions.assertEquals(1, testInt.i);
		Assertions.assertEquals(1, testInt.l);
		Assertions.assertEquals(1, testInt.f);
		Assertions.assertEquals(1, testInt.d);
		Assertions.assertEquals(1, testInt.bigInteger.longValue());
		Assertions.assertEquals(1, testInt.bigDecimal.longValue());
		Assertions.assertTrue(testInt.bool);
	}

	@ToString
	public static class TestB {

		private TestA a;

		private int i;

	}

	@Test
	void testB() {
		TestB testB = ChaosTest.create(TestB.class, config, InjectType.EXPECTED);
		System.out.println(testB);
		this.assertionsTestA(testB.a);
		Assertions.assertEquals(1, testB.i);
	}

	@ToString
	public static class TestC {

		private int[] ints;

		private String[] strings;

	}

	@Test
	void testC() {
		TestC test = ChaosTest.create(TestC.class, config, InjectType.EXPECTED);
		System.out.println(test);
		Assertions.assertTrue(test.ints.length > 0 && test.strings.length > 0);
	}

}
