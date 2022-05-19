package org.magneton.module.safedog.sign;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
public class InMemorySignSafeDog extends AbstractSignSafeDog {

	private Cache<String, Long> cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
			.maximumSize(1024).build();

	@Override
	public boolean validate(String sign, int signPeriodSeconds, Map<String, String> data, String salt) {
		Preconditions.checkNotNull(sign);
		Preconditions.checkNotNull(data);
		if (signPeriodSeconds > 0) {
			Long signCache = this.cache.getIfPresent(sign);
			if (signCache != null) {
				return false;
			}
			this.cache.put(sign, System.currentTimeMillis());
		}
		return Objects.equal(sign, this.sign(data, salt));
	}

}
