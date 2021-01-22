package org.magneton.lock;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
public interface LockManager {

  /**
   * get distributed lock.
   *
   * @param key lock key.
   * @return the lock.
   */
  DistributedLock getLock(String key);
}
