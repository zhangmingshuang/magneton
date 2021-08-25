package org.magneton.test.validate;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import javax.annotation.Nullable;
import javax.validation.constraints.Digits;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.config.Config;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.AnnotationUtil;

/**
 * {@link Digits }整数部分位数不超过integer,小数部分位数不超过fraction.
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
@TestComponent
public class DigitsConfigPostProcessor extends AbstractConfigPostProcessor {

  @Override
  protected void doPostProcessor(Annotation annotation, Config config, Definition definition) {
    Map<String, Object> metadata = AnnotationUtil.getMetadata(annotation);
    // 整数部分
    int integer = (int) metadata.get("integer");
    // 小数部分
    int fraction = (int) metadata.get("fraction");

    int minValue = integer * -9;
    int maxValue = integer * 10;

    this.setByte(config, minValue, maxValue);
    this.setShort(config, minValue, maxValue);
    this.setInt(config, minValue, maxValue);
    this.setLong(config, minValue, maxValue);
    this.setChar(config, minValue, maxValue);
    this.setFloat(config, minValue, maxValue);
    this.setDouble(config, minValue, maxValue);
    this.setBigDecimal(config, minValue, maxValue);
    this.setBigInteger(config, minValue, maxValue);

    config.setFloatScale(fraction).setDoubleScale(fraction).setBigDecimalScale(fraction);
  }

  private void setBigInteger(Config config, int minValue, int maxValue) {
    BigInteger minBigInterger = config.getMinBigInteger();
    if (minBigInterger == null
        || minBigInterger.intValue() < minValue
        || minBigInterger.intValue() > maxValue) {
      minBigInterger = BigInteger.valueOf(minValue);
    }
    BigInteger maxBigInteger = config.getMaxBigInteger();
    if (maxBigInteger == null || maxBigInteger.intValue() > maxValue) {
      maxBigInteger = BigInteger.valueOf(maxValue);
    }
    config.setMinBigInteger(minBigInterger).setMaxBigInteger(maxBigInteger);
  }

  private void setBigDecimal(Config config, int minValue, int maxValue) {
    BigDecimal minBigDecimal = config.getMinBigDecimal();
    if (minBigDecimal == null
        || minBigDecimal.intValue() < minValue
        || minBigDecimal.intValue() > maxValue) {
      minBigDecimal = BigDecimal.valueOf(minValue);
    }
    BigDecimal maxBigDecimal = config.getMaxBigDecimal();
    if (maxBigDecimal == null || maxBigDecimal.intValue() > maxValue) {
      maxBigDecimal = BigDecimal.valueOf(maxValue);
    }
    config.setMinBigDecimal(minBigDecimal).setMaxBigDecimal(maxBigDecimal);
  }

  private void setDouble(Config config, int minValue, int maxValue) {
    Double minDouble = config.getMinDouble();
    if (minDouble == null || minDouble.intValue() < minValue || minDouble.intValue() > maxValue) {
      minDouble = minValue * 1.0D;
    }
    Double maxDouble = config.getMaxDouble();
    if (maxDouble == null || maxDouble.intValue() > maxValue) {
      maxDouble = maxValue * 1.0D;
    }
    config.setMinDouble(minDouble).setMaxDouble(maxDouble);
  }

  private void setFloat(Config config, int minValue, int maxValue) {
    Float minFloat = config.getMinFloat();
    if (minFloat == null || minFloat.intValue() < minValue || minFloat.intValue() > maxValue) {
      minFloat = minValue * 1.0F;
    }
    Float maxFloat = config.getMaxFloat();
    if (maxFloat == null || maxFloat.intValue() > maxValue) {
      maxFloat = maxValue * 1.0F;
    }
    config.setMinFloat(minFloat).setMaxFloat(maxFloat);
  }

  private void setChar(Config config, int minValue, int maxValue) {
    Character minChar = config.getMinChar();
    if (minChar == null || (int) minChar < minValue || (int) minChar > maxValue) {
      minChar = (char) minValue;
    }
    Character maxChar = config.getMaxChar();
    if (maxChar == null || (int) maxChar > maxValue) {
      maxChar = ((char) maxValue);
    }
    config.setMinChar(minChar).setMaxChar(maxChar);
  }

  private void setLong(Config config, int minValue, int maxValue) {
    Long minLong = config.getMinLong();
    if (minLong == null || minLong < minValue || minLong > maxValue) {
      minLong = (long) minValue;
    }
    Long maxLong = config.getMaxLong();
    if (maxLong == null || maxLong > maxValue) {
      maxLong = (long) maxValue;
    }
    config.setMinLong(minLong).setMaxLong(maxLong);
  }

  private void setInt(Config config, int minValue, int maxValue) {
    Integer minInt = config.getMinInt();
    if (minInt == null || minInt < minValue || minInt > maxValue) {
      minInt = minValue;
    }
    Integer maxInt = config.getMaxInt();
    if (maxInt == null || maxInt > maxValue) {
      maxInt = maxValue;
    }
    config.setMinInt(minInt).setMaxInt(maxInt);
  }

  private void setShort(Config config, int minValue, int maxValue) {
    Short minShort = config.getMinShort();
    if (minShort == null || minShort.intValue() < minValue || minShort.intValue() > maxValue) {
      minShort = (short) minValue;
    }
    Short maxShort = config.getMaxShort();
    if (maxShort == null || maxShort.intValue() > maxValue) {
      maxShort = (short) maxValue;
    }
    config.setMinShort(minShort).setMaxShort(maxShort);
  }

  private void setByte(Config config, int minValue, int maxValue) {
    Byte minByte = config.getMinByte();
    if (minByte == null || minByte.intValue() < minValue || minByte.intValue() > maxValue) {
      minByte = (byte) minValue;
    }
    Byte maxByte = config.getMaxByte();
    if (maxByte == null || maxByte.intValue() > maxValue) {
      maxByte = (byte) maxValue;
    }
    config.setMinByte(minByte).setMaxByte(maxByte);
  }

  @Nullable
  @Override
  protected Class[] jsrAnnotations() {
    return new Class[] {Digits.class};
  }
}
