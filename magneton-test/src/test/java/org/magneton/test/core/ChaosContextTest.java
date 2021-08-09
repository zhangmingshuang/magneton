package org.magneton.test.core;

import org.junit.jupiter.api.Test;
import org.magneton.test.annotation.TestComponentScan;
import org.magneton.test.injector.InjectorFactory;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@TestComponentScan("cn.nascent.framework.test")
class ChaosContextTest implements ChaosApplication {

  @Test
  void test() {
    ChaosContext.init(ChaosContextTest.class);
    InjectorFactory injectorFactory = ChaosContext.get(InjectorFactory.class);
    System.out.println(injectorFactory);
  }
}
