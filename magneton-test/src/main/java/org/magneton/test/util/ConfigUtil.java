package org.magneton.test.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import org.magneton.test.core.Config;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public class ConfigUtil {

  private ConfigUtil() {}

  public static int getRandomSize(Config config) {
    if (config.getMinSize() == config.getMinSize()) {
      return config.getMinSize();
    }
    return ThreadLocalRandom.current().nextInt(config.getMinSize(), config.getMaxSize());
  }

  @Nullable
  public static boolean isNullable(Config config) {
    int demoNullProbability = config.getDemonNullProbability();
    return ThreadLocalRandom.current().nextInt(100) < demoNullProbability;
  }

  public static BigDecimal nextBigDecimal(Config config) {
    double d;
    if (config.getMinBigDecimal().compareTo(config.getMaxBigDecimal()) == 0) {
      d = config.getMinBigDecimal().doubleValue();
    } else {
      d =
          ThreadLocalRandom.current()
              .nextDouble(
                  config.getMinBigDecimal().doubleValue(), config.getMaxBigDecimal().doubleValue());
    }
    return BigDecimal.valueOf(d)
        .setScale(config.getBigDecimalScale(), config.getBigDecimalRoundingModel());
  }

  public static BigInteger nextBigInteger(Config config) {
    if (config.getMinBigInterger().compareTo(config.getMaxBigInteger()) == 0) {
      return BigInteger.valueOf(config.getMinBigInterger().longValue());
    }
    return BigInteger.valueOf(
        ThreadLocalRandom.current()
            .nextLong(
                config.getMinBigInterger().longValue(), config.getMaxBigInteger().longValue()));
  }

  public static byte nextByte(Config config) {
    return (byte) ThreadLocalRandom.current().nextInt(config.getMinByte(), config.getMaxByte());
  }

  public static double nextDouble(Config config) {
    double d;
    if (config.getMinDouble() == config.getMaxDouble()) {
      d = config.getMinDouble();
    } else {
      d = ThreadLocalRandom.current().nextDouble(config.getMinDouble(), config.getMaxDouble());
    }
    return BigDecimal.valueOf(d)
        .setScale(config.getDoubleScale(), config.getDoubleRoundingModel())
        .doubleValue();
  }

  public static float nextFloat(Config config) {
    double f;
    if (config.getMinFloat() == config.getMaxFloat()) {
      f = config.getMinFloat();
    } else {
      f = ThreadLocalRandom.current().nextDouble(config.getMinFloat(), config.getMaxFloat());
    }
    return BigDecimal.valueOf(f)
        .setScale(config.getFloatScale(), config.getFloatRoundingModel())
        .floatValue();
  }

  public static int nextInt(Config config) {
    if (config.getMinInt() == config.getMaxInt()) {
      return config.getMinInt();
    }
    return ThreadLocalRandom.current().nextInt(config.getMinInt(), config.getMaxInt());
  }

  public static long nextLong(Config config) {
    if (config.getMinLong() == config.getMaxLong()) {
      return config.getMinLong();
    }
    return ThreadLocalRandom.current().nextLong(config.getMinLong(), config.getMaxLong());
  }

  public static short nextShort(Config config) {
    if (config.getMinShort() == config.getMaxShort()) {
      return config.getMaxShort();
    }
    return (short) ThreadLocalRandom.current().nextInt(config.getMinShort(), config.getMaxShort());
  }
}
