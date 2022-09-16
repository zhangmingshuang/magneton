/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.injector;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
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

	@Test
	void testB() {
		TestB testB = ChaosTest.create(TestB.class, config, InjectType.EXPECTED);
		System.out.println(testB);
		this.assertionsTestA(testB.a);
		Assertions.assertEquals(1, testB.i);
	}

	@Test
	void testC() {
		TestC test = ChaosTest.create(TestC.class, config, InjectType.EXPECTED);
		System.out.println(test);
		Assertions.assertTrue(test.ints.length > 0 && test.strings.length > 0);
	}

	@ToString
	public static class TestB {

		private TestA a;

		private int i;

	}

	@ToString
	public static class TestC {

		private int[] ints;

		private String[] strings;

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

}
