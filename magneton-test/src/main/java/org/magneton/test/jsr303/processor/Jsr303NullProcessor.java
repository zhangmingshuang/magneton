package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
import javax.validation.constraints.Null;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.InjectorFactory;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;

/**
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
public class Jsr303NullProcessor implements AnnotationProcessor {

  @TestAutowired private InjectorFactory injectorFactory;

  @Override
  public boolean processable(Class annotationType) {
    return Null.class == annotationType;
  }

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    if (injectType.isDemon()) {
      Object value =
          this.injectorFactory.injectRequired(config, Inject.of(inject.getInectType()), injectType);
      dataStatement.breakNext(value);
      return;
    }
    dataStatement.setValue(null);
  }
}
