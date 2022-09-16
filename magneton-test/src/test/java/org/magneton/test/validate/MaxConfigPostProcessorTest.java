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

package org.magneton.test.validate;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

/**
 * .
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 * @see MaxConfigPostProcessor
 */
class MaxConfigPostProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@RepeatedTest(10)
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

	@RepeatedTest(10)
	void testB() {
		TestB testB = ChaosTest.create(TestB.class, this.config, this.angle);
		Human.sout(testB);
		Assertions.assertTrue(HibernateValid.valid(testB));
	}

	public static class TestA {

		@Max(10)
		private byte b;

		@Max(120)
		private short s;

		@Max(101)
		private int i;

		@Max(1011)
		private long l;

		@Max(100)
		private BigDecimal bigDecimal;

		@Max(1000)
		private BigInteger bigInteger;

	}

	public static class TestB {

		@Max(10)
		@Min(8)
		private byte b;

		@Max(120)
		@Min(110)
		private short s;

		@Max(101)
		@Min(100)
		private int i;

		@Max(1011)
		@Min(1010)
		private long l;

		@Max(100)
		@Min(99)
		private BigDecimal bigDecimal;

		@Max(1000)
		@Min(999)
		private BigInteger bigInteger;

	}

}
