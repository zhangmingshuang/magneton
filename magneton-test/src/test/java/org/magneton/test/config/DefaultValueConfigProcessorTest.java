package org.magneton.test.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
class DefaultValueConfigProcessorTest {

  public static class TestA {
    private int i;
    private Integer integer;
    private TestB testB;
  }

  public static class TestB {
    private long l;
    private TestA testA;
  }

  @Test
  void testA() {
    TestA testA = ChaosTest.createDefaultValue(TestA.class);
    Human.sout(testA);
    Assertions.assertNull(testA.testB.testA.integer);
  }
}
