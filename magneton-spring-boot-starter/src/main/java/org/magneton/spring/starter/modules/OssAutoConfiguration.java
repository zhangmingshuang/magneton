package org.magneton.spring.starter.modules;

import org.magneton.module.oss.aliyun.AliyunOss;
import org.magneton.spring.starter.properties.AliyunOssProperties;
import org.redisson.api.RedissonClient;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, AliyunOss.class })
@ConditionalOnProperty(prefix = AliyunOssProperties.PREFIX, name = AliyunOssProperties.CONDITION_KEY)
public class OssAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public AliyunOssProperties aliyunOssProperties() {
		return new AliyunOssProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public AliyunOss aliyunOss(AliyunOssProperties aliyunOssProperties) {
		return new AliyunOss(aliyunOssProperties);
	}

}
