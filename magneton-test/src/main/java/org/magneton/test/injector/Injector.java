package org.magneton.test.injector;

import javax.annotation.Nullable;
import org.magneton.test.core.Config;
import org.magneton.test.injector.processor.AnnotationProcessorFactory;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
public interface Injector {

  void setProcessorFactory(AnnotationProcessorFactory annotationProcessorFactory);

  Class[] getTypes();

  @Nullable
  Object inject(Config config, InjectType injectType, Inject inject);

  Object injectRequired(Config config, InjectType injectType, Inject inject);
}
