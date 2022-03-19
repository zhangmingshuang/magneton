package org.magneton.support.api.auth;

import org.magneton.adaptive.redis.RedissonAdapter;
import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.distributed.lock.redis.RedissonDistributedLock;
import org.magneton.module.sms.Sms;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.module.sms.redis.RedissonSms;
import org.magneton.module.statistics.Statistics;
import org.magneton.module.statistics.redis.RedissonStatistics;
import org.magneton.support.api.auth.properties.SmsModuleProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmsh 2022/3/19
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ComponentScan
@EnableConfigurationProperties(SmsModuleProperties.class)
public class ApiAuthAutoConfiguration {

	@Autowired
	private SmsModuleProperties smsModuleProperties;

	@ConditionalOnMissingBean(RedissonClient.class)
	@Bean
	public RedissonClient redissonClient() {
		return RedissonAdapter.createSingleServerClient();
	}

	@ConditionalOnMissingBean(DistributedCache.class)
	@Bean
	public DistributedCache distributedCache(RedissonClient redissonClient) {
		return new RedissonDistributedCache(redissonClient);
	}

	@ConditionalOnMissingBean(DistributedLock.class)
	@Bean
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		return new RedissonDistributedLock(redissonClient);
	}

	// ============== sms ===================
	@ConditionalOnMissingBean(AliyunSmsProperty.class)
	@Bean
	public AliyunSmsProperty aliyunSmsProperty() {
		return this.smsModuleProperties.getAliyun();
	}

	@ConditionalOnMissingBean(Sms.class)
	@Bean
	public Sms sms(RedissonClient redissonClient, SendProcessor sendProcessor) {
		return new RedissonSms(redissonClient, sendProcessor, this.smsModuleProperties);
	}
	// ============ sms ============== end

	@ConditionalOnMissingBean(Statistics.class)
	@Bean
	public Statistics statistics(RedissonClient redissonClient) {
		return new RedissonStatistics(redissonClient);
	}

}
