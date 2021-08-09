package org.magneton.test.injector.processor;

import java.lang.annotation.Annotation;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.statement.DataStatement;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public interface AnnotationProcessor {

  void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement);

  boolean processable(Class annotationType);
}
