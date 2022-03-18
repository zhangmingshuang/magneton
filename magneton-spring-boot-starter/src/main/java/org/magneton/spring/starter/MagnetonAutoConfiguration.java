package org.magneton.spring.starter;

import org.magneton.adaptive.redis.RedissonAdapter;
import org.redisson.api.RedissonClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
@Configuration
public class MagnetonAutoConfiguration {

	// @Bean
	// public RunLauncherContextInitializer runLauncherInitializer() {
	// return RunLauncherContextInitializer.getInstance();
	// }

	@Bean
	@ConditionalOnClass(RedissonAdapter.class)
	@ConditionalOnMissingBean
	public RedissonClient redissonClient() {
		return RedissonAdapter.createSingleServerClient();
	}

}
