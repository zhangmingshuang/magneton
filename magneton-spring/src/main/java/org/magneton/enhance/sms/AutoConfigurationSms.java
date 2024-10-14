package org.magneton.enhance.sms;

import org.magneton.enhance.sms.process.SendProcessor;
import org.magneton.enhance.sms.process.aliyun.AliyunSmsProperty;
import org.magneton.enhance.sms.property.SmsProperty;
import org.magneton.enhance.sms.redis.RedissonSmsTemplate;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AutoConfigurationSms {

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
	public SmsTemplate sms(RedissonClient redissonClient, @Autowired(required = false) SendProcessor sendProcessor,
			SmsProperties smsProperties) {
		if (sendProcessor == null) {
			return null;
		}
		return new RedissonSmsTemplate(redissonClient, sendProcessor, smsProperties);
	}

}
