package org.magneton.module.safedog.sign;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.magneton.core.Result;
import org.magneton.module.safedog.SafeDogErr;

import java.util.concurrent.TimeUnit;

/**
 * 签名缓存.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class MemorySignValidator implements SignValidator {

	private final Cache<String, Long> cache = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES)
			.maximumSize(2048).build();

	@Override
	public Result<Void> validate(String expectedSign, String actualSign, int signPeriod) {
		if (signPeriod > 0) {
			Long signCache = this.cache.getIfPresent(actualSign);
			if (signCache != null) {
				return Result.fail(SafeDogErr.SIGN_USED);
			}
			this.cache.put(actualSign, System.currentTimeMillis());
		}
		if (expectedSign == null || !expectedSign.equals(actualSign)) {
			return Result.fail(SafeDogErr.SIGN_INCONSISTENT);
		}
		return Result.success();
	}

}