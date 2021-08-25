package org.magneton.test.validate;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.validation.constraints.Digits;
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
 * @see DigitsConfigPostProcessor
 */
class DigitsConfigPostProcessorTest {

  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {

    /** 位数不得超过1，即-9~9 */
    @Digits(integer = 1, fraction = 1)
    private byte b;
    /** 位数不得超过1，即-9~9 */
    @Digits(integer = 1, fraction = 1)
    private Byte aByte;
    /** 位数不得超过2，即-99~99 */
    @Digits(integer = 2, fraction = 1)
    private short s;
    /** 位数不得超过3，即-999~999 */
    @Digits(integer = 3, fraction = 1)
    private Short aShort;

    @Digits(integer = 1, fraction = 1)
    private int i;

    @Digits(integer = 1, fraction = 1)
    private Integer integer;

    @Digits(integer = 1, fraction = 1)
    private long l;

    @Digits(integer = 1, fraction = 1)
    private Long aLong;

    @Digits(integer = 1, fraction = 2)
    private float f;

    @Digits(integer = 1, fraction = 2)
    private Float aFloat;

    @Digits(integer = 1, fraction = 2)
    private double d;

    @Digits(integer = 1, fraction = 2)
    private Double aDouble;

    @Digits(integer = 1, fraction = 2)
    private BigDecimal bigDecimal;

    @Digits(integer = 1, fraction = 2)
    private BigInteger bigInteger;
  }

  @SuppressWarnings("OverlyComplexMethod")
  @RepeatedTest(10)
  void testA() {
    Config copied = Config.copyOf(this.config);
    copied.setMinByte((byte) 100).setMaxByte((byte) 100);
    copied.setMinShort((short) 100).setMaxShort((short) 9999);
    TestA testA = ChaosTest.create(TestA.class, copied, this.angle);
    Human.sout(testA);

    Assertions.assertTrue(HibernateValid.valid(testA));
  }
}
