package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.pay.wechat.v3.core.WxPayConfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(WxPayConfig.class)
@ConfigurationProperties(WxPayProperties.PREFIX)
public class WxPayProperties extends WxPayConfig {

	public static final String PREFIX = "magneton.module.pay.weixin";

}
