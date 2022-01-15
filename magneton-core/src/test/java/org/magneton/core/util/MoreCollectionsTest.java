package org.magneton.core.util;

import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.core.collect.MoreCollections;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/30
 */
class MoreCollectionsTest {

	@Test
	void isNullOrEmpty() {
		Assertions.assertTrue(MoreCollections.isNullOrEmpty(Collections.emptyList()), "empty list");
		Assertions.assertTrue(MoreCollections.isNullOrEmpty(Collections.emptyMap()), "empty map");
		Assertions.assertFalse(MoreCollections.isNullOrEmpty(Collections.singletonList("1")), "not empty");
	}

}
