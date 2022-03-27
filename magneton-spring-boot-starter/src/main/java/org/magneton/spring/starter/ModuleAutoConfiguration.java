package org.magneton.spring.starter;

import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.distributed.lock.redis.RedissonDistributedLock;
import org.magneton.module.oss.Oss;
import org.magneton.module.oss.aliyun.AliyunOss;
import org.magneton.module.oss.aliyun.AliyunOssProperty;
import org.magneton.module.safedog.SignSafeDog;
import org.magneton.module.safedog.impl.RedissonSignSafeDog;
import org.magneton.module.sms.Sms;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.module.sms.property.SmsProperty;
import org.magneton.module.sms.redis.RedissonSms;
import org.magneton.module.statistics.Statistics;
import org.magneton.module.statistics.redis.RedissonStatistics;
import org.magneton.spring.starter.properties.AliyunOssProperties;
import org.magneton.spring.starter.properties.SmsProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
@Configuration(proxyBeanMethods = false)
public class ModuleAutoConfiguration {

	@Bean
	@ConditionalOnClass(DistributedCache.class)
	@ConditionalOnMissingBean
	public DistributedCache distributedCache(RedissonClient redissonClient) {
		return new RedissonDistributedCache(redissonClient);
	}

	@Bean
	@ConditionalOnClass(DistributedLock.class)
	@ConditionalOnMissingBean
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		return new RedissonDistributedLock(redissonClient);
	}

	// ============== sms ===================

	@Bean
	@ConditionalOnClass(SmsProperty.class)
	public SmsProperties smsProperties() {
		return new SmsProperties();
	}

	@Bean
	@ConditionalOnClass(AliyunSmsProperty.class)
	public AliyunSmsProperty aliyunSmsProperty(SmsProperties smsProperties) {
		return smsProperties.getAliyun();
	}

	@Bean
	@ConditionalOnClass(Sms.class)
	@ConditionalOnMissingBean
	public Sms sms(RedissonClient redissonClient, SendProcessor sendProcessor, SmsProperties smsProperties) {
		return new RedissonSms(redissonClient, sendProcessor, smsProperties);
	}
	// ============ sms ============== end

	@Bean
	@ConditionalOnClass(Statistics.class)
	@ConditionalOnMissingBean
	public Statistics statistics(RedissonClient redissonClient) {
		return new RedissonStatistics(redissonClient);
	}

	@Bean
	@ConditionalOnClass(SignSafeDog.class)
	@ConditionalOnMissingBean
	public SignSafeDog signSafeDog(RedissonClient redissonClient) {
		return new RedissonSignSafeDog(redissonClient);
	}

	// =============== oss ===========
	@ConditionalOnProperty(prefix = AliyunOssProperties.PREFIX, name = AliyunOssProperties.CONDITION_KEY)
	@Bean
	@ConditionalOnClass(AliyunOssProperty.class)
	public AliyunOssProperties aliyunOssProperties() {
		return new AliyunOssProperties();
	}

	@ConditionalOnProperty(prefix = AliyunOssProperties.PREFIX, name = AliyunOssProperties.CONDITION_KEY)
	@Bean
	@ConditionalOnClass(AliyunOss.class)
	public Oss oss(AliyunOssProperties aliyunOssProperties) {
		return new AliyunOss(aliyunOssProperties);
	}
	// =============== oss =========== end

}
