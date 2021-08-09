package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;
import org.magneton.test.util.NumberUtil;

/**
 * {@code @Positive} 验证注解的元素必须是正数
 *
 * <p>{@code @PositiveOrZero} 验证注解的元素必须是正数或 0
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
@TestComponent
public class Jsr3030PositiveProcessor implements AnnotationProcessor {

  @Override
  public boolean processable(Class annotationType) {
    return Positive.class == annotationType || PositiveOrZero.class == annotationType;
  }

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    if (injectType.isDemon()) {
      dataStatement.breakNext(NumberUtil.cast(inject.getInectType(), -1));
      return;
    }
    dataStatement.setValue(NumberUtil.cast(inject.getInectType(), 1));
  }
}
