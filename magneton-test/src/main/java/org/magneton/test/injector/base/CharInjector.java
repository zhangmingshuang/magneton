package org.magneton.test.injector.base;

import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.exception.NoSupportTypeCreateException;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.util.ConfigUtil;
import org.magneton.test.util.RandomStringUtils;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@TestComponent
public class CharInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    int length = ConfigUtil.getRandomSize(config);
    return RandomStringUtils.randomAlphanumeric(length)
        .charAt(ThreadLocalRandom.current().nextInt(length));
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (char[].class.isAssignableFrom(inject.getInectType())) {
      return new char[length];
    }
    if (Character[].class.isAssignableFrom(inject.getInectType())) {
      return new Character[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {char.class, Character.class, char[].class, Character[].class};
  }
}
