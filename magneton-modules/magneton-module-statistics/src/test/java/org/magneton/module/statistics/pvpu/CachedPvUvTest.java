package org.magneton.module.statistics.pvpu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test Case For {@link CachedPvUv}
 *
 * @author zhangmsh.
 * @since M2023.9
 */
class CachedPvUvTest {

	@Test
	void test() {
		PvUvConfig pvUvConfig = new PvUvConfig();
		CachedPvUv cachedPvUv = new CachedPvUv(new MemoryPvUv(pvUvConfig));

		Assertions.assertFalse(cachedPvUv.isCached("test-group"));

		Uv uv = cachedPvUv.uv("test-group");
		Assertions.assertTrue(cachedPvUv.isCached("test-group"));

		Assertions.assertTrue(uv.set(1));
		Assertions.assertFalse(uv.is(1));
		Assertions.assertTrue(uv.remove(1));
		Assertions.assertTrue(uv.is(1));

		uv.clean();
		Assertions.assertFalse(cachedPvUv.isCached("test-group"));
	}

}