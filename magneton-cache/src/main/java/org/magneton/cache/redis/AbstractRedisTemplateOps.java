package org.magneton.cache.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public abstract class AbstractRedisTemplateOps {

  protected final RedisTemplate redisTemplate;

  public AbstractRedisTemplateOps(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }
}
