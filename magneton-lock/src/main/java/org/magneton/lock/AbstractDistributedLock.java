package org.magneton.lock;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
public abstract class AbstractDistributedLock implements DistributedLock {

  private static final String LOCK_KEY_PREFIX = "dl_";

  /**
   * reoutch key.
   *
   * @param key the given lock key.
   * @return decorated lock key.
   */
  protected final String reoutchKey(String key) {
    return LOCK_KEY_PREFIX + key;
  }
}
