package org.magneton.test.validate;

import javax.validation.constraints.AssertFalse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/24
 * @since
 */
class AssertFalseConfigPostProcessorTest {

  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {
    @AssertFalse private boolean bool;
  }

  @Test
  void test() {
    Config coyied = Config.copyOf(this.config);
    coyied.setBooleanTrueProbability(100);
    TestA testA = ChaosTest.create(TestA.class, coyied, this.angle);
    Human.sout(testA);
    Assertions.assertTrue(HibernateValid.valid(testA));
  }
}
