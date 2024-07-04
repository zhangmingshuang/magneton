package org.magneton.enhance.safedog.access;

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

	private final Cache<String, Accessor> accessors = CacheBuilder.newBuilder()
		.maximumSize(1024)
		.expireAfterWrite(5, TimeUnit.MINUTES)
		.build();

	@Nullable
	@Override
	public Accessor get(String name) {
		return this.accessors.getIfPresent(name);
	}

	@Override
	public void put(String name, Accessor accessor) {
		this.accessors.put(name, accessor);
	}

}
