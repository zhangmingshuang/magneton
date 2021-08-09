package org.magneton.test.injector;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
public enum InjectType {
  ANGEL,
  DEMON;

  public boolean isDemon() {
    return this == DEMON;
  }
}
