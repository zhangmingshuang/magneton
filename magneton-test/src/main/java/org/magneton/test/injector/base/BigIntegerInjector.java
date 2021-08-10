package org.magneton.test.injector.base;

import java.math.BigInteger;
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
public class BigIntegerInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return ConfigUtil.nextBigInteger(config);
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    return new BigInteger[length];
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {BigInteger.class, BigInteger[].class};
  }
}
