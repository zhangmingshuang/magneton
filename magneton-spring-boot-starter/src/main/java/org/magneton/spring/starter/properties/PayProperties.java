package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.pay.wechat.WechatPayConfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author zhangmsh 30/03/2022
 * @since 2.0.7
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(PayProperties.PREFIX)
public class PayProperties {

	public static final String PREFIX = "magneton.module.pay";

	public static final String WECHAT_PREFIX = "magneton.module.pay.wechat";

	public static final String WECHAT_CONDITION_KEY = "app-id";

	@NestedConfigurationProperty
	private WechatPayConfig wechat;

}
