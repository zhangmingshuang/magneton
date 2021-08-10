package org.magneton.test.injector.base;

import java.math.BigDecimal;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.util.ConfigUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
public class BigDecimalInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return ConfigUtil.nextBigDecimal(config);
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    return new BigDecimal[length];
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {BigDecimal.class, BigDecimal[].class};
  }
}
