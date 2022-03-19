package org.magneton.spring.starter.configuration;

import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.distributed.lock.redis.RedissonDistributedLock;
import org.magneton.module.geo.Geo;
import org.magneton.module.geo.redis.RedissonGeo;
import org.magneton.module.sms.Sms;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.module.sms.redis.RedissonSms;
import org.magneton.spring.starter.properties.SmsModuleProperties;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
@Configuration
public class ModulesAutoConfiguration {

	@Autowired
	private SmsModuleProperties smsModuleProperties;

	@Bean
	@ConditionalOnClass(
			name = { "org.redisson.api.RedissonClient", "org.magneton.module.distributed.cache.DistributedCache" })
	@ConditionalOnMissingBean
	public DistributedCache distributedCache(RedissonClient redissonClient) {
		return new RedissonDistributedCache(redissonClient);
	}

	@Bean
	@ConditionalOnClass(
			name = { "org.redisson.api.RedissonClient", "org.magneton.module.distributed.lock.DistributedLock" })
	@ConditionalOnMissingBean
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		return new RedissonDistributedLock(redissonClient);
	}

	@Bean
	@ConditionalOnClass(name = { "org.redisson.api.RedissonClient", "org.magneton.module.geo.Geo" })
	@ConditionalOnMissingBean
	public Geo geo(RedissonClient redissonClient) {
		return new RedissonGeo(redissonClient);
	}

	// ============== sms ===================
	@Bean
	@ConditionalOnClass(name = { "org.magneton.module.sms.process.SendProcessor",
			"org.magneton.module.sms.process.aliyun.AliyunSmsProperty" })
	@ConditionalOnMissingBean
	public AliyunSmsProperty aliyunSmsProperty() {
		return this.smsModuleProperties.getAliyun();
	}

	@Bean
	@ConditionalOnClass(name = { "org.redisson.api.RedissonClient", "org.magneton.module.sms.process.SendProcessor",
			"org.magneton.module.sms.Sms" })
	@ConditionalOnMissingBean
	public Sms sms(RedissonClient redissonClient, SendProcessor sendProcessor) {
		return new RedissonSms(redissonClient, sendProcessor, this.smsModuleProperties);
	}

}
