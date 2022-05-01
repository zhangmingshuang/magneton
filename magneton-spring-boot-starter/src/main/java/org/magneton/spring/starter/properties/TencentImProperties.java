package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.im.tencent.TencentImConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * .
 *
 * @author zhangmsh 2022/4/26
 * @since 2.0.8
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = TencentImProperties.PREFIX)
public class TencentImProperties extends TencentImConfig {

	public static final String PREFIX = "magneton.module.im.tencent";

}
