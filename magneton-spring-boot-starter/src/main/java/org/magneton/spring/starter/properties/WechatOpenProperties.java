package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.wechat.open.WechatOpenConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(WechatOpenProperties.PREFIX)
public class WechatOpenProperties extends WechatOpenConfig {

	public static final String PREFIX = "magneton.module.wechat.open";

	public static final String CONDITION_KEY = "appid";

}
