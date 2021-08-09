package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
import java.util.Optional;
import javax.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.Config;
import org.magneton.test.core.TraceChain;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;

/**
 * .
 *
 * @author zhangmsh 2021/8/5
 * @since 2.0.0
 */
@TestComponent
@Slf4j
public class Jsr303PatternProcessor implements AnnotationProcessor {

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    String className = TraceChain.current().getRoot().getName();
    log.warn(
        "框架不支持@Pattern的反向数据生成{}#{}", className, Optional.ofNullable(inject.getObject()).orElse(""));
  }

  @Override
  public boolean processable(Class annotationType) {
    return Pattern.class == annotationType;
  }
}
