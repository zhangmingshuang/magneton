package org.magneton.test.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since 1.0.0
 */
class ConfigTest {

	@Test
	void copyOf() {
		Config config = new Config();
		config.setBooleanTrueProbability(1);

		Config copied = Config.copyOf(config);
		copied.setBooleanTrueProbability(22);
		Assertions.assertEquals(1, config.getBooleanTrueProbability());
	}

}
