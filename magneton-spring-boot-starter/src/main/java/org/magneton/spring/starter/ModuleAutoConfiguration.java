package org.magneton.spring.starter;

import org.magneton.module.auth.wechat.WechatAuth;
import org.magneton.module.auth.wechat.WechatAuthImpl;
import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.distributed.lock.redis.RedissonDistributedLock;
import org.magneton.module.geo.Geo;
import org.magneton.module.geo.redis.RedissonGeo;
import org.magneton.module.oss.Oss;
import org.magneton.module.oss.aliyun.AliyunOss;
import org.magneton.module.oss.aliyun.AliyunOssConfig;
import org.magneton.module.pay.wechat.WechatPay;
import org.magneton.module.pay.wechat.WechatV3Pay;
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
import org.magneton.spring.starter.properties.WechatPayProperties;
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
	@ConditionalOnMissingBean
	@ConditionalOnClass(DistributedCache.class)
	public DistributedCache distributedCache(RedissonClient redissonClient) {
		return new RedissonDistributedCache(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(DistributedLock.class)
	public DistributedLock distributedLock(RedissonClient redissonClient) {
		return new RedissonDistributedLock(redissonClient);
	}

	// ============== sms ===================

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(SmsProperty.class)
	public SmsProperties smsProperties() {
		return new SmsProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(AliyunSmsProperty.class)
	public AliyunSmsProperty aliyunSmsProperty(SmsProperties smsProperties) {
		return smsProperties.getAliyun();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(Sms.class)
	public Sms sms(RedissonClient redissonClient, SendProcessor sendProcessor, SmsProperties smsProperties) {
		return new RedissonSms(redissonClient, sendProcessor, smsProperties);
	}

	// ============ sms ============== end
	// ============ geo =============
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(Geo.class)
	public Geo geo(RedissonClient redissonClient) {
		return new RedissonGeo(redissonClient);
	}
	// ============ geo ============= end

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(Statistics.class)
	public Statistics statistics(RedissonClient redissonClient) {
		return new RedissonStatistics(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(SignSafeDog.class)
	public SignSafeDog signSafeDog(RedissonClient redissonClient) {
		return new RedissonSignSafeDog(redissonClient);
	}

	// =============== oss ===========
	@Bean
	@ConditionalOnClass(AliyunOssConfig.class)
	@ConditionalOnProperty(prefix = AliyunOssProperties.PREFIX, name = AliyunOssProperties.CONDITION_KEY)
	public AliyunOssProperties aliyunOssProperties() {
		return new AliyunOssProperties();
	}

	@Bean
	@ConditionalOnClass(AliyunOss.class)
	@ConditionalOnProperty(prefix = AliyunOssProperties.PREFIX, name = AliyunOssProperties.CONDITION_KEY)
	public Oss oss(AliyunOssProperties aliyunOssProperties) {
		return new AliyunOss(aliyunOssProperties);
	}
	// =============== oss =========== end

	// =========== pay wechat ===========
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = WechatPayProperties.PREFIX, name = WechatPayProperties.CONDITION_KEY)
	@ConditionalOnClass(WechatPay.class)
	public WechatPayProperties wechatPayProperties() {
		return new WechatPayProperties();
	}

	@Bean
	@ConditionalOnClass(WechatPay.class)
	@ConditionalOnProperty(prefix = WechatPayProperties.PREFIX, name = WechatPayProperties.CONDITION_KEY)
	public WechatPay wechatPay(WechatPayProperties wechatPayProperties) {
		return new WechatV3Pay(wechatPayProperties);
	}
	// =========== pay wechat =========== end

	// ============ auth ==================
	@Bean
	@ConditionalOnClass(WechatAuth.class)
	public WechatAuth wechatAuth() {
		return new WechatAuthImpl();
	}
	// ============ auth ================== end

}
