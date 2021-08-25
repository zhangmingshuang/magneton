package org.magneton.test.validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.Negative;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.magneton.test.ChaosTest;
import org.magneton.test.HibernateValid;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;
import org.magneton.test.helper.Human;

/**
 * .
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 * @see NegativeConfigPostProcessor
 */
class NegativeConfigPostProcessorTest {
  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {
    @Negative private byte b;

    @Negative private short s;

    @Negative private int i;

    @Negative private long l;

    @Negative private BigDecimal bigDecimal;

    @Negative private BigInteger bigInteger;
  }

  @RepeatedTest(10)
  void testA() {
    TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
    Human.sout(testA);
    Assertions.assertTrue(HibernateValid.valid(testA));
  }
}
