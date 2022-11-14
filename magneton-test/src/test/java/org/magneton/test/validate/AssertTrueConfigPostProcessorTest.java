package org.magneton.test.validate;

import javax.validation.constraints.AssertTrue;

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
 * @since
 */
class AssertTrueConfigPostProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@Test
	void test() {
		Config coyied = Config.copyOf(this.config);
		coyied.setBooleanTrueProbability(100);
		TestA testA = ChaosTest.create(TestA.class, coyied, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

	public static class TestA {

		@AssertTrue
		private boolean bool;

	}

}
