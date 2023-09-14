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
	private static final String DEFAULT_KEY = "magneton:m:accessor";

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

		return new RedissonAccessor(name, this.accessConfig, this.redissonClient, this.key);
	}

	@AllArgsConstructor
	public static class RedissonAccessor implements Accessor {

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
			RBucket<Long> bucket = this.redissonClient.getBucket(this.bizKey(":lock:"));
			long ttl = bucket.remainTimeToLive();
			if (ttl < 0) {
				return -1;
			}
			return ttl;
		}

		@Override
		public int onError() {
			try {
				if (this.locked()) {
					return 0;
				}
				RAtomicLong atomicLong = this.redissonClient.getAtomicLong(this.bizKey(":error:"));
				long wrongs = atomicLong.incrementAndGet();
				if (wrongs == 1 || (wrongs < 6 && atomicLong.remainTimeToLive() == -1)) {
					atomicLong.expire(this.accessConfig.getWrongTimeToForget(), TimeUnit.MILLISECONDS);
				}
				if (wrongs >= this.accessConfig.getNumberOfWrongs()) {
					long lockTime = this.accessConfig.getAccessTimeCalculator().calculate(this.name, wrongs,
							this.accessConfig);
					RBucket<Long> bucket = this.redissonClient.getBucket(this.bizKey(":lock:"));
					bucket.set(System.currentTimeMillis(), lockTime, TimeUnit.MILLISECONDS);
					atomicLong.delete();
					return 0;
				}
				return (int) (this.accessConfig.getNumberOfWrongs() - wrongs);
			}
			catch (Throwable e) {
				throw new AccessException("processor error", e);
			}
		}

		@Override
		public void reset() {
			RBucket<Long> bucket = this.redissonClient.getBucket(this.bizKey(":lock:"));
			bucket.delete();
			RAtomicLong atomicLong = this.redissonClient.getAtomicLong(this.bizKey(":error:"));
			atomicLong.delete();
		}

		protected String bizKey(String biz) {
			return this.key + biz + this.name;
		}

	}

}
