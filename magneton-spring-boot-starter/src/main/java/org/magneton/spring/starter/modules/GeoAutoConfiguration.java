package org.magneton.spring.starter.modules;

import org.magneton.module.geo.Geo;
import org.magneton.module.geo.redis.RedissonGeo;
import org.redisson.api.RedissonClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, Geo.class })
public class GeoAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Geo geo(RedissonClient redissonClient) {
		return new RedissonGeo(redissonClient);
	}

}
