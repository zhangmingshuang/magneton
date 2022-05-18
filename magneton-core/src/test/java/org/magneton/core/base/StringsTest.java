package org.magneton.core.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author zhangmsh 2022/3/27
 * @since 1.0.0
 */
class StringsTest {

	@Test
	void format() {
		String format = Strings.format("a %1$s，%2$s，%2$s", "a", "b");
		Assertions.assertEquals("a a，b，b", format);
	}

	@Test
	void indexFormat() {
		String format = Strings.indexFormat("{0} = {1} = {0}", 1, 2, 3);
		Assertions.assertEquals("1 = 2 = 1", format);
	}

}