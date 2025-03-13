package org.magneton.framework.core.safedog.access;

import javax.annotation.Nullable;

/**
 * Accessor container.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public interface AccessorContainer {

	/**
	 * get accessor.
	 * @param name accessor name.
	 * @return accessor.
	 */
	@Nullable
	SafeDogAccessor get(String name);

	void put(String name, SafeDogAccessor safeDogAccessor);

}
