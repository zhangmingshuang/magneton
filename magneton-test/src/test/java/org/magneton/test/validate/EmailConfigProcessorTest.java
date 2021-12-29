package org.magneton.test.validate;

import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import javax.validation.constraints.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 * @see Jsr303EmailConfigProcessor
 */
class EmailConfigProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	public static class TestA {

		@Email
		private String email;

		@org.hibernate.validator.constraints.Email
		private String email2;

	}

	@Test
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

}
