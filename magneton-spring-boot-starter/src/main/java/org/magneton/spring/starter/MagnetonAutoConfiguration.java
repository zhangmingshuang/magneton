package org.magneton.spring.starter;

import java.util.Locale;

import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.adaptive.redis.RedissonClientType;
import org.magneton.spring.starter.properties.MagnetonProperties;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(MagnetonProperties.class)
public class MagnetonAutoConfiguration {

	// @Bean
	// public RunLauncherContextInitializer runLauncherInitializer() {
	// return RunLauncherContextInitializer.getInstance();
	// }

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(RedissonAdapter.class)
	public RedissonClient redissonClient() {
		String redissonClientType = System.getProperty("redisson.adapter.client.type",
				RedissonClientType.SINGLE.name());
		return RedissonAdapter.createClient(RedissonClientType.valueOf(redissonClientType.toUpperCase(Locale.ROOT)));
	}

	@ConditionalOnClass(CacheManager.class)
	@ConditionalOnBean(RedissonClient.class)
	@ConditionalOnProperty(prefix = "magneton.spring.cache.redis", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public RedissonSpringCacheManager redissonSpringCacheManager(RedissonClient redissonClient) {
		return new RedissonSpringCacheManager(redissonClient);
	}

}
