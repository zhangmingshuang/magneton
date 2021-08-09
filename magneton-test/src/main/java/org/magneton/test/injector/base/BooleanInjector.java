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
 * @since
 */
@TestComponent
public class BooleanInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return ThreadLocalRandom.current().nextBoolean();
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (boolean[].class.isAssignableFrom(inject.getInectType())) {
      return new boolean[length];
    }
    if (Boolean[].class.isAssignableFrom(inject.getInectType())) {
      return new Boolean[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {boolean.class, Boolean.class, boolean[].class, Boolean[].class};
  }
}
