package org.magneton.test.util;

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
}
