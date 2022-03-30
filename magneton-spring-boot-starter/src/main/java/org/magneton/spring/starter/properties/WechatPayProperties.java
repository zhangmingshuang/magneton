package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.pay.wechat.WechatPayConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(WechatPayProperties.PREFIX)
public class WechatPayProperties extends WechatPayConfig {

	public static final String PREFIX = "magneton.module.pay.wechat";

	public static final String CONDITION_KEY = "app-id";

}
