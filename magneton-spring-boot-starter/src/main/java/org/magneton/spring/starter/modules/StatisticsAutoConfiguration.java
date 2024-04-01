package org.magneton.spring.starter.modules;

import org.magneton.module.statistics.pvpu.PvUv;
import org.magneton.module.statistics.pvpu.PvUvConfig;
import org.magneton.module.statistics.pvpu.RedissonPvUv;
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
@ConditionalOnClass({ RedissonClient.class, PvUv.class })
public class StatisticsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public PvUv statistics(RedissonClient redissonClient) {
		return new RedissonPvUv(redissonClient, new PvUvConfig());
	}

}