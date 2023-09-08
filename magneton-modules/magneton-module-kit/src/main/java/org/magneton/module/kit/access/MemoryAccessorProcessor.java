package org.magneton.module.kit.access;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Memory Accessor processor.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public class MemoryAccessorProcessor implements AccessorProcessor {

	private AccessConfig accessConfig;

	@Override
	public void setAccessConfig(AccessConfig accessConfig) {
		this.accessConfig = accessConfig;
	}

	@Override
	public Accessor create(String name) {
		return new MemoryAccessor(name, this.accessConfig);
	}

	public static class MemoryAccessor implements Accessor {

		private Cache<String, AtomicInteger> errorRecords;

		private Cache<String, Long> locked;

		private final String name;

		private final AccessConfig accessConfig;

		public MemoryAccessor(String name, AccessConfig accessConfig) {
			this.name = name;
			this.accessConfig = accessConfig;

			this.locked = CacheBuilder.newBuilder()
					.expireAfterWrite(this.accessConfig.getLockTime(), TimeUnit.MILLISECONDS).build();
			this.errorRecords = CacheBuilder.newBuilder()
					.expireAfterAccess(this.accessConfig.getWrongTimeToForget(), TimeUnit.MILLISECONDS).build();
		}

		@Override
		public boolean locked() {
			return this.ttl() > 0;
		}

		@Override
		public long ttl() {
			Long ttl = this.locked.getIfPresent(this.name);
			long now = System.currentTimeMillis();
			if (ttl != null && ttl <= now) {
				// release locked.
				this.locked.invalidate(this.name);
				return -1;
			}
			return ttl == null ? -1 : ttl - now;
		}

		@Override
		public int onError() {
			AtomicInteger errorCount;
			try {
				errorCount = this.errorRecords.get(this.name, AtomicInteger::new);
			}
			catch (ExecutionException e) {
				throw new AccessException("record error", e);
			}
			int wrongs = errorCount.incrementAndGet();
			if (wrongs >= this.accessConfig.getNumberOfWrongs()) {
				long lockTime = this.accessConfig.getAccessTimeCalculator().calculate(this.name, wrongs,
						this.accessConfig);
				this.locked.put(this.name, lockTime);
				return 0;
			}
			return this.accessConfig.getNumberOfWrongs() - errorCount.get();
		}

	}

}
