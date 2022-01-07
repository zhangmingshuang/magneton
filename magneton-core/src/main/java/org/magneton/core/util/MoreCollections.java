package org.magneton.core.util;

import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/25
 */
public class MoreCollections {

	private MoreCollections() {
	}

	/**
	 * Determine given {@code Collection} is {@code null} or empty.
	 * @param collection given collection.
	 * @return {@code true} if given collection is{@code null} or empty, or {@code false}
	 * if not.
	 */
	public static boolean isNullOrEmpty(@Nullable Collection collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Determine given {@code Map} is {@code null} or empty.
	 * @param map given map.
	 * @return {@code true} if given map is {@code null} or empty, or {@code false} if
	 * not.
	 */
	public static boolean isNullOrEmpty(@Nullable Map map) {
		return map == null || map.isEmpty();
	}

}
