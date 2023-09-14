package org.magneton.module.safedog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.module.safedog.access.*;

/**
 * Biz test for {@link AccessorFactoryBuilder}
 *
 * @author zhangmsh.
 * @since 2023.0
 */
class AccessorFactoryBuilderTest {

	@Test
	void test() {
		int numberOfWrongs = 5;
		AccessorFactory accessorFactory = AccessorFactoryBuilder.newBuilder().numberOfWrongs(numberOfWrongs)
				.wrongTimeToForget(2000).lockTime(2000).timeCalculator(new DefaultAccessTimeCalculator())
				.accessorContainer(new CachedAccessorContainer()).accessorFactory(new MemoryAccessorProcessor())
				.build();
		Accessor accessor = accessorFactory.get("AccessorFactoryBuilderTest");

		if (accessor.locked()) {
			throw new RuntimeException("should unlocked");
		}

		int remain = accessor.onError();
		Assertions.assertEquals(numberOfWrongs - 1, remain);

		for (int i = 0; i < numberOfWrongs; i++) {
			accessor.onError();
		}

		Assertions.assertTrue(accessor.locked());
	}

	@Test
	void testDefault() {
		AccessorFactory accessorFactory = AccessorFactoryBuilder.newBuilder().build();

		Accessor accessor = accessorFactory.get("AccessorFactoryBuilderTest");

		Assertions.assertFalse(accessor.locked());

		Assertions.assertDoesNotThrow(accessor::onError);
	}

}