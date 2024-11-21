package org.magneton.spring.cache;

import com.google.common.base.Strings;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
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

	public static final String REDIS_TEMPLATE = "redisTemplate";

	public static final String REDISSON = "redisson";

	@Resource
	private MagnetonProperties magnetonProperties;

	private ApplicationContext applicationContext;

	@Delegate
	private MCache mCache;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
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

	@SuppressWarnings("rawtypes")
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
				if (REDISSON.equalsIgnoreCase(strategy)) {
					RedissonClient redissonClient = this.applicationContext.getBean(RedissonClient.class);
					return new RedissonMCache(redissonClient);
				}
				else if (REDIS_TEMPLATE.equalsIgnoreCase(strategy)) {
					try {
						RedisTemplate redisTemplate = (RedisTemplate) this.applicationContext.getBean(REDIS_TEMPLATE);
						return new RedisTemplateMCache(redisTemplate);
					}
					catch (Throwable e) {
						RedisTemplate redisTemplate = this.applicationContext.getBean(RedisTemplate.class);
						return new RedisTemplateMCache(redisTemplate);
					}
				}
			}
			catch (Throwable e) {
				log.debug("缓存策略:{}不可用", strategy);
			}
		}
		return new NilMCahce();
	}

}
