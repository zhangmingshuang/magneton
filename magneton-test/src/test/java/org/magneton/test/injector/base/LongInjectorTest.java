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

package org.magneton.test.injector.base;

import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/18
 * @since
 */
class LongInjectorTest {

	@Test
	void test() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinLong(1L).setMaxLong(1L);
		Long l = ChaosTest.create(long.class, config, type);
		Assertions.assertEquals(1, l);
		l = ChaosTest.create(Long.class, config, type);
		Assertions.assertEquals(1, l);
	}

	@Test
	void testArray() {
		InjectType type = InjectType.EXPECTED;
		Config config = new Config();

		config.setMinSize(1).setMaxSize(1).setMinLong(1L).setMaxLong(1L);
		long[] ints = ChaosTest.create(long[].class, config, type);
		Assertions.assertEquals(1, ints.length);
		Assertions.assertEquals(1, ints[0]);
	}

}
