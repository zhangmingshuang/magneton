package org.magneton.test.injector.processor;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.statement.DataStatement;
import org.magneton.test.util.AnnotationUtils;
import org.magneton.test.util.ConfigUtil;
import org.magneton.test.util.NumberUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since
 */
@TestComponent
public class AnnotationProcessorFactory {

  @TestAutowired private List<AnnotationProcessor> processors;

  @Nullable
  public Object process(
      Config config,
      InjectType injectType,
      Inject inject,
      boolean required,
      Supplier<Object> supplier) {
    Object object = inject.getObject();
    if (object == null) {
      return this.createIfNeed(config, injectType, inject, required, supplier);
    }
    List<Annotation> annotations = AnnotationUtils.findAnnotations(object);
    if (annotations.isEmpty()) {
      return this.createIfNeed(config, injectType, inject, required, supplier);
    }
    AnnotationProcessor processor = null;
    try {
      Object value = supplier.get();
      DataStatement dataStatement = new DataStatement(inject, value);
      for (int i = 0, l = this.processors.size(); i < l; i++) {
        processor = this.processors.get(i);
        for (Annotation annotation : annotations) {
          Class annotationType = annotation.annotationType();
          if (processor.processable(annotationType)) {
            processor.process(config, injectType, inject, annotation, dataStatement);
            if (!dataStatement.isNext()) {
              break;
            }
          }
        }
      }
      return dataStatement.getValue();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nullable
  private Object createIfNeed(
      Config config,
      InjectType injectType,
      Inject inject,
      boolean requried,
      Supplier<Object> supplier) {
    if (requried || !injectType.isDemon()) {
      return supplier.get();
    }
    if (ConfigUtil.isNullable(config)) {
      return inject.isPrimitive() ? NumberUtil.minValue(inject.getInectType()) : null;
    }
    return supplier.get();
  }
}
