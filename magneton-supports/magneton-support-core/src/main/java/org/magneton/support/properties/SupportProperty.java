package org.magneton.support.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.magneton.foundation.constant.Constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author zhangmsh 2022/2/16
 * @since 1.2.0
 */
@Component
@ConfigurationProperties(prefix = SupportProperty.PREFIX)
@Setter
@Getter
@ToString
public class SupportProperty {

	public static final String PREFIX = Constant.PROPERTY_SUPPORT_PREFIX;

}
