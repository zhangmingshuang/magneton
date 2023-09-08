package org.magneton.spring.starter.properties;

import org.magneton.module.oss.aliyun.AliyunOssConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The properties of aliyun oss.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
@ConditionalOnClass(AliyunOssConfig.class)
@ConfigurationProperties(prefix = AliyunOssProperties.PREFIX)
public class AliyunOssProperties extends AliyunOssConfig {

	public static final String PREFIX = "magneton.module.oss.aliyun";

	public static final String CONDITION_KEY = "access-key";

}
