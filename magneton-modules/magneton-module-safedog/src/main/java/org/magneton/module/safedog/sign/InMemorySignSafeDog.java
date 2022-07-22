package org.magneton.module.safedog.sign;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
@Slf4j
public class InMemorySignSafeDog extends AbstractSignSafeDog {

	private Cache<String, Long> cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES)
			.maximumSize(1024).build();

	@Override
	public boolean validate(String sign, int signPeriodSeconds, Map<String, String> data, String salt) {
		Preconditions.checkNotNull(sign);
		Preconditions.checkNotNull(data);
		if (signPeriodSeconds > 0) {
			Long signCacheExpireTime = this.cache.getIfPresent(sign);
			if (signCacheExpireTime != null) {
				if (signCacheExpireTime > System.currentTimeMillis()) {
					// 已经过期了
					this.cache.invalidate(sign);
					return true;
				}
				if (log.isDebugEnabled()) {
					log.debug("sign duplicate. {}", sign);
				}
				return false;
			}
			this.cache.put(sign, System.currentTimeMillis() + signPeriodSeconds * 1000L);
		}
		return Objects.equal(sign, this.sign(data, salt));
	}

}
