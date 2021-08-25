package org.magneton.test.validate;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.AnnotationUtil;

/**
 * 验证注解的元素值小于等于 {@link DecimalMax} 指定的 value 值.
 *
 * <p>验证注解的元素值小于等于 {@link Max} 指定的 value 值
 *
 * @author zhangmsh 2021/8/24
 * @since 2.0.0
 */
@TestComponent
public class MaxConfigPostProcessor extends AbstractConfigPostProcessor {

  @Override
  protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
    Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
    long max = (long) metadata.get("value");

    this.setByte(config, max);
    this.setShort(config, max);
    this.setInt(config, max);
    this.setLong(config, max);
    this.setBigDecimal(config, max);
    this.setBigInteger(config, max);
  }

  private void setBigInteger(Config config, long max) {
    BigInteger minBigInterger = config.getMinBigInteger();
    if (minBigInterger == null || minBigInterger.longValue() > max) {
      config.setMinBigInteger(BigInteger.valueOf(max));
    }
    BigInteger maxBigInteger = config.getMaxBigInteger();
    if (maxBigInteger == null || maxBigInteger.longValue() > max) {
      config.setMaxBigInteger(BigInteger.valueOf(max));
    }
  }

  private void setBigDecimal(Config config, long max) {
    BigDecimal minBigDecimal = config.getMinBigDecimal();
    if (minBigDecimal == null || minBigDecimal.compareTo(BigDecimal.valueOf(max)) > 0) {
      config.setMinBigDecimal(BigDecimal.valueOf(max));
    }
    BigDecimal maxBigDecimal = config.getMaxBigDecimal();
    if (maxBigDecimal == null || maxBigDecimal.compareTo(BigDecimal.valueOf(max)) > 0) {
      config.setMaxBigDecimal(BigDecimal.valueOf(max));
    }
  }

  private void setLong(Config config, long max) {
    Long minLong = config.getMinLong();
    if (minLong == null || minLong > max) {
      config.setMinLong(max);
    }
    Long maxLong = config.getMaxLong();
    if (maxLong == null || maxLong > max) {
      config.setMaxLong(max);
    }
  }

  private void setInt(Config config, long max) {
    Integer minInt = config.getMinInt();
    if (minInt == null || minInt > max) {
      config.setMinInt((int) max);
    }
    Integer maxInt = config.getMaxInt();
    if (maxInt == null || maxInt > max) {
      config.setMaxInt((int) max);
    }
  }

  private void setShort(Config config, long max) {
    Short minShort = config.getMinShort();
    if (minShort == null || minShort.intValue() > max) {
      config.setMinShort((short) max);
    }
    Short maxShort = config.getMaxShort();
    if (maxShort == null || maxShort.intValue() > max) {
      config.setMaxShort((short) max);
    }
  }

  private void setByte(Config config, long max) {
    Byte minByte = config.getMinByte();
    if (minByte == null || minByte.intValue() > max) {
      config.setMinByte((byte) max);
    }
    Byte maxByte = config.getMaxByte();
    if (maxByte == null || maxByte.intValue() > max) {
      config.setMaxByte((byte) max);
    }
  }

  @Nullable
  @Override
  protected Class[] jsrAnnotations() {
    return new Class[] {Max.class, DecimalMax.class};
  }
}
