package org.magneton.lock;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import javax.annotation.Nonnull;
import org.redisson.RedissonRedLock;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/7
 */
@SuppressWarnings("LockAcquiredButNotSafelyReleased")
public class RedisDistributedRedLock implements DistributedLock {

  private final RedissonRedLock redissonRedLock;

  public RedisDistributedRedLock(RedissonRedLock redissonRedLock) {
    Preconditions.checkNotNull(redissonRedLock, "redissonRedLock must be not null");
    this.redissonRedLock = redissonRedLock;
  }

  @Override
  public void lock() {
    this.redissonRedLock.lock();
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {
    this.redissonRedLock.lockInterruptibly();
  }

  @Override
  public boolean tryLock() {
    return this.redissonRedLock.tryLock();
  }

  @Override
  public boolean tryLock(long time, @Nonnull TimeUnit unit) throws InterruptedException {
    return this.redissonRedLock.tryLock(time, unit);
  }

  @Override
  public void unlock() {
    this.redissonRedLock.unlock();
  }

  @Override
  @Nonnull
  public Condition newCondition() {
    return this.redissonRedLock.newCondition();
  }
}
