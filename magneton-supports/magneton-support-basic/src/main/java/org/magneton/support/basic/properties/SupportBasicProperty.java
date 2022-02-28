package org.magneton.support.basic.properties;

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
@ConfigurationProperties(prefix = Constant.PROPERTY_SUPPORT_PREFIX + ".basic")
@Component
@Setter
@Getter
@ToString
public class SupportBasicProperty {

	/**
	 * 是否使用缓存
	 */
	private boolean cache;

	/**
	 * 缓存时间，单位毫秒，默认5分钟。当 {@link #cache} 为 {@code true}时有效。
	 */
	private int cacheTime = 5 * 60 * 1000;

}
