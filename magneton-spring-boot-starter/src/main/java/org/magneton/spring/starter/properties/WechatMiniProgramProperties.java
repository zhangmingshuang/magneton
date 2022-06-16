package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.module.wechat.miniprogram.WechatMiniProgramConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmsh 2022/4/3
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@ConditionalOnClass(WechatMiniProgramConfig.class)
@ConfigurationProperties(WechatMiniProgramProperties.PREFIX)
public class WechatMiniProgramProperties extends WechatMiniProgramConfig {

	public static final String PREFIX = "magneton.module.wechat.mini-program";

	public static final String CONDITION_KEY = "appid";

}
