package org.magneton.spring.cache;

import com.google.common.base.Strings;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.magneton.cache.redis.RedisTemplateMCache;
import org.magneton.cache.redis.RedissonMCache;
import org.magneton.spring.properties.MagnetonProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 提供缓存的能力，内部动态切换缓存实现.
 *
 * @author zhangmsh
 * @since 2024
 */
@Slf4j
@Component
public class StrategyMCache implements MCache, InitializingBean, ApplicationContextAware {

	@Resource
	private MagnetonProperties magnetonProperties;

	private ApplicationContext applicationContext;

	@Delegate
	private MCache mCache;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String cacheStrategy = this.magnetonProperties.getCacheStrategy();
		if (Strings.isNullOrEmpty(cacheStrategy)) {
			cacheStrategy = "memory";
		}
		else {
			cacheStrategy = cacheStrategy.toLowerCase();
			cacheStrategy = cacheStrategy.replace("，", ",");
		}

		String[] strategies = cacheStrategy.split(",");
		for (String strategy : strategies) {
			if (StringUtils.isBlank(strategy)) {
				continue;
			}
			if (this.mCache != null) {
				log.info("缓存策略: {}", this.mCache.clientId());
				break;
			}
			try {
				if ("redisson".equalsIgnoreCase(strategy)) {
					RedissonClient redissonClient = this.applicationContext.getBean(RedissonClient.class);
					this.mCache = new RedissonMCache(redissonClient);
				}
				else if ("redisTemplate".equalsIgnoreCase(strategy)) {
					RedisTemplate redisTemplate = this.applicationContext.getBean(RedisTemplate.class);
					this.mCache = new RedisTemplateMCache(redisTemplate);
				}
				else {
					throw new IllegalArgumentException("未知的缓存策略: " + strategy);
				}
			}
			catch (Throwable e) {
				// ignore
			}
		}
	}

}
