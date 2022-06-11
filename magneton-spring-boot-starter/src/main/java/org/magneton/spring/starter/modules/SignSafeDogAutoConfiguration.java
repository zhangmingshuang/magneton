package org.magneton.spring.starter.modules;

import org.magneton.module.safedog.SignSafeDog;
import org.magneton.module.safedog.sign.RedissonSignSafeDog;
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
@ConditionalOnClass({ RedissonClient.class, SignSafeDog.class })
public class SignSafeDogAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SignSafeDog signSafeDog(RedissonClient redissonClient) {
		return new RedissonSignSafeDog(redissonClient);
	}

}
