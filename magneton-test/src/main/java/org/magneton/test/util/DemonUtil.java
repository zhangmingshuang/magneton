package org.magneton.test.util;

import java.util.concurrent.ThreadLocalRandom;
import org.magneton.test.injector.InjectType;

/**
 * .
 *
 * @author zhangmsh 2021/8/3
 * @since
 */
public class DemonUtil {

  private DemonUtil() {}

  public static int createInt(InjectType injectType, int value) {
    if (!injectType.isDemon()) {
      return value;
    }
    int demon = ThreadLocalRandom.current().nextInt();
    return (demon + value) / ThreadLocalRandom.current().nextInt(1, 10);
  }
}
