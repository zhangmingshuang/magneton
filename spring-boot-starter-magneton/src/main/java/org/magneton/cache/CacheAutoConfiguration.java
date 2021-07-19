package org.magneton.cache;

import org.magneton.cache.redis.RedisTemplateCache;
import org.magneton.cache.redis.RedisValueSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * .
 *
 * @author zhangmsh
 * @since 2021/6/24
 */
@Configuration
@ConditionalOnClass(Cache.class)
public class CacheAutoConfiguration {

  @Bean
  @ConditionalOnBean(RedisTemplate.class)
  @ConditionalOnProperty(
      prefix = "magneton.cache",
      name = "redis",
      havingValue = "true",
      matchIfMissing = true)
  public Cache redisCache(RedisTemplate redisTemplate, RedisValueSerializer redisValueSerializer) {
    return new RedisTemplateCache(redisTemplate, redisValueSerializer);
  }
}
