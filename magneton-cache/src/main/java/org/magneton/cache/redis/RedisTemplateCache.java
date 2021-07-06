package org.magneton.cache.redis;

import org.magneton.cache.Cache;
import org.magneton.cache.ops.HashOps;
import org.magneton.cache.ops.ListOps;
import org.magneton.cache.ops.ValueOps;
import org.magneton.cache.util.Trans;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
@SuppressWarnings("ConstantConditions")
public class RedisTemplateCache implements Cache {

  private final RedisTemplate redisTemplate;
  private final ValueOps valueOps;
  private final ListOps listOps;
  private final HashOps hashOps;

  public RedisTemplateCache(RedisTemplate redisTemplate, RedisValueSerializer valueSerializer) {
    this.redisTemplate = redisTemplate;
    this.valueOps = new RedisTemplateValueOps(redisTemplate, valueSerializer);
    this.listOps = new RedisTemplateListOps(redisTemplate, valueSerializer);
    this.hashOps = new RedisTemplateHashOps(redisTemplate, valueSerializer);
  }

  @Override
  public HashOps opsForHash() {
    return this.hashOps;
  }

  @Override
  public ListOps opsForList() {
    return this.listOps;
  }

  @Override
  public ValueOps opsForValue() {
    return this.valueOps;
  }

  @Override
  public long ttl(String key) {
    return (long) this.redisTemplate.execute((RedisCallback) rb -> rb.ttl(Trans.toByte(key)));
  }

  @Override
  public boolean expire(String key, long expire) {
    return (boolean)
        this.redisTemplate.execute((RedisCallback) cb -> cb.expire(Trans.toByte(key), expire));
  }

  @Override
  public boolean exists(String key) {
    return (boolean) this.redisTemplate.execute((RedisCallback) rc -> rc.exists(Trans.toByte(key)));
  }

  @Override
  public long del(String... keys) {
    return (long) this.redisTemplate.execute((RedisCallback) rc -> rc.del(Trans.toByteArray(keys)));
  }
}
