package org.magneton.enhance.safedog.sign;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.magneton.core.Result;
import org.magneton.enhance.safedog.SafeDogErr;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的签名安全狗
 *
 * @author zhangmsh 2022/3/20
 * @since 1.0.0
 */
public class RedissonSignValidator implements SignValidator {

	/**
	 * 默认的key
	 */
	private static final String DEFAULT_KEY = "magneton:m:signer";

	private final RedissonClient redissonClient;

	private final String key;

	public RedissonSignValidator(RedissonClient redissonClient) {
		this(redissonClient, DEFAULT_KEY);
	}

	public RedissonSignValidator(RedissonClient redissonClient, String key) {
		Preconditions.checkNotNull(redissonClient, "redissonClient is null");
		Preconditions.checkNotNull(key, "key is null");

		this.redissonClient = redissonClient;
		this.key = key;
	}

	@Override
	public Result<Void> validate(String expectedSign, String actualSign, int signPeriod) {
		if (signPeriod > 0) {
			RBucket<String> signBucket = this.redissonClient.getBucket(this.key + ":" + actualSign);
			if (signBucket.isExists()) {
				return Result.fail(SafeDogErr.SIGN_USED);
			}
			signBucket.set(String.valueOf(System.currentTimeMillis()), signPeriod, TimeUnit.SECONDS);
		}
		if (expectedSign == null || !Objects.equal(expectedSign, actualSign)) {
			return Result.fail(SafeDogErr.SIGN_INCONSISTENT);
		}
		return Result.success();
	}

}