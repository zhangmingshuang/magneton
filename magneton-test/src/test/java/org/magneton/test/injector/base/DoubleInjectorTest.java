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
class DoubleInjectorTest {

  @Test
  void test() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinDouble(1D).setMaxDouble(1D);
    Double integer = ChaosTest.create(double.class, config, type);
    Assertions.assertEquals(1, integer);
    integer = ChaosTest.create(Double.class, config, type);
    Assertions.assertEquals(1, integer);
  }

  @Test
  void testArray() {
    InjectType type = InjectType.EXPECTED;
    Config config = new Config();

    config.setMinSize(1).setMaxSize(1).setMinDouble(1.0D).setMaxDouble(1.0D);
    double[] ints = ChaosTest.create(double[].class, config, type);
    Assertions.assertEquals(1, ints.length);
    Assertions.assertEquals(1, ints[0]);
  }
}
