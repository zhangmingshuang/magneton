package org.magneton.properties;

import org.magneton.lock.LockProperties;
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

	public static final String PREFIX_LOCK = "magneton.lock";

	/** lock properties. */
	private LockProperties lock;

	private MagnetonProperties() {
	}

}
