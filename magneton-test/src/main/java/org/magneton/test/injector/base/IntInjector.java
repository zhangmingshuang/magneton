package org.magneton.test.injector.base;

import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.exception.NoSupportTypeCreateException;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class IntInjector extends AbstractInjector {

  @Override
  public Class[] getTypes() {
    return new Class[] {int.class, Integer.class, int[].class, Integer[].class};
  }

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return ThreadLocalRandom.current().nextInt(config.getMinInt(), config.getMaxInt());
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (int[].class.isAssignableFrom(inject.getInectType())) {
      return new int[length];
    }
    if (Integer[].class.isAssignableFrom(inject.getInectType())) {
      return new Integer[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }
}
