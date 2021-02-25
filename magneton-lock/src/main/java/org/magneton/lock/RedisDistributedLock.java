package org.magneton.lock;

import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import javax.annotation.Nonnull;
import org.redisson.api.RedissonClient;

/**
 * {@code Redisson} distributed lock that relies on {@code Redis}.
 *
 * <pre>{@code
 * Config config = new Config();
 * config.useSingleServer().setAddress("redis://xxxx:xxxx").setPassword("xxxxxx");
 * RedissonClient redissonClient = Redisson.create(config);
 * Lock lock = new RedisLock(client,"lock_key");
 * lock.lock();
 * try {
 *   ...
 * } finally {
 *   lock.unLock();
 * }
 * }</pre>
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/7
 */
public class RedisDistributedLock extends AbstractDistributedLock {

  private final RedissonClient redissonClient;
  private final String key;

  /**
   * Redis Lock.
   *
   * @param redissonClient Redisson Client.
   * @param key the lock key.
   */
  public RedisDistributedLock(RedissonClient redissonClient, String key) {
    Preconditions.checkNotNull(redissonClient, "redissonClient must be not null");
    Preconditions.checkNotNull(key, "key must be not null");
    this.redissonClient = redissonClient;
    this.key = this.reoutchKey(key);
  }

  @Override
  public void lock() {
    this.redissonClient.getLock(this.key).lock();
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {
    this.redissonClient.getLock(this.key).lockInterruptibly();
  }

  @Override
  public boolean tryLock() {
    return this.redissonClient.getLock(this.key).tryLock();
  }

  @Override
  public boolean tryLock(long time, @Nonnull TimeUnit unit) throws InterruptedException {
    return this.redissonClient.getLock(this.key).tryLock(time, unit);
  }

  @Override
  public void unlock() {
    this.redissonClient.getLock(this.key).unlock();
  }

  @Override
  @Nonnull
  public Condition newCondition() {
    return this.redissonClient.getLock(this.key).newCondition();
  }
}
