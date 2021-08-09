package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.validation.constraints.Size;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.InjectorFactory;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;
import org.magneton.test.util.AnnotationUtils;

/**
 * .
 *
 * @author zhangmsh 2021/8/6
 * @since 2.0.0
 */
@TestComponent
public class Jsr303SizeProcessor implements AnnotationProcessor {

  @TestAutowired private InjectorFactory injectorFactory;

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    if (injectType.isDemon()) {
      dataStatement.breakNext(null);
      return;
    }
    Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
    int min = (int) metadata.get("min");
    int max = (int) metadata.get("max");
    config.setMinSize(min);
    config.setMaxSize(max);
    dataStatement.setValue(
        this.injectorFactory.inject(config, Inject.of(inject.getInectType()), injectType));
  }

  @Override
  public boolean processable(Class annotationType) {
    return Size.class == annotationType;
  }
}
