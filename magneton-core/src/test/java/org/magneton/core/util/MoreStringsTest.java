package org.magneton.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/30
 */
class MoreStringsTest {

	@Test
	void format() {
		String format = MoreStrings.format("1%s1", "1", "2");
		Assertions.assertEquals("12", format, "format error");

		String format1 = MoreStrings.format("0:%s0, 1:%s1, 2:%s0", "a", "b");
		Assertions.assertEquals("0:a, 1:b, 2:a", format1, "format error");
	}

	@Test
	void isAnyNullOrEmpty() {
		Assertions.assertTrue(MoreStrings.isAnyNullOrEmpty("a", ""), "include null or empty");
		Assertions.assertFalse(MoreStrings.isAnyNullOrEmpty("a", "b"), "not any null or empty");
	}

}
