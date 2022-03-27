package org.magneton.spring.starter.properties;

import org.magneton.module.oss.aliyun.AliyunOssProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmsh 2022/3/26
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = AliyunOssProperties.PREFIX)
public class AliyunOssProperties extends AliyunOssProperty {

	public static final String PREFIX = "magneton.module.oss.aliyun";

	public static final String CONDITION_KEY = "access-key";

}
