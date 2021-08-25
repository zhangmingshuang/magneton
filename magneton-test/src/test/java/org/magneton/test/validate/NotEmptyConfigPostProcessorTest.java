package org.magneton.test.validate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
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
 * @see NotEmptyConfigPostProcessor
 */
class NotEmptyConfigPostProcessorTest {
  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  public static class TestA {
    @NotEmpty private String str;
    @NotEmpty private CharSequence cs;
    @NotEmpty private List list;
    @NotEmpty private Map map;
    @NotEmpty private Set set;
    @NotEmpty private int[] array;
  }

  @RepeatedTest(10)
  void testA() {
    TestA testA = ChaosTest.create(TestA.class, this.config, this.angle);
    Human.sout(testA);
    Assertions.assertTrue(HibernateValid.valid(testA));
    ;
  }
}
