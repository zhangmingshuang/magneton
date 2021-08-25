package org.magneton.test.injector.base;

import javax.annotation.Nullable;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.config.ConfigProcessorFactory;
import org.magneton.test.core.InjectType;
import org.magneton.test.injector.AbstractInjector;
import org.magneton.test.parser.Definition;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class ShortInjector extends AbstractInjector {

  @Override
  public Class[] getTypes() {
    return new Class[] {short.class, Short.class, short[].class, Short[].class};
  }

  @Nullable
  @Override
  protected Object createValue(Definition definition, Config config, InjectType injectType) {
    return ConfigProcessorFactory.of(injectType).nextShort(config, definition);
  }
}
