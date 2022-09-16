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

import javax.validation.constraints.Positive;

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
 * @see PositiveConfigPostProcessor
 */
class PositiveConfigPostProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@RepeatedTest(10)
	void testA() {
		NegativeConfigPostProcessorTest.TestA testA = ChaosTest.create(NegativeConfigPostProcessorTest.TestA.class,
				this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

	public static class TestA {

		@Positive
		private byte b;

		@Positive
		private short s;

		@Positive
		private int i;

		@Positive
		private long l;

		@Positive
		private BigDecimal bigDecimal;

		@Positive
		private BigInteger bigInteger;

	}

}
