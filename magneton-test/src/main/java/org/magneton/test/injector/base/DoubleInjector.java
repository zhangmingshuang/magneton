package org.magneton.test.injector.base;

import java.math.BigDecimal;
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
public class DoubleInjector extends AbstractInjector {

  @Override
  protected Object createValue(Config config, InjectType injectType, Inject inject) {
    return BigDecimal.valueOf(
            ThreadLocalRandom.current().nextDouble(config.getMinDouble(), config.getMaxDouble()))
        .setScale(config.getDoubleScale(), config.getDoubleRoundingModel())
        .doubleValue();
  }

  @Override
  protected Object createArray(
      Config config, InjectType injectType, Inject inject, Integer length) {
    if (double[].class.isAssignableFrom(inject.getInectType())) {
      return new double[length];
    }
    if (Double[].class.isAssignableFrom(inject.getInectType())) {
      return new Double[length];
    }
    throw new NoSupportTypeCreateException(inject.getName());
  }

  @Override
  public Class[] getTypes() {
    return new Class[] {double.class, Double.class, double[].class, Double[].class};
  }
}
