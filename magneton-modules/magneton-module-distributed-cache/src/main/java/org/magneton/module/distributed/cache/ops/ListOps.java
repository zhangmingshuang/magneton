package org.magneton.module.distributed.cache.ops;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import org.magneton.core.collect.Collections;
import org.magneton.foundation.util.Arrays;

/**
 * .
 *
 * @author zhangmsh 2021/6/24
 * @since 1.0.0
 */
public interface ListOps {

	/**
	 * 添加到列表尾部
	 *
	 * <p>
	 * 比如：
	 *
	 * <pre>{@code
	 * #add("listName", "a", "b", "c")
	 * 此时，则列表元素为: "a", "b", "c"
	 * 再次调用添加， #add("listName", "d")
	 * 则列表元素为: "a", "b", "c", "d"
	 * }</pre>
	 * @param list 列表名称
	 * @param values 要添加的数据
	 * @return 操作完成之后当前列表集合中的长度
	 */
	default <V> boolean add(String list, V... values) {
		return this.add(list, Arrays.asList(values));
	}

	/**
	 * 添加到列表尾部
	 *
	 * <p>
	 * 比如：
	 *
	 * <pre>{@code
	 * List<String> values = "a", "b", "c"
	 * #add("listName", values)
	 * 此时，则列表元素为: "a", "b", "c"
	 * 再次调用添加， #add("listName", "d")
	 * 则列表元素为: "a", "b", "c", "d"
	 *
	 * }</pre>
	 * @param list 列表名称
	 * @param values 要添加的数据
	 * @return 操作完成之后当前列表集合中的长度
	 */
	<V> boolean add(String list, List<V> values);

	/**
	 * 添加到列表头部
	 *
	 * <p>
	 * 比如：
	 *
	 * <pre>{@code
	 * #add("listName", "a", "b", "c")
	 * 此时，则列表元素为: "c", "b", "a"
	 * 再次调用添加， #add("listName", "d")
	 * 则列表元素为: "d", "c", "b", "a"
	 * }</pre>
	 * @param list 列表名称
	 * @param values 要添加的数据
	 * @return 操作完成之后当前列表集合中的长度
	 */
	<V> void addAtHead(String list, V... values);

	/**
	 * 添加到列表头部
	 *
	 * <p>
	 * 比如：
	 *
	 * <pre>{@code
	 * List<String> values = "a", "b", "c"
	 * #add("listName", values)
	 * 此时，则列表元素为: "c", "b", "a"
	 * 再次调用添加， #add("listName", "d")
	 * 则列表元素为: "d", "c", "b", "a"
	 * }</pre>
	 * @param list 列表名称
	 * @param values 要添加的数据
	 * @return 操作完成之后当前列表集合中的长度
	 */
	default <V> void addAtHead(String list, Collection<V> values) {
		if (Collections.isNullOrEmpty(values)) {
			return;
		}
		V obj = (V) values.stream().findFirst();
		addAtHead(list, Arrays.toArray(values, obj.getClass()));
	}

	/**
	 * 获取列表数据
	 *
	 * <b>索引范围说明：</b>如果有一个100长度的列表，则查找范围0，10的数据，即返回11个数据。
	 * @param list 列表名称
	 * @param start 开始索引。从0开始。即0表示第一个元素。支持负数，表示倒数，-1表示倒数第一个元素，-2表示倒数第二个。
	 * 如果开始索引超过列表大小，则直接返回一个空列表。
	 * @param end 结束索引。支持负数，表示倒数，-1表示倒数第一个元素，-2表示倒数第二个。
	 * 该位置会被包括在内。如果超过列表长度，则表示直到当前列表的最后一个元素，超出的被忽略。
	 * @return 选择范围内的列表数据。
	 */
	<V> List<V> range(String list, int start, int end);

	/**
	 * 获取列表的大小
	 * @param list 列表名称
	 * @return 列表大小，如果列表不存在返回 {@code 0}
	 */
	long size(String list);

	/**
	 * 根据下标获取列表中的元素
	 * @param list 列表名称
	 * @param index 位置下标，从0开始。0表示第一个元素，1表示第2个元素。 -1表示倒数最后一个元素，-2表示倒数最后第二个元素，以此类推。
	 * 如果下标位置不正确，如超出，则返回 {@code  null}
	 * @return 名称为 {@code list} 的列表中，下标为 {@code index} 的值。如果 {@code index} 不在列表区间，则返回
	 * {@code null}.
	 */
	@Nullable
	<V> V get(String list, int index);

	/**
	 * 删除列表中的特定值
	 * @param list 列表名称
	 * @param value 要删除的值，列表中所有匹配的值将被删除。
	 * @return 删除的数量
	 */
	<V> boolean remove(String list, V value);

}
