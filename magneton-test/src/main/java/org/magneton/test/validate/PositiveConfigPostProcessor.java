package org.magneton.test.validate;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

/**
 * {@code @Positive} 验证注解的元素必须是正数
 *
 * <p>{@code @PositiveOrZero} 验证注解的元素必须是正数或 0
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class PositiveConfigPostProcessor extends AbstractConfigPostProcessor {

  @Override
  protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
    config.setAllNumberMinValue(0);
  }

  @Nullable
  @Override
  protected Class[] jsrAnnotations() {
    return new Class[] {Positive.class, PositiveOrZero.class};
  }
}
