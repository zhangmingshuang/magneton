package org.magneton.spring.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/25
 */
@ConfigurationProperties(prefix = MagnetonProperties.PREFIX)
public class MagnetonProperties {

	public static final String PREFIX = "magneton";

	private MagnetonProperties() {
	}

}
