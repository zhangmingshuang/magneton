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

import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;
import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

/**
 * .
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 * @see RangeConfigPostProcessor
 */
class RangeConfigPostProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@RepeatedTest(10)
	void testB() {
		MaxConfigPostProcessorTest.TestB testB = ChaosTest.create(MaxConfigPostProcessorTest.TestB.class, this.config,
				this.angle);
		Human.sout(testB);
		Assertions.assertTrue(HibernateValid.valid(testB));
	}

	public static class TestB {

		@Range(min = 8, max = 10)
		private byte b;

		@Range(min = 110, max = 120)
		private short s;

		@Range(min = 100, max = 101)
		private int i;

		@Range(min = 1010, max = 1011)
		private long l;

		@Range(min = 99, max = 100)
		private BigDecimal bigDecimal;

		@Range(min = 999, max = 1000)
		private BigInteger bigInteger;

	}

}
