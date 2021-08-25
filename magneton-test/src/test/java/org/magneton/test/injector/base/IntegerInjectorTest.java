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
 * @since 2.0.0
 * @see IntegerInjector
 */
class IntegerInjectorTest {

  @Test
  void test() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinInt(1).setMaxInt(1);
    Integer integer = ChaosTest.create(int.class, config, type);
    Assertions.assertEquals(1, integer);
    integer = ChaosTest.create(Integer.class, config, type);
    Assertions.assertEquals(1, integer);
  }

  @Test
  void testArray() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinSize(1).setMaxSize(1).setMinInt(1).setMaxInt(1);
    int[] ints = ChaosTest.create(int[].class, config, type);
    Assertions.assertEquals(1, ints.length);
    Assertions.assertEquals(1, ints[0]);
  }
}
