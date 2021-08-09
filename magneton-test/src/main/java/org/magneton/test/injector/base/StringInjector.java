package org.magneton.test.injector.base;

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
 * @since 2.0.0
 */
@TestComponent
public class StringInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return RandomStringUtils.randomAlphanumeric(ConfigUtil.getRandomSize(config));
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (String[].class.isAssignableFrom(inject.getInectType())) {
      return new String[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {String.class, String[].class, CharSequence.class};
  }
}
