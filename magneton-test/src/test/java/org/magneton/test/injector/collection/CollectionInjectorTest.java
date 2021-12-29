package org.magneton.test.injector.collection;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import java.util.List;
import java.util.Set;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
class CollectionInjectorTest {

	private final Config config = new Config();

	private final InjectType angle = InjectType.EXPECTED;

	@ToString
	public static class TestA {

		private List<Integer> lists;

		private Set<Long[]> sets;

	}

	@Test
	void test() {
		this.config.setMinSize(1).setMaxSize(1).setMinInt(1).setMaxInt(1);
		TestA test = ChaosTest.create(TestA.class, this.config, this.angle);
		System.out.println(test);
		Assertions.assertEquals(1, test.lists.size());
		Assertions.assertEquals(1, test.sets.size());
	}

	@ToString
	public static class TestB {

		private List<B> bs;

	}

	@ToString
	public static class B {

		private int a;

		private String b;

	}

	@Test
	void testB() {
		this.config.setMinSize(1).setMaxSize(1).setMinInt(1).setMaxInt(1);
		TestB excepted = ChaosTest.create(TestB.class, this.config, this.angle);
		System.out.println(excepted);
	}

}
