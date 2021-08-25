package org.magneton.test.injector.collection;

import java.util.Map;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.magneton.test.ChaosTest;
import org.magneton.test.config.Config;
import org.magneton.test.core.InjectType;

/**
 * .
 *
 * @author zhangmsh 2021/8/23
 * @since
 */
class MapInjectorTest {

  private final Config config = new Config();
  private final InjectType angle = InjectType.EXPECTED;

  @ToString
  public static class TestA {
    private Map map;
    private Map<Integer, Long[]> map2;
  }

  @Test
  void testA() {
    this.config.setMinSize(1).setMaxSize(1);
    TestA test = ChaosTest.create(TestA.class, this.config, this.angle);
    System.out.println(test);
    Assertions.assertEquals(1, test.map.size());
    Assertions.assertEquals(1, test.map2.size());
  }
}
