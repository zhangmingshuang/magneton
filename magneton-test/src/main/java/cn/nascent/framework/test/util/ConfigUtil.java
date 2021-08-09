package cn.nascent.framework.test.util;

import cn.nascent.framework.test.core.Config;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
public class ConfigUtil {

  private ConfigUtil() {}

  public static int getRandomSize(Config config) {
    return ThreadLocalRandom.current().nextInt(config.getMinSize(), config.getMaxSize());
  }

  @Nullable
  public static boolean isNullable(Config config) {
    int demoNullProbability = config.getDemonNullProbability();
    return ThreadLocalRandom.current().nextInt(100) < demoNullProbability;
  }
}
