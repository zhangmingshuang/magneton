package org.magneton.test.injector.base;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;

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
