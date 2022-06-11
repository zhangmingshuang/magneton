package org.magneton.spring.starter.modules;

import org.magneton.module.statistics.Statistics;
import org.magneton.module.statistics.redis.RedissonStatistics;
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
@ConditionalOnClass({ RedissonClient.class, Statistics.class })
public class StatisticsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Statistics statistics(RedissonClient redissonClient) {
		return new RedissonStatistics(redissonClient);
	}

}
