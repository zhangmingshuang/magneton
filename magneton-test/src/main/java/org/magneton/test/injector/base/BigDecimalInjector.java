package org.magneton.test.injector.base;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;

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
    double value =
        ThreadLocalRandom.current()
            .nextDouble(
                config.getMinBigDecimal().doubleValue(), config.getMaxBigDecimal().doubleValue());
    return new BigDecimal(value)
        .setScale(config.getBigDecimalScale(), config.getBigDecimalRoundingModel());
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
