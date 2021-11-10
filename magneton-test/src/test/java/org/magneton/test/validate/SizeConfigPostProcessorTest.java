package org.magneton.test.validate;

import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.Size;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/25
 * @since
 */
class SizeConfigPostProcessorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	public static class TestA {

		@Size(min = 1, max = 10)
		private String str;

		@Size(min = 2, max = 2)
		private List list;

		@Size(min = 1, max = 1)
		private Map map;

		@Size(min = 1, max = 1)
		private Set set;

		@Size(min = 1, max = 2)
		private int[] array;

	}

	@Test
	void testA() {
		TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
		Human.sout(testA);
		Assertions.assertTrue(HibernateValid.valid(testA));
	}

}
