package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.wechat.WechatConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(WechatProperties.PREFIX)
public class WechatProperties extends WechatConfig {

	public static final String PREFIX = "magneton.module.wechat";

	public static final String CONDITION_KEY = "appid";

}
