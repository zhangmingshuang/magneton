package org.magneton.module.distributed.cache.ops;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Sorted Set Operations.
 *
 * @author zhangmsh 2022/5/21
 * @since 1.0.0
 */
public interface SortedSetOps {

	/**
	 * 添加元素
	 * @param key 集合名称
	 * @param value 元素
	 * @param score 元素分数
	 * @return 如果有元素添加成功，返回true。如果元素已经存在或者添加失败，返回false
	 */
	<V> boolean add(String key, V value, double score);

	/**
	 * 添加元素
	 * @param key 集合名称
	 * @param values 元素
	 * @param <V> V
	 * @return 添加成功的数量，如果添加的元素已经存在，则不计数。
	 */
	<V> int addAll(String key, Map<V, Double> values);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startIndex 开始位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param endIndex 结束位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Collection<V> valueRange(String key, int startIndex, int endIndex);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startIndex 开始位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param endIndex 结束位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Collection<V> valueRangeReversed(String key, int startIndex, int endIndex);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startIndex 开始位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param endIndex 结束位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Map<V, Double> valueRangeWithScore(String key, int startIndex, int endIndex);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startIndex 开始位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param endIndex 结束位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Map<V, Double> valueRangeReversedWithScore(String key, int startIndex, int endIndex);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数，包含的
	 * @param endScore 结束分数，包含的
	 * @param <V> V
	 * @return 范围内的数据
	 */
	default <V> Collection<V> valueRange(String key, double startScore, double endScore) {
		return this.valueRange(key, startScore, true, endScore, true);
	}

	default <V> Collection<V> valueRangeReversed(String key, double startScore, double endScore) {
		return this.valueRangeReversed(key, startScore, true, endScore, true);
	}

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数，包含的
	 * @param endScore 结束分数，包含的
	 * @param <V> V
	 * @return 范围内的数据
	 */
	default <V> Map<V, Double> valueRangeWithScore(String key, double startScore, double endScore) {
		return this.valueRangeWithScore(key, startScore, true, endScore, true);
	}

	/**
	 * 获取范围内的数据, 倒序
	 * @param key 集合名称
	 * @param startScore 开始分数，包含的
	 * @param endScore 结束分数，包含的
	 * @param <V> V
	 * @return 范围内的数据
	 */
	default <V> Map<V, Double> valueRangeReversedWithScore(String key, double startScore, double endScore) {
		return this.valueRangeReversedWithScore(key, startScore, true, endScore, true);
	}

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Collection<V> valueRange(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive);

	/**
	 * 获取范围内的数据, 倒序
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Collection<V> valueRangeReversed(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Map<V, Double> valueRangeWithScore(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive);

	/**
	 * 获取范围内的数据, 倒序
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @param <V> V
	 * @return 范围内的数据
	 */
	<V> Map<V, Double> valueRangeReversedWithScore(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数，包含的
	 * @param endScore 结束分数，包含的
	 * @param offset 偏移量
	 * @param count 数量
	 * @param <V> V
	 * @return 范围内的数据
	 */
	default <V> Collection<V> valueRange(String key, double startScore, double endScore, int offset, int count) {
		return this.valueRange(key, startScore, true, endScore, true, offset, count);
	}

	default <V> Collection<V> valueRangeReversed(String key, double startScore, double endScore, int offset,
			int count) {
		return this.valueRangeReversed(key, startScore, true, endScore, true, offset, count);
	}

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数，包含的
	 * @param endScore 结束分数，包含的
	 * @param offset 偏移量
	 * @param count 数量
	 * @param <V> V
	 * @return 范围内的数据
	 */
	default <V> Map<V, Double> valueRangeWithScore(String key, double startScore, double endScore, int offset,
			int count) {
		return this.valueRangeWithScore(key, startScore, true, endScore, true, offset, count);
	}

	default <V> Map<V, Double> valueRangeReversedWithScore(String key, double startScore, double endScore, int offset,
			int count) {
		return this.valueRangeReversedWithScore(key, startScore, true, endScore, true, offset, count);
	}

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数，包含的
	 * @param endScore 结束分数，包含的
	 * @param count 数量
	 * @param <V> V
	 * @return 范围内的数据
	 */
	default <V> Collection<V> valueRange(String key, double startScore, double endScore, int count) {
		return this.valueRange(key, startScore, endScore, 0, count);
	}

	default <V> Collection<V> valueRangeReversed(String key, double startScore, double endScore, int count) {
		return this.valueRangeReversed(key, startScore, endScore, 0, count);
	}

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @param offset 偏移量
	 * @param count 数量
	 * @param <V> V
	 * @return 范围内的数据
	 */
	@SuppressWarnings("MethodWithTooManyParameters")
	<V> Collection<V> valueRange(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive, int offset, int count);

	@SuppressWarnings("MethodWithTooManyParameters")
	<V> Collection<V> valueRangeReversed(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive, int offset, int count);

	/**
	 * 获取范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @param offset 偏移量
	 * @param count 数量
	 * @param <V> V
	 * @return 范围内的数据
	 */
	@SuppressWarnings("MethodWithTooManyParameters")
	<V> Map<V, Double> valueRangeWithScore(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive, int offset, int count);

	@SuppressWarnings("MethodWithTooManyParameters")
	<V> Map<V, Double> valueRangeReversedWithScore(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive, int offset, int count);

	/**
	 * 取得Scope
	 * @param key 集合名称
	 * @param values 元素
	 * @return 元素对应的Scope
	 */
	<V> List<Double> getScope(String key, List<V> values);

	/**
	 * 取得Scope
	 * @param key 集合名称
	 * @param value 元素
	 * @return 元素的Scope，如果元素不存在，返回null
	 */
	@Nullable
	default <V> Double getScope(String key, V value) {
		List<Double> scopes = this.getScope(key, Collections.singletonList(value));
		if (!scopes.isEmpty()) {
			return scopes.get(0);
		}
		return null;
	}

	/**
	 * 移除元素
	 * @param key 集合名称
	 * @param value 元素
	 * @param <V> V
	 * @return 如果要移除的元素存在并移除成功返回 {@code true}，如果不存在或者移除失败返回 {@code false}
	 */
	<V> boolean remove(String key, V value);

	/**
	 * 移除元素
	 * @param key 集合名称
	 * @param values 元素
	 * @param <V> V
	 * @return 如果要移除的元素列表中有一个移除成功则返回 {@code true}，如果全部移除失败则返回 {@code false}
	 */
	<V> boolean remove(String key, Collection<V> values);

	/**
	 * 移除范围内的数据
	 * @param key 集合名称
	 * @param startIndex 开始位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @param endIndex 结束位置，以0为起点，可以使用负数，-1表示最后一个成员，-2表示倒数第二个成员，以此类推。
	 * @return 移除的数量
	 */
	int removeRange(String key, int startIndex, int endIndex);

	/**
	 * 移除范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数，包含
	 * @param endScore 结束分数，包含
	 * @return 范围内的数据
	 */
	default int removeRangeWithScore(String key, double startScore, double endScore) {
		return this.removeRangeWithScore(key, startScore, true, endScore, true);
	}

	/**
	 * 移除范围内的数据
	 * @param key 集合名称
	 * @param startScore 开始分数
	 * @param startScoreInclusive 是否包含开始分数
	 * @param endScore 结束分数
	 * @param endScoreInclusive 是否包含结束的分数
	 * @return 范围内的数据
	 */
	int removeRangeWithScore(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive);

}
