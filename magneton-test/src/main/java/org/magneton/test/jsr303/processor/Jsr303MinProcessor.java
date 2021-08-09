package org.magneton.test.jsr303.processor;

import com.google.common.base.Strings;
import java.lang.annotation.Annotation;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.annotation.TestSort;
import org.magneton.test.core.Config;
import org.magneton.test.injector.Inject;
import org.magneton.test.injector.InjectType;
import org.magneton.test.injector.processor.statement.DataStatement;
import org.magneton.test.util.AnnotationUtils;
import org.magneton.test.util.NumberUtil;

/**
 * 验证注解的元素值大于等于 {@code @Min} 指定的 value 值.
 *
 * <p>验证注解的元素值大于等于 {@code @DecimalMin} 指定的 value 值
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@TestComponent
@TestSort
public class Jsr303MinProcessor extends AbstractJsr303NumberProcessor {

  public static final String METADATA_KEY = "jsr303__Min__temp";

  @Override
  public boolean processable(Class annotationType) {
    return Min.class == annotationType || DecimalMin.class == annotationType;
  }

  @Nullable
  @Override
  public void process(
      Config config,
      InjectType injectType,
      Inject inject,
      Annotation annotation,
      DataStatement dataStatement) {
    Map<String, Object> metadata = AnnotationUtils.getMetadata(annotation);
    if (annotation.annotationType() == Min.class) {
      this.doMinProcess(config, injectType, inject, metadata, dataStatement);
    } else {
      this.doMinDecimalMinProcess(config, injectType, inject, metadata, dataStatement);
    }
  }

  private void doMinDecimalMinProcess(
      Config config,
      InjectType injectType,
      Inject inject,
      Map<String, Object> metadata,
      DataStatement dataStatement) {
    String value = (String) metadata.get("value");
    boolean inclusive = (boolean) metadata.get("inclusive");
    if (Strings.isNullOrEmpty(value)) {
      value = "0";
    }
    Number number = NumberUtil.cast(inject.getInectType(), value);
    this.setValue(config, injectType, inject, dataStatement, number.doubleValue());
  }

  private void doMinProcess(
      Config config,
      InjectType injectType,
      Inject inject,
      Map<String, Object> metadata,
      DataStatement dataStatement) {
    long min = (long) metadata.get("value");
    this.setValue(config, injectType, inject, dataStatement, min);
  }

  private void setValue(
      Config config,
      InjectType injectType,
      Inject inject,
      DataStatement dataStatement,
      double min) {
    if (injectType.isDemon()) {
      dataStatement.breakNext(min - 1);
    } else {
      dataStatement.setValue(min + 1);
    }
  }
}
