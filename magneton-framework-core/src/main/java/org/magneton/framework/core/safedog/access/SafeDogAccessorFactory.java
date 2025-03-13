package org.magneton.framework.core.safedog.access;

/**
 * Accessor factory.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public interface SafeDogAccessorFactory {

	/**
	 * create accessor.
	 * @param name accessor name.
	 * @return accessor.
	 */
	SafeDogAccessor get(String name);

}
