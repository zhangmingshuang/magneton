package org.magneton.spring.starter;

import org.magneton.module.distributed.cache.DistributedCache;
import org.magneton.module.distributed.cache.redis.RedissonDistributedCache;
import org.magneton.module.distributed.lock.DistributedLock;
import org.magneton.module.distributed.lock.redis.RedissonDistributedLock;
import org.magneton.module.geo.Geo;
import org.magneton.module.geo.redis.RedissonGeo;
import org.magneton.module.im.tencent.TencentIm;
import org.magneton.module.im.tencent.UserSignCache;
import org.magneton.module.oss.aliyun.AliyunOss;
import org.magneton.module.oss.aliyun.AliyunOssConfig;
import org.magneton.module.pay.wechat.v3.WxV3Pay;
import org.magneton.module.pay.wechat.v3.WxV3PayImpl;
import org.magneton.module.safedog.SignSafeDog;
import org.magneton.module.safedog.sign.RedissonSignSafeDog;
import org.magneton.module.sms.Sms;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.module.sms.property.SmsProperty;
import org.magneton.module.sms.redis.RedissonSms;
import org.magneton.module.statistics.Statistics;
import org.magneton.module.statistics.redis.RedissonStatistics;
import org.magneton.module.wechat.Wechat;
import org.magneton.module.wechat.WechatBuilder;
import org.magneton.module.wechat.core.oauth2.WechatAccessTokenCache;
import org.magneton.spring.starter.extension.tencent.im.TencentImUserSignCache;
import org.magneton.spring.starter.extension.wechat.RedisWechatAccessTokenCache;
import org.magneton.spring.starter.properties.AliyunOssProperties;
import org.magneton.spring.starter.properties.SmsProperties;
import org.magneton.spring.starter.properties.TencentImProperties;
import org.magneton.spring.starter.properties.WechatProperties;
import org.magneton.spring.starter.properties.WxPayProperties;
import org.redisson.api.RedissonClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
	public AliyunOss aliyunOss(AliyunOssProperties aliyunOssProperties) {
		return new AliyunOss(aliyunOssProperties);
	}
	// =============== oss =========== end

	// =========== pay wechat ===========
	@Bean
	@ConditionalOnClass(WxV3Pay.class)
	@ConditionalOnProperty(prefix = WxPayProperties.PREFIX, name = WxPayProperties.CONDITION_KEY)
	public WxV3Pay wechatPay(WxPayProperties wechatPayProperties) {
		return new WxV3PayImpl(wechatPayProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = WxPayProperties.PREFIX, name = WxPayProperties.CONDITION_KEY)
	@ConditionalOnClass(WxV3Pay.class)
	public WxPayProperties wechatPayProperties() {
		return new WxPayProperties();
	}
	// =========== pay wechat =========== end

	// ============== wechat =============
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = WechatProperties.PREFIX, name = WechatProperties.CONDITION_KEY)
	@ConditionalOnClass(Wechat.class)
	public WechatProperties wechatProperties() {
		return new WechatProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = WechatProperties.PREFIX, name = WechatProperties.CONDITION_KEY)
	@ConditionalOnClass(Wechat.class)
	public WechatAccessTokenCache accessTokenCache(RedissonClient redissonClient) {
		return new RedisWechatAccessTokenCache(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = WechatProperties.PREFIX, name = WechatProperties.CONDITION_KEY)
	@ConditionalOnClass(Wechat.class)
	public Wechat wechat(WechatProperties wechatProperties, WechatAccessTokenCache wechatAccessTokenCache) {
		return WechatBuilder.newBuilder(wechatProperties).accessTokenCache(wechatAccessTokenCache).build();
	}
	// ============== wechat ============= end

	// ================ im =======================
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(TencentIm.class)
	public UserSignCache userSignCache(RedissonClient redissonClient) {
		return new TencentImUserSignCache(redissonClient);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(TencentIm.class)
	public TencentImProperties tencentImProperties() {
		return new TencentImProperties();
	}

	public TencentIm tencentIm(TencentImProperties tencentImProperties,
			@Autowired(required = false) UserSignCache userSignCache) {
		TencentIm tencentIm = new TencentIm(tencentImProperties);
		tencentIm.setUserSignCache(userSignCache);
		return tencentIm;
	}
	// ================ im ======================= end

}
