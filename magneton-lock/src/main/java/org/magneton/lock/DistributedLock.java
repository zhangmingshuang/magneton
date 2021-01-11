package org.magneton.lock;

import java.util.concurrent.locks.Lock;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/7
 */
public interface DistributedLock extends Lock {

  String PREFIX = "dl_";

  /**
   * reoutch key.
   *
   * @param key the given lock key.
   * @return decorated lock key.
   */
  default String reoutchKey(String key) {
    return PREFIX + key;
  }
}
