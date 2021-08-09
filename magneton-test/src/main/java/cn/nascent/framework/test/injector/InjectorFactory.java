package cn.nascent.framework.test.injector;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.AfterAutowrited;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.processor.AnnotationProcessorFactory;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
@TestComponent
public class InjectorFactory implements AfterAutowrited {

  private final Map<Class, Injector> classInjectors = Maps.newLinkedHashMap();

  @TestAutowired private List<Injector> injectors;

  @TestAutowired private AnnotationProcessorFactory annotationProcessorFactory;

  @Nullable
  public <T> T inject(Config config, Inject<T> inject, InjectType injectType) {
    Injector injector = this.getInjector(inject.getInectType());
    Verify.verify(injector != null, "%s not injector found", inject.getName());
    return (T) injector.inject(config, injectType, inject);
  }

  public <T> T injectRequired(Config config, Inject inject, InjectType injectType) {
    Injector injector = this.getInjector(inject.getInectType());
    Verify.verify(injector != null, "%s not injector found", inject.getName());
    return (T) injector.injectRequired(config, injectType, inject);
  }

  @Override
  public void afterAutowrited() {
    for (Injector injector : this.injectors) {
      Class[] types = injector.getTypes();
      if (types != null && types.length > 0) {
        for (Class type : types) {
          this.classInjectors.put(type, injector);
        }
      }
      injector.setProcessorFactory(this.annotationProcessorFactory);
    }
  }

  private Injector getInjector(Class clazz) {
    Injector injector = this.classInjectors.get(clazz);
    if (injector != null) {
      return injector;
    }
    Set<Entry<Class, Injector>> entries = this.classInjectors.entrySet();
    for (Entry<Class, Injector> entry : entries) {
      if (entry.getKey().isAssignableFrom(clazz)) {
        return entry.getValue();
      }
    }
    return this.classInjectors.get(Object.class);
  }
}
