package org.magneton.test.validate;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;
import org.hibernate.validator.constraints.Range;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;

/**
 * {@link org.hibernate.validator.constraints.Range}
 *
 * <p>{@link javax.validation.constraints.Min}与{@link javax.validation.constraints.Max}的组合
 *
 * @author zhangmsh 2021/8/25
 * @since 2.0.0
 */
@TestComponent
public class RangeConfigPostProcessor extends AbstractConfigPostProcessor {

  @TestAutowired private MaxConfigPostProcessor maxConfigPostProcessor;
  @TestAutowired private MinConfigPostProcessor minConfigPostProcessor;

  @Override
  protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
    this.minConfigPostProcessor.doPostProcessor(annotation, config, definition);
    this.maxConfigPostProcessor.doPostProcessor(annotation, config, definition);
  }

  @Nullable
  @Override
  protected Class[] jsrAnnotations() {
    return new Class[] {Range.class};
  }
}
