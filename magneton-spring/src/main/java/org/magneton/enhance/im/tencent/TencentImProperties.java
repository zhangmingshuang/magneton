package org.magneton.enhance.im.tencent;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(TencentImConfig.class)
@ConfigurationProperties(prefix = TencentImProperties.PREFIX)
public class TencentImProperties extends TencentImConfig {

	public static final String PREFIX = "magneton.module.im.tencent";

	public static final String CONDITION_KEY = "app-id";

}
