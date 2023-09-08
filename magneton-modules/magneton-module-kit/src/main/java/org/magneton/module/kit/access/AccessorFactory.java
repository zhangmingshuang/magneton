package org.magneton.module.kit.access;

/**
 * Accessor factory.
 *
 * @author zhangmsh.
 * @since 2023.1
 */
public interface AccessorFactory {

	/**
	 * create accessor.
	 * @param name accessor name.
	 * @return accessor.
	 */
	Accessor get(String name);

}
