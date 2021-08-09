package org.magneton.test.jsr303.processor;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.validation.constraints.Digits;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.annotation.TestSort;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.AnnotationProcessor;
import org.magneton.test.injector.processor.statement.DataStatement;
import org.magneton.test.util.AnnotationUtils;
import org.magneton.test.util.NumberUtil;

/**
 * {@code @Digits(integer,fraction) }整数部分位数不超过integer,小数部分位数不超过fraction.
 *
 * @author zhangmsh 2021/8/4
 * @since 2.0.0
 */
@TestComponent
@TestSort(TestSort.LOWEST_PRECEDENCE - 1)
public class Jsr303DigitsProcessor implements AnnotationProcessor {

  @Override
  public boolean processable(Class annotationType) {
    return Digits.class == annotationType;
  }

  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
    // 整数部分
    int integer = (int) metadata.get("integer");
    // 小数部分
    int fraction = (int) metadata.get("fraction");
    if (injectType.isDemon()) {
      // 不能超过，相反就是超过
      integer++;
      dataStatement.breakNext(NumberUtil.cast(inject.getInectType(), integer * 10));
      return;
    }
    int legitimateInteger = ThreadLocalRandom.current().nextInt(integer * 10 - 1);
    int legitimateFraction = ThreadLocalRandom.current().nextInt(fraction * 10 - 1);
    dataStatement.setValue(
        NumberUtil.cast(inject.getInectType(), legitimateInteger + "." + legitimateFraction));
  }
}
