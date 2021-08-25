package org.magneton.test.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.core.InjectType;
import org.magneton.test.parser.Definition;
import org.magneton.test.util.RandomStringUtils;

/**
 * 反预期的数据生成处理器.
 *
 * <p>反预期处理逻辑：
 *
 * <p>根据Config的取值范围，如果最小值-2不超过对应类型的最小值，则采用该值，
 *
 * <p>如果超过对应类型的最小值（即会得正），则判断类型是否为基础类型
 *
 * <p>如果是基础类型，则表示该类型对应的无法生成反预期的数据，因为所有的范围都是预期的，则生成基本类型的默认值。
 *
 * <p>如果是非基础类型，则返回 {@code null}
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@ConfigWith(InjectType.ANTI_EXPECTED)
public class AntiExceptedConfigProcessor implements ConfigProcessor {

  private static final long YEAR_MILLIS = 365 * 24 * 60 * 60 * 1000L;

  @Override
  public int nextSize(Config config, Definition definition) {
    int minSize = config.getMinSize();
    int maxSize = config.getMaxSize();
    // 如果小于0表示返回null，如果等于0表示返回空数组、字符串、集合
    if (minSize < 1 || maxSize < 1) {
      // 反预期，一定要返回数据
      return 1;
    }
    if (minSize == maxSize) {
      return Math.max(0, minSize - 1);
    }
    if (minSize > 2) {
      return Math.max(0, minSize - 2);
    } else {
      return Math.max(maxSize, maxSize + 2);
    }
  }

  @Override
  public boolean nullable(Config config, Definition definition) {
    int nullableProbability = config.getNullableProbability();
    if (nullableProbability < 0) {
      // 负数则忽略
      return false;
    }
    // 反向概率
    return nullableProbability < ThreadLocalRandom.current().nextInt(100);
  }

  @Override
  public BigDecimal nextBigDecimal(Config config, Definition definition) {
    BigDecimal minBigDecimal = config.getMinBigDecimal();
    BigDecimal maxBigDecimal = config.getMaxBigDecimal();
    BigDecimal bigDecimal;
    if (minBigDecimal == null || maxBigDecimal == null) {
      // 预期是null,则一定要返回数据
      bigDecimal = BigDecimal.ONE;
    } else if (minBigDecimal.compareTo(BigDecimal.valueOf(2)) > 0) {
      // 如果最小值大于2，则返回小于2的数据
      bigDecimal = minBigDecimal.subtract(BigDecimal.valueOf(2));
    } else {
      // 返回预期的早大值+2
      bigDecimal = maxBigDecimal.add(BigDecimal.valueOf(2));
    }
    return bigDecimal.setScale(config.getBigDecimalScale(), config.getBigDecimalRoundingModel());
  }

  @Override
  public BigInteger nextBigInteger(Config config, Definition definition) {
    BigInteger minBigInterger = config.getMinBigInteger();
    BigInteger maxBigInteger = config.getMaxBigInteger();
    if (minBigInterger == null || maxBigInteger == null) {
      // 预期是null,则一定要返回数据
      return BigInteger.ONE;
    }
    if (minBigInterger.compareTo(BigInteger.valueOf(2)) > 0) {
      return minBigInterger.subtract(BigInteger.valueOf(2));
    } else {
      return maxBigInteger.add(BigInteger.valueOf(2));
    }
  }

  @Override
  public Byte nextByte(Config config, Definition definition) {
    Byte minByte = config.getMinByte();
    Byte maxByte = config.getMaxByte();
    if (minByte == null || maxByte == null) {
      // 预期是null,则一定要返回数据
      return Byte.MAX_VALUE / 2;
    }
    if (minByte.intValue() > 2) {
      return (byte) (minByte.intValue() - 2);
    } else {
      return (byte) Math.max(0, maxByte.intValue() + 2);
    }
  }

  @Override
  public Short nextShort(Config config, Definition definition) {
    Short minShort = config.getMinShort();
    Short maxShort = config.getMaxShort();
    if (minShort == null || maxShort == null) {
      return Short.MAX_VALUE / 2;
    }
    if (minShort.intValue() > 2) {
      return (short) (minShort.intValue() - 2);
    } else {
      return (short) (maxShort.intValue() + 2);
    }
  }

  @Override
  public Integer nextInt(Config config, Definition definition) {
    Integer minInt = config.getMinInt();
    Integer maxInt = config.getMaxInt();
    if (minInt == null || maxInt == null) {
      return Integer.MAX_VALUE / 2;
    }
    if (minInt > 2) {
      return minInt - 2;
    } else {
      return maxInt + 2;
    }
  }

  @Override
  public Long nextLong(Config config, Definition definition) {
    Long minLong = config.getMinLong();
    Long maxLong = config.getMaxLong();
    if (minLong == null || maxLong == null) {
      return Long.MAX_VALUE / 2;
    }
    if (minLong > 2) {
      return minLong - 2;
    }
    return maxLong + 2;
  }

  @Override
  public Float nextFloat(Config config, Definition definition) {
    Float minFloat = config.getMinFloat();
    Float maxFloat = config.getMaxFloat();
    double f;
    if (minFloat == null || maxFloat == null) {
      f = Float.MAX_VALUE / 2;
    } else if (minFloat > 2) {
      f = minFloat - 2;
    } else {
      f = maxFloat + 2;
    }
    return BigDecimal.valueOf(f)
        .setScale(config.getFloatScale(), config.getFloatRoundingModel())
        .floatValue();
  }

  @Override
  public Double nextDouble(Config config, Definition definition) {
    Double minDouble = config.getMinDouble();
    Double maxDouble = config.getMaxDouble();
    double d;
    if (minDouble == null || maxDouble == null) {
      d = Double.MAX_VALUE / 2;
    } else if (minDouble > 2) {
      d = minDouble - 2;
    } else {
      d = maxDouble + 2;
    }
    return BigDecimal.valueOf(d)
        .setScale(config.getDoubleScale(), config.getDoubleRoundingModel())
        .doubleValue();
  }

  @Override
  public Boolean nextBoolean(Config config, Definition definition) {
    int booleanTrueProbability = config.getBooleanTrueProbability();
    if (booleanTrueProbability < 1) {
      return Boolean.TRUE;
    }
    if (booleanTrueProbability >= 100) {
      return Boolean.FALSE;
    }
    return config.getBooleanTrueProbability() < ThreadLocalRandom.current().nextInt(100);
  }

  @Override
  public Character nextCharacter(Config config, Definition definition) {
    Character minChar = config.getMinChar();
    Character maxChar = config.getMaxChar();
    if (minChar == null || maxChar == null) {
      return Character.MIN_VALUE;
    }
    if ((int) minChar > 2) {
      return (char) ((int) minChar - 2);
    }
    return (char) ((int) maxChar + 2);
  }

  @Override
  public String nextString(Config config, Definition definition) {
    int minCharSequenceLength = config.getMinCharSequenceLength();
    int maxCharSequenceLength = config.getMaxCharSequenceLength();
    int length;
    if (minCharSequenceLength <= 0 || maxCharSequenceLength <= 0) {
      // 期望为空或null，一定要生成数据
      length = ThreadLocalRandom.current().nextInt(1, 16);
    } else if (minCharSequenceLength == maxCharSequenceLength) {
      length = Math.max(0, minCharSequenceLength - 1);
    } else if (minCharSequenceLength > maxCharSequenceLength) {
      length = Math.max(0, minCharSequenceLength + 1);
    } else {
      length = Math.max(0, maxCharSequenceLength + 1);
    }
    return RandomStringUtils.randomAlphanumeric(length);
  }

  @Override
  public Date nextDate(Config config, Definition definition) {
    Long minDateMillis = config.getMinDateMillis();
    Long maxDateMillis = config.getMaxDateMillis();
    if (minDateMillis == null || maxDateMillis == null) {
      return new Date();
    }
    if (minDateMillis > YEAR_MILLIS) {
      minDateMillis -= YEAR_MILLIS;
    } else {
      maxDateMillis += YEAR_MILLIS;
    }
    if (minDateMillis.longValue() == maxDateMillis.longValue()) {
      return new Date(minDateMillis);
    }
    long millis;
    if (minDateMillis > maxDateMillis) {
      millis = ThreadLocalRandom.current().nextLong(maxDateMillis, minDateMillis);
    } else {
      millis = ThreadLocalRandom.current().nextLong(minDateMillis, maxDateMillis);
    }
    return new Date(millis);
  }
}
