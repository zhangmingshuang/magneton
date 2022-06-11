package org.magneton.spring.starter.modules;

import java.util.Locale;

import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.adaptive.redis.RedissonClientType;
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
@ConditionalOnClass({ RedissonClient.class, RedissonAdapter.class })
public class RedissonClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(RedissonAdapter.class)
	public RedissonClient redissonClient() {
		String redissonClientType = System.getProperty("redisson.adapter.client.type",
				RedissonClientType.SINGLE.name());
		return RedissonAdapter.createClient(RedissonClientType.valueOf(redissonClientType.toUpperCase(Locale.ROOT)));
	}

}
