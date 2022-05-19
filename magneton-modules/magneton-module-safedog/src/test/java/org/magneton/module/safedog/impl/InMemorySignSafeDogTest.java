package org.magneton.module.safedog.impl;

import com.google.common.collect.Maps;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.safedog.sign.AbstractSignSafeDog;
import org.magneton.module.safedog.sign.InMemorySignSafeDog;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
class InMemorySignSafeDogTest {

	@Test
	void test() {
		AbstractSignSafeDog vecWatchDog = new InMemorySignSafeDog();

		Map<String, String> data = Maps.newHashMap();
		data.put("a", "a");
		data.put("b", "b");
		data.put("c", "C");
		String embedding = vecWatchDog.sign(data);

		Assertions.assertTrue(vecWatchDog.validate(embedding, 10, data, "slat"));
		Assertions.assertFalse(vecWatchDog.validate(embedding, 10, data, "slat"));
	}

}