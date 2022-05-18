package org.magneton.core.collect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
	 * @since 1.5
	 */
	public static <K, V> Map<K, V> emptyMap() {
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

	public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
		return java.util.Collections.newSetFromMap(map);
	}

	/**
	 * 去重集合
	 * @param <T> 集合元素类型
	 * @param collection 集合
	 * @return {@link ArrayList}
	 */
	public static <T> ArrayList<T> distinct(Collection<T> collection) {
		if (isNullOrEmpty(collection)) {
			return new ArrayList<>();
		}
		else if (collection instanceof Set) {
			return new ArrayList<>(collection);
		}
		else {
			return new ArrayList<>(new LinkedHashSet<>(collection));
		}
	}

	/**
	 * 两个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c]，此结果中只保留了两个c
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 交集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> intersection(Collection<T> coll1, Collection<T> coll2) {
		if (!isNullOrEmpty(coll1) && !isNullOrEmpty(coll2)) {
			final ArrayList<T> list = new ArrayList<>(Math.min(coll1.size(), coll2.size()));
			final Map<T, Integer> map1 = CollUtil.countMap(coll1);
			final Map<T, Integer> map2 = CollUtil.countMap(coll2);
			final Set<T> elts = Sets.newHashSet(coll2);
			int m;
			for (T t : elts) {
				m = Math.min(Convert.toInt(map1.get(t), 0), Convert.toInt(map2.get(t), 0));
				for (int i = 0; i < m; i++) {
					list.add(t);
				}
			}
			return list;
		}

		return new ArrayList<>();
	}

	/**
	 * 多个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留最少的个数<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c, c]，此结果中只保留了两个c
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param otherColls 其它集合
	 * @return 交集的集合，返回 {@link ArrayList}
	 */
	@SafeVarargs
	public static <T> Collection<T> intersection(Collection<T> coll1, Collection<T> coll2,
			Collection<T>... otherColls) {
		Collection<T> intersection = intersection(coll1, coll2);
		if (isNullOrEmpty(intersection)) {
			return intersection;
		}
		for (Collection<T> coll : otherColls) {
			intersection = intersection(intersection, coll);
			if (isNullOrEmpty(intersection)) {
				return intersection;
			}
		}
		return intersection;
	}

	/**
	 * 多个集合的交集<br>
	 * 针对一个集合中存在多个相同元素的情况，只保留一个<br>
	 * 例如：集合1：[a, b, c, c, c]，集合2：[a, b, c, c]<br>
	 * 结果：[a, b, c]，此结果中只保留了一个c
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param otherColls 其它集合
	 * @return 交集的集合，返回 {@link LinkedHashSet}
	 * @since 5.3.9
	 */
	@SafeVarargs
	public static <T> Set<T> intersectionDistinct(Collection<T> coll1, Collection<T> coll2,
			Collection<T>... otherColls) {
		final Set<T> result;
		if (isNullOrEmpty(coll1) || isNullOrEmpty(coll2)) {
			// 有一个空集合就直接返回空
			return new LinkedHashSet<>();
		}
		else {
			result = new LinkedHashSet<>(coll1);
		}

		if (ArrayUtil.isNotEmpty(otherColls)) {
			for (Collection<T> otherColl : otherColls) {
				if (!isNullOrEmpty(otherColl)) {
					result.retainAll(otherColl);
				}
				else {
					// 有一个空集合就直接返回空
					return new LinkedHashSet<>();
				}
			}
		}

		result.retainAll(coll2);

		return result;
	}

	/**
	 * 两个集合的差集<br>
	 * 针对一个集合中存在多个相同元素的情况，计算两个集合中此元素的个数，保留两个集合中此元素个数差的个数<br>
	 * 例如：
	 *
	 * <pre>
	 *     disjunction([a, b, c, c, c], [a, b, c, c]) -》 [c]
	 *     disjunction([a, b], [])                    -》 [a, b]
	 *     disjunction([a, b, c], [b, c, d])          -》 [a, d]
	 * </pre> 任意一个集合为空，返回另一个集合<br>
	 * 两个集合无差集则返回空集合
	 * @param <T> 集合元素类型
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @return 差集的集合，返回 {@link ArrayList}
	 */
	public static <T> Collection<T> disjunction(Collection<T> coll1, Collection<T> coll2) {
		if (isNullOrEmpty(coll1)) {
			return coll2;
		}
		if (isNullOrEmpty(coll2)) {
			return coll1;
		}

		final List<T> result = new ArrayList<>();
		final Map<T, Integer> map1 = CollUtil.countMap(coll1);
		final Map<T, Integer> map2 = CollUtil.countMap(coll2);
		final Set<T> elts = Sets.newHashSet(coll2);
		elts.addAll(coll1);
		int m;
		for (T t : elts) {
			m = Math.abs(Convert.toInt(map1.get(t), 0) - Convert.toInt(map2.get(t), 0));
			for (int i = 0; i < m; i++) {
				result.add(t);
			}
		}
		return result;
	}

	/**
	 * 计算集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素，例如：
	 *
	 * <pre>
	 *     subtract([1,2,3,4],[2,3,4,5]) -》 [1]
	 * </pre>
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param <T> 元素类型
	 * @return 单差集
	 */
	public static <T> Collection<T> subtract(Collection<T> coll1, Collection<T> coll2) {
		Collection<T> result = ObjectUtil.clone(coll1);
		if (null == result) {
			result = CollUtil.create(coll1.getClass());
			result.addAll(coll1);
		}
		result.removeAll(coll2);
		return result;
	}

	/**
	 * 计算集合的单差集，即只返回【集合1】中有，但是【集合2】中没有的元素，例如：
	 *
	 * <pre>
	 *     subtractToList([1,2,3,4],[2,3,4,5]) -》 [1]
	 * </pre>
	 * @param coll1 集合1
	 * @param coll2 集合2
	 * @param <T> 元素类型
	 * @return 单差集
	 * @since 5.3.5
	 */
	public static <T> List<T> subtractToList(Collection<T> coll1, Collection<T> coll2) {

		if (isNullOrEmpty(coll1)) {
			return ListUtil.empty();
		}
		if (isNullOrEmpty(coll2)) {
			return ListUtil.list(true, coll1);
		}

		// 将被交数用链表储存，防止因为频繁扩容影响性能
		final List<T> result = new LinkedList<>();
		Set<T> set = new HashSet<>(coll2);
		for (T t : coll1) {
			if (false == set.contains(t)) {
				result.add(t);
			}
		}
		return result;
	}

}
