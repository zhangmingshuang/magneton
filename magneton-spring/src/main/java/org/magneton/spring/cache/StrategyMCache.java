package org.magneton.spring.cache;

import com.google.common.base.Strings;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.magneton.cache.MCache;
import org.magneton.cache.NilMCahce;
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
	public void afterPropertiesSet() {
		this.mCache = this.getMCache(this.magnetonProperties);
		if (!this.mCache.usable()) {
			log.info("关闭缓存策略");
		}
		else {
			log.info("使用缓存策略:{}", this.mCache.clientId());
		}
	}

	protected MCache getMCache(MagnetonProperties magnetonProperties) {
		String cacheStrategy = magnetonProperties.getCacheStrategy();
		if (Strings.isNullOrEmpty(cacheStrategy) || "false".equalsIgnoreCase(cacheStrategy)) {
			return new NilMCahce();
		}
		cacheStrategy = cacheStrategy.toLowerCase();
		cacheStrategy = cacheStrategy.replace("，", ",");

		String[] strategies = cacheStrategy.split(",");
		for (String strategy : strategies) {
			if (StringUtils.isBlank(strategy)) {
				continue;
			}
			try {
				if ("redisson".equalsIgnoreCase(strategy)) {
					RedissonClient redissonClient = this.applicationContext.getBean(RedissonClient.class);
					return new RedissonMCache(redissonClient);
				}
				else if ("redisTemplate".equalsIgnoreCase(strategy)) {
					@SuppressWarnings("rawtypes")
					RedisTemplate redisTemplate = this.applicationContext.getBean(RedisTemplate.class);
					return new RedisTemplateMCache(redisTemplate);
				}
			}
			catch (Throwable e) {
				// ignore
			}
		}
		return new NilMCahce();
	}

}
