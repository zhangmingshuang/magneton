package org.magneton.test.validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.Min;
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
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 * @see MinConfigPostProcessor
 */
class MinConfigPostProcessorTest {

  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {
    @Min(10)
    private byte b;

    @Min(120)
    private short s;

    @Min(101)
    private int i;

    @Min(1011)
    private long l;

    @Min(100)
    private BigDecimal bigDecimal;

    @Min(1000)
    private BigInteger bigInteger;
  }

  @RepeatedTest(10)
  void testA() {
    TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
    Human.sout(testA);
    Assertions.assertTrue(HibernateValid.valid(testA));
  }
}
