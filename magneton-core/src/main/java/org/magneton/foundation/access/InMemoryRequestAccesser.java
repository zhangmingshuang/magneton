package org.magneton.foundation.access;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

/**
 * In meory accesser.
 *
 * <pre>{@code
 * RequestAccesser requestAccesser =
 *         (RequestAccesser)
 *             AccesserBuilder.of(new InMemoryRequestAccesser<>()).build();
 * }</pre>
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 * @see RequestAccesser
 */
@Slf4j
public class InMemoryRequestAccesser<T> extends AbstractAccesser implements RequestAccesser<T> {

	private Cache<String, AtomicInteger> errorRcords;

	private Cache<String, Long> locked;

	@Override
	protected void initialiazation() {
		this.locked = CacheBuilder.newBuilder()
				.expireAfterWrite(super.accessConfig.getLockTime(), TimeUnit.MILLISECONDS)
				.maximumSize(super.accessConfig.getLockSize()).build();
		this.errorRcords = CacheBuilder.newBuilder()
				.expireAfterAccess(super.accessConfig.getErrorRecordTime(), TimeUnit.MILLISECONDS)
				.maximumSize(super.accessConfig.getErrorRecordSize()).build();
	}

	@Override
	public boolean locked(String key) {
		return this.ttl(key) > 0;
	}

	@Override
	public long ttl(String key) {
		@Nullable
		Long ttl = this.locked.getIfPresent(Preconditions.checkNotNull(key));
		long now = System.currentTimeMillis();
		if (ttl != null && ttl <= now) {
			// release locked.
			this.locked.invalidate(key);
			return -1;
		}
		return ttl == null ? -1 : ttl - now;
	}

	@Override
	public int recordError(String key) {
		AtomicInteger errorCount;
		try {
			errorCount = this.errorRcords.get(Preconditions.checkNotNull(key), AtomicInteger::new);
		}
		catch (ExecutionException e) {
			throw new AccessException("record errror", e);
		}
		if (errorCount.incrementAndGet() >= super.accessConfig.getLockErrorCount()) {
			this.locked.put(key, System.currentTimeMillis() + super.accessConfig.getLockTime());
			return 0;
		}
		return super.accessConfig.getLockErrorCount() - errorCount.get();
	}

	@Override
	public Accessible access(String key, Supplier<Boolean> supplier) {
		boolean locked = this.locked(key);
		if (locked) {
			long ttl = this.ttl(key);
			if (ttl > 1) {
				// if lock effective.
				return Accessible.lock(ttl, key + " is locked");
			}
		}
		Boolean success = Preconditions.checkNotNull(supplier).get();
		Verify.verifyNotNull(success, "access return must be not null");
		if (success) {
			return Accessible.access(true, super.accessConfig.getLockErrorCount());
		}
		int errorRemainCount = this.recordError(key);
		if (errorRemainCount < 1) {
			long ttl = this.ttl(key);
			return Accessible.lock(ttl, key + " is locked");
		}
		return Accessible.access(false, errorRemainCount);
	}

}
