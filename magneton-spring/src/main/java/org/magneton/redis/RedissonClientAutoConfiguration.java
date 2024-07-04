package org.magneton.redis;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author zhangmsh 2022/6/11
 * @since 1.0.0
 */
@Component
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RedissonClient.class, RedissonAdapter.class })
public class RedissonClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(RedissonAdapter.class)
	public RedissonClient redissonClient(Environment env) {
		String redissonClientType = System.getProperty("redisson.adapter.client.type",
				RedissonClientType.SINGLE.name());
		String profile = System.getProperty("spring.profiles.active",
				System.getProperty("redisson.adapter.prefix", ""));
		if (Strings.isNullOrEmpty(profile)) {
			profile = env.getProperty("spring.profiles.active");
			System.setProperty("redisson.adapter.prefix", MoreObjects.firstNonNull(profile, ""));
		}
		return RedissonAdapter.createClient(RedissonClientType.valueOf(redissonClientType.toUpperCase(Locale.ROOT)));
	}

}
