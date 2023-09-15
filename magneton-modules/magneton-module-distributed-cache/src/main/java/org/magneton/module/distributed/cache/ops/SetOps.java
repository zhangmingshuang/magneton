package org.magneton.module.distributed.cache.ops;

import java.util.Collection;
import java.util.Set;

/**
 * Set Operations.
 *
 * @author zhangmsh 2022/5/4
 * @since 1.0.0
 */
public interface SetOps {

	/**
	 * 添加元素
	 * @param set 集合名称
	 * @param values 元素
	 * @return 如果有元素添加成功，返回true。如果所有的元素均已经存在，则添加时没有改变原集合，则否则返回false
	 */
	<V> boolean add(String set, V... values);

	/**
	 * 添加元素
	 * @param set 集合名称
	 * @param values 元素
	 * @return 如果有元素添加成功，返回true。如果所有的元素均已经存在，则添加时没有改变原集合，则否则返回false
	 */
	<V> boolean add(String set, Collection<V> values);

	/**
	 * 获取集合列表
	 * @param set 集合名称
	 * @return 集合列表
	 */
	<V> Set<V> get(String set);

	/**
	 * 获取集合长长度
	 * @param set 集合名称
	 * @return 集合长度
	 */
	int size(String set);

	/**
	 * 判断集合是否为空
	 * @param set 集合名称
	 * @return 集合是否为空
	 */
	boolean isEmpty(String set);

	/**
	 * 判断集合是否包含指定元素
	 * @param set 集合名称
	 * @param value 元素
	 * @return 如果存在则返回true，否则返回false
	 */
	<V> boolean contains(String set, V value);

	/**
	 * 移除集合中的某个原素
	 * @param set 集合名称
	 * @param value 元素
	 * @return 如果元素存在并移除成功，则返回true，否则返回false
	 */
	<V> boolean remove(String set, V value);

}
