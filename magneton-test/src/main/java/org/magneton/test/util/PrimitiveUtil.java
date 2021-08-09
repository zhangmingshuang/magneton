package org.magneton.test.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
public class PrimitiveUtil {

  private static final Class[] PRIMITIVE_TYPES = {
    byte.class,
    short.class,
    int.class,
    long.class,
    float.class,
    double.class,
    char.class,
    boolean.class
  };

  private PrimitiveUtil() {}

  public static Class random() {
    return PRIMITIVE_TYPES[ThreadLocalRandom.current().nextInt(PRIMITIVE_TYPES.length)];
  }
}
