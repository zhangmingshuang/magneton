package org.magneton.spring.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 框架的配置属性.
 *
 * @author zhangmsh
 * @since 2024
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(MagnetonProperties.PREFIX)
public class MagnetonProperties {

	public static final String PREFIX = "magneton";

	private boolean cachedHttpRequestWrapper = false;

	/**
	 * 缓存策略优先级，如果有redis则优先使用redisson，如果没有则使用memory.
	 */
	private String cacheStrategy = "redisson,redisTemplate";

}
