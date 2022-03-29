package org.magneton.spring.starter.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangmsh 2022/3/29
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(MagnetonProperties.PREFIX)
public class MagnetonProperties {

	public static final String PREFIX = "magneton";

	private boolean cachedHttpRequestWrapper = false;

}
