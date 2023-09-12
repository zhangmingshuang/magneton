package org.magneton.module.safedog.access;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import lombok.AllArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redis accessor processor.
 *
 * @author zhangmsh.
 * @since 2023.9
 */
public class RedissonAccessorProcessor implements AccessorProcessor {

	/**
	 * 默认的key
	 */
	private static final String DEFAULT_KEY = "magneton:m:accessorProcessor";

	private final RedissonClient redissonClient;

	private final String key;

	private AccessConfig accessConfig;

	public RedissonAccessorProcessor(RedissonClient redissonClient) {
		this(redissonClient, DEFAULT_KEY);
	}

	public RedissonAccessorProcessor(RedissonClient redissonClient, String key) {
		Preconditions.checkNotNull(redissonClient, "redissonClient is null");
		Preconditions.checkNotNull(key, "key is null");

		this.redissonClient = redissonClient;
		this.key = key;
	}

	@Override
	public void setAccessConfig(AccessConfig accessConfig) {
		this.accessConfig = accessConfig;
	}

	@Override
	public Accessor create(String name) {
		Verify.verifyNotNull(this.accessConfig, "accessConfig is null. please set accessConfig first.");

		return new RedisAccessor(name, this.accessConfig, this.redissonClient, this.key);
	}

	@AllArgsConstructor
	public static class RedisAccessor implements Accessor {

		private final String name;

		private final AccessConfig accessConfig;

		private final RedissonClient redissonClient;

		private final String key;

		@Override
		public boolean locked() {
			return this.ttl() > 0;
		}

		@Override
		public long ttl() {
			RBucket<Long> bucket = this.redissonClient.getBucket(this.key + ":lock:" + this.name);
			long ttl = bucket.remainTimeToLive();
			if (ttl < 0) {
				return -1;
			}
			return ttl;
		}

		@Override
		public int onError() {
			RAtomicLong atomicLong = this.redissonClient.getAtomicLong(this.key + ":error:" + this.name);
			long wrongs = atomicLong.incrementAndGet();
			if (wrongs >= this.accessConfig.getNumberOfWrongs()) {
				RBucket<Long> bucket = this.redissonClient.getBucket(this.key + ":lock:" + this.name);
				bucket.set(System.currentTimeMillis(), this.accessConfig.getLockTime(), TimeUnit.MILLISECONDS);
				atomicLong.delete();
				return 0;
			}
			return (int) (this.accessConfig.getNumberOfWrongs() - wrongs);
		}

	}

}
