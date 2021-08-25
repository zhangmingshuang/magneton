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
class StringInjectorTest {
  @Test
  void test() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinSize(12).setMaxSize(12);
    String integer = ChaosTest.create(String.class, config, type);
    Assertions.assertEquals(12, integer.toString().length());
  }

  @Test
  void testArray() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinSize(1).setMaxSize(1);
    String[] ints = ChaosTest.create(String[].class, config, type);
    Assertions.assertEquals(1, ints.length);
  }
}
