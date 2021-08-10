package org.magneton.test.injector.base;

import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.exception.NoSupportTypeCreateException;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.util.ConfigUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class LongInjector extends AbstractInjector {

  @Override
  public Class[] getTypes() {
    return new Class[] {long.class, Long.class, long[].class, Long[].class};
  }

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return ConfigUtil.nextLong(config);
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (long[].class.isAssignableFrom(inject.getInectType())) {
      return new long[length];
    }
    if (Long[].class.isAssignableFrom(inject.getInectType())) {
      return new Long[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }
}
