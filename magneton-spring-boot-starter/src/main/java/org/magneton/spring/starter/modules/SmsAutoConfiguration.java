package org.magneton.spring.starter.modules;

import org.magneton.module.sms.SmsTemplate;
import org.magneton.module.sms.process.SendProcessor;
import org.magneton.module.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.module.sms.property.SmsProperty;
import org.magneton.module.sms.redis.RedissonSmsTemplate;
import org.magneton.spring.starter.properties.SmsProperties;
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
@ConditionalOnClass({ RedissonClient.class, SmsTemplate.class, SmsProperty.class })
public class SmsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SmsProperties smsProperties() {
		return new SmsProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public AliyunSmsProperty aliyunSmsProperty(SmsProperties smsProperties) {
		return smsProperties.getAliyun();
	}

	@Bean
	@ConditionalOnMissingBean
	public SmsTemplate sms(RedissonClient redissonClient, SendProcessor sendProcessor, SmsProperties smsProperties) {
		return new RedissonSmsTemplate(redissonClient, sendProcessor, smsProperties);
	}

}
