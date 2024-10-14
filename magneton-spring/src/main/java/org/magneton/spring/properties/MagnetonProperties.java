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

	public static final String CACHE_STRATEGY = PREFIX + ".cache-strategy";

	private boolean cachedHttpRequestWrapper = false;

	/**
	 * 缓存策略优先级，如果有redis则优先使用redisson，如果都不支持默认关闭（也可以使用-直接关闭）
	 */
	private String cacheStrategy = "redisson,redisTemplate";

}
