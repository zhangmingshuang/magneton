package org.magneton.cache.ops;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;

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
   * <p>比如：
   *
   * <pre>{@code
   * #add("listName", "a", "b", "c")
   * 此时，则列表元素为: "a", "b", "c"
   * 再次调用添加， #add("listName", "d")
   * 则列表元素为: "a", "b", "c", "d"
   * }</pre>
   *
   * @param list 列表名称
   * @param values 要添加的数据
   * @return 添加条数
   */
  default long add(String list, String... values) {
    return this.add(list, Arrays.asList(values));
  }
  /**
   * 添加到列表尾部
   *
   * <p>比如：
   *
   * <pre>{@code
   * List<String> values = "a", "b", "c"
   * #add("listName", values)
   * 此时，则列表元素为: "a", "b", "c"
   * 再次调用添加， #add("listName", "d")
   * 则列表元素为: "a", "b", "c", "d"
   *
   * }</pre>
   *
   * @param list 列表名称
   * @param values 要添加的数据
   * @return 添加条数
   */
  long add(String list, List<String> values);
  /**
   * 添加到列表头部
   *
   * <p>比如：
   *
   * <pre>{@code
   * #add("listName", "a", "b", "c")
   * 此时，则列表元素为: "c", "b", "a"
   * 再次调用添加， #add("listName", "d")
   * 则列表元素为: "d", "c", "b", "a"
   * }</pre>
   *
   * @param list 列表名称
   * @param values 要添加的数据
   * @return 添加条数
   */
  default long addAtHead(String list, String... values) {
    return this.addAtHead(list, Arrays.asList(values));
  }
  /**
   * 添加到列表头部
   *
   * <p>比如：
   *
   * <pre>{@code
   * List<String> values = "a", "b", "c"
   * #add("listName", values)
   * 此时，则列表元素为: "c", "b", "a"
   * 再次调用添加， #add("listName", "d")
   * 则列表元素为: "d", "c", "b", "a"
   * }</pre>
   *
   * @param list 列表名称
   * @param values 要添加的数据
   * @return 添加条数
   */
  long addAtHead(String list, List<String> values);

  /**
   * 获取列表范围数据
   *
   * <p>{@code start} 和 {@code end} 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推。
   *
   * <p>也可以是负数，表示偏移量是从list尾部开始计数。 例如， -1 表示列表的最后一个元素，-2 是倒数第二个，以此类推。
   *
   * <p>如果start比list的尾部下标大的时候，会返回一个空列表。 如果stop比list的实际尾部大的时候，当它是最后一个元素的下标。
   *
   * <p>使用示例：
   *
   * <pre>{@code
   * list={"a", "b", "c"}
   * #range(list, 0, 0) 表示获得第一个元素，即"a"
   * #range(list, 0, 1) 表示获取第0~第1个元素，即"a","b"
   * #range(list, -1, -1) 表示获取最后一个元素，即"c"
   * #range(list, -2, -1) 表示获取从最后面依次获取元素，即"b","c"
   * }</pre>
   *
   * @param list 列表名称
   * @param start 开始位置
   * @param end 结束位置
   * @return 在 [{@code start} , {@code end} ]范围内的数据
   */
  List<String> range(String list, long start, long end);

  /**
   * 获取列表的大小
   *
   * @param list 列表名称
   * @return 列表大小，如果列表不存在返回 {@code 0}
   */
  long size(String list);

  /**
   * 根据下标获取列表中的元素
   *
   * @param list 列表名称
   * @param index 位置下标，从0开始。0表示第一个元素，1表示第2个元素，以此类推。
   *     <p>可以使用负数下标，-1表示最后一个元素，-2表示最后第二个元素，以此类推。
   * @return 名称为 {@code list} 的列表中，下标为 {@code index} 的值。如果 {@code index} 不在列表区间，则返回 {@code null}.
   */
  @Nullable
  String get(String list, long index);

  /**
   * 删除列表中的特定值
   *
   * @param list 列表名称
   * @param value 要删除的值
   * @return 删除的数量
   */
  long remove(String list, String value);
}
