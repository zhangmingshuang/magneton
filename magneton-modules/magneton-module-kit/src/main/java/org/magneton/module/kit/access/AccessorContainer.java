package org.magneton.module.kit.access;

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
	Accessor get(String name);

	void put(String name, Accessor accessor);

}
