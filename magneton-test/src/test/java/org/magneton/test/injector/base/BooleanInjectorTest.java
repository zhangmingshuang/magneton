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
 * @see BooleanInjector
 */
class BooleanInjectorTest {

  @Test
  void test() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setBooleanTrueProbability(100);
    Boolean integer = ChaosTest.create(boolean.class, config, type);
    Assertions.assertTrue(integer);
    integer = ChaosTest.create(Boolean.class, config, type);
    Assertions.assertTrue(integer);
  }

  @Test
  void testArray() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinSize(1).setMaxSize(1).setBooleanTrueProbability(100);
    boolean[] ints = ChaosTest.create(boolean[].class, config, type);
    Assertions.assertEquals(1, ints.length);
    Assertions.assertTrue(ints[0]);
  }
}
