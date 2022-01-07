package org.magneton.lock;

import org.magneton.properties.MagnetonProperties;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/21
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@ConditionalOnClass(DistributedLock.class)
public class MagnetonLockAutoConfiguration {

	@Bean
	@ConditionalOnBean(RedissonClient.class)
	@ConditionalOnMissingBean(DistributedLock.class)
	@ConditionalOnProperty(prefix = MagnetonProperties.PREFIX_LOCK, value = LockProperties.TYPE,
			havingValue = LockProperties.REDIS, matchIfMissing = true)
	public LockManager redisDistributedLock(RedissonClient redissonClient) {
		return new RedisDistributedLockManager(redissonClient);
	}

}
