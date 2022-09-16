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

package org.magneton.test.injector.collection;

import java.util.List;
import java.util.Set;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
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

	@Test
	void test() {
		this.config.setMinSize(1).setMaxSize(1).setMinInt(1).setMaxInt(1);
		TestA test = ChaosTest.create(TestA.class, this.config, this.angle);
		System.out.println(test);
		Assertions.assertEquals(1, test.lists.size());
		Assertions.assertEquals(1, test.sets.size());
	}

	@Test
	void testB() {
		this.config.setMinSize(1).setMaxSize(1).setMinInt(1).setMaxInt(1);
		TestB excepted = ChaosTest.create(TestB.class, this.config, this.angle);
		System.out.println(excepted);
	}

	@ToString
	public static class B {

		private int a;

		private String b;

	}

	@ToString
	public static class TestB {

		private List<B> bs;

	}

	@ToString
	public static class TestA {

		private List<Integer> lists;

		private Set<Long[]> sets;

	}

}
