package org.magneton.test.validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.Positive;
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
 * @see PositiveConfigPostProcessor
 */
class PositiveConfigPostProcessorTest {
  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {
    @Positive private byte b;

    @Positive private short s;

    @Positive private int i;

    @Positive private long l;

    @Positive private BigDecimal bigDecimal;

    @Positive private BigInteger bigInteger;
  }

  @RepeatedTest(10)
  void testA() {
    NegativeConfigPostProcessorTest.TestA testA =
        ChaosTest.create(NegativeConfigPostProcessorTest.TestA.class, this.config, this.angle);
    Human.sout(testA);
    Assertions.assertTrue(HibernateValid.valid(testA));
  }
}
