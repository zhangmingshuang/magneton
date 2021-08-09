package cn.nascent.framework.test.injector;

import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.processor.AnnotationProcessorFactory;
import javax.annotation.Nullable;

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
