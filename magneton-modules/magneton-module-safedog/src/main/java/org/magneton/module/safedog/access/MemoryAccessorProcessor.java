package org.magneton.module.safedog.access;

import com.google.common.base.Verify;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final Map<AccessConfig, CacheNode> CACHE_NODES = new LinkedHashMap() {
		@Override
		protected boolean removeEldestEntry(Map.Entry eldest) {
			return this.size() > 4096;
		}
	};

	private AccessConfig accessConfig;

	@Override
	public void setAccessConfig(AccessConfig accessConfig) {
		this.accessConfig = accessConfig;
		CACHE_NODES.computeIfAbsent(accessConfig, CacheNode::new);
	}

	@Override
	public Accessor create(String name) {
		Verify.verifyNotNull(this.accessConfig, "accessConfig is null. please set accessConfig first.");

		return new MemoryAccessor(name, this.accessConfig);
	}

	public static class CacheNode {

		private final Cache<String, AtomicInteger> errorRecords;

		private final Cache<String, Long> locked;

		public CacheNode(AccessConfig accessConfig) {
			this.locked = CacheBuilder.newBuilder().expireAfterWrite(accessConfig.getLockTime(), TimeUnit.MILLISECONDS)
					.build();
			this.errorRecords = CacheBuilder.newBuilder()
					.expireAfterAccess(accessConfig.getWrongTimeToForget(), TimeUnit.MILLISECONDS).build();
		}

	}

	public static class MemoryAccessor implements Accessor {

		private final String name;

		private final AccessConfig accessConfig;

		public MemoryAccessor(String name, AccessConfig accessConfig) {
			this.name = name;
			this.accessConfig = accessConfig;
		}

		@Override
		public boolean locked() {
			return this.ttl() > 0;
		}

		@Override
		public long ttl() {
			CacheNode cacheNode = CACHE_NODES.get(this.accessConfig);
			Long ttl = cacheNode.locked.getIfPresent(this.name);
			long now = System.currentTimeMillis();
			if (ttl != null && ttl <= now) {
				// release locked.
				cacheNode.locked.invalidate(this.name);
				return -1;
			}
			return ttl == null ? -1 : ttl - now;
		}

		@Override
		public int onError() {
			CacheNode cacheNode = CACHE_NODES.get(this.accessConfig);
			AtomicInteger errorCount;
			try {
				errorCount = cacheNode.errorRecords.get(this.name, AtomicInteger::new);
			}
			catch (ExecutionException e) {
				throw new AccessException("record error", e);
			}
			if (this.locked()) {
				return 0;
			}
			int wrongs = errorCount.incrementAndGet();
			if (wrongs >= this.accessConfig.getNumberOfWrongs()) {
				long lockTime = this.accessConfig.getAccessTimeCalculator().calculate(this.name, wrongs,
						this.accessConfig);
				cacheNode.locked.put(this.name, System.currentTimeMillis() + lockTime);
				cacheNode.errorRecords.invalidate(this.name);
				return 0;
			}
			return this.accessConfig.getNumberOfWrongs() - errorCount.get();
		}

		@Override
		public void reset() {
			CacheNode cacheNode = CACHE_NODES.get(this.accessConfig);
			cacheNode.locked.invalidate(this.name);
			cacheNode.errorRecords.invalidate(this.name);
		}

	}

}
