package org.magneton.test.validate;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.hibernate.validator.constraints.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

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
