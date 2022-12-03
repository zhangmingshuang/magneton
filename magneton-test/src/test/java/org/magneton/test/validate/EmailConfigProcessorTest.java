package org.magneton.test.validate;

import javax.validation.constraints.Email;

import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 * @see EmailConfigPostProcessor
 */
class EmailConfigProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@Test
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

	public static class TestA {

		@Email
		private String email;

		@org.hibernate.validator.constraints.Email
		private String email2;

	}

}
