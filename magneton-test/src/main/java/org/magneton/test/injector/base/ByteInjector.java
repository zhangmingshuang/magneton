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
public class ByteInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return (byte) ThreadLocalRandom.current().nextInt(config.getMinByte(), config.getMaxByte());
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (byte[].class.isAssignableFrom(inject.getInectType())) {
      return new byte[length];
    }
    if (Byte[].class.isAssignableFrom(inject.getInectType())) {
      return new Byte[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {byte.class, Byte.class, byte[].class, Byte[].class};
  }
}
