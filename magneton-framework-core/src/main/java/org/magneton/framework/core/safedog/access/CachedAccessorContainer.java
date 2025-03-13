package org.magneton.framework.core.safedog.access;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;

/**
 * Accessor container.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public class CachedAccessorContainer implements AccessorContainer {

	private final Cache<String, SafeDogAccessor> accessors;

	{
		this.accessors = CacheBuilder.newBuilder().maximumSize(1024).expireAfterWrite(5, TimeUnit.MINUTES).build();
	}

	@Nullable
	@Override
	public SafeDogAccessor get(String name) {
		return this.accessors.getIfPresent(name);
	}

	@Override
	public void put(String name, SafeDogAccessor safeDogAccessor) {
		this.accessors.put(name, safeDogAccessor);
	}

}
