package org.magneton.core.collect;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * .
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
public class Collections {

	private Collections() {

	}

	public static <E> boolean isNullOrEmpty(@Nullable Collection<E> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * Returns an empty map (immutable). This map is serializable.
	 *
	 * <p>
	 * This example illustrates the type-safe way to obtain an empty map: <pre>
	 *     Map&lt;String, Date&gt; s = Collections.emptyMap();
	 * </pre>
	 * @implNote Implementations of this method need not create a separate {@code Map}
	 * object for each call. Using this method is likely to have comparable cost to using
	 * the like-named field. (Unlike this method, the field does not provide type safety.)
	 * @param <K> the class of the map keys
	 * @param <V> the class of the map values
	 * @return an empty map
	 * @see #EMPTY_MAP
	 * @since 1.5
	 */
	public static final <K, V> Map<K, V> emptyMap() {
		return java.util.Collections.emptyMap();
	}

	/**
	 * Returns an immutable list containing only the specified object. The returned list
	 * is serializable.
	 * @param <T> the class of the objects in the list
	 * @param o the sole object to be stored in the returned list.
	 * @return an immutable list containing only the specified object.
	 * @since 1.3
	 */
	public static <T> List<T> singletonList(T o) {
		return java.util.Collections.singletonList(o);
	}

	public static <T> List<T> emptyList() {
		return java.util.Collections.emptyList();
	}

	public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
		return map == null || map.isEmpty();
	}

	public static <T> boolean addAll(Collection<? super T> c, T... elements) {
		return java.util.Collections.addAll(c, elements);
	}

	public static Set<Class<?>> singleton(Class<?> clazz) {
		return Collections.singleton(clazz);
	}

}
