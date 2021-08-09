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
public class ShortInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return (short) ThreadLocalRandom.current().nextInt(config.getMinShort(), config.getMaxShort());
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (short[].class.isAssignableFrom(inject.getInectType())) {
      return new short[length];
    }
    if (Short[].class.isAssignableFrom(inject.getInectType())) {
      return new Short[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {short.class, Short.class, short[].class, Short[].class};
  }
}
