package org.magneton.spring.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangmsh 2022/3/29
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(MagnetonProperties.PREFIX)
public class MagnetonProperties {

	public static final String PREFIX = "magneton";

	private boolean cachedHttpRequestWrapper = false;

}
