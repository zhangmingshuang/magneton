package org.magneton.test.validate;

import javax.validation.constraints.Null;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 * @see NullConfigPostProcessor
 */
class NullConfigPostProcessorTest {

  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {
    private int i;
    @Null private Integer integer;
    @Null private TestB testB;
  }

  public static class TestB {
    private Long aLong;
  }

  @Test
  void testA() {
    TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
    Human.sout(testA);
    Assertions.assertTrue(HibernateValid.valid(testA));
  }
}
