package cn.nascent.framework.test.injector.processor;

import cn.nascent.framework.test.annotation.TestAutowired;
import cn.nascent.framework.test.annotation.TestComponent;
import cn.nascent.framework.test.core.Config;
import cn.nascent.framework.test.injector.Inject;
import cn.nascent.framework.test.injector.InjectType;
import cn.nascent.framework.test.injector.processor.statement.DataStatement;
import cn.nascent.framework.test.util.AnnotationUtils;
import cn.nascent.framework.test.util.ConfigUtil;
import cn.nascent.framework.test.util.NumberUtil;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;

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
