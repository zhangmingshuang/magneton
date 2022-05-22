package org.magneton.module.distributed.cache.redis;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.magneton.module.distributed.cache.ops.SortedSetOps;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.protocol.ScoredEntry;

/**
 * @author zhangmsh 2022/5/21
 * @since 1.0.0
 */
public class ResissonSortedSetOps extends AbstractRedissonOps implements SortedSetOps {

	protected ResissonSortedSetOps(RedissonClient redissonClient) {
		super(redissonClient);
	}

	@Override
	public <V> boolean add(String key, V value, double score) {
		Preconditions.checkNotNull(key, "key");
		Preconditions.checkNotNull(value, "value");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.add(score, value);
	}

	@Override
	public <V> int addAll(String key, Map<V, Double> values) {
		Preconditions.checkNotNull(key, "key");
		Preconditions.checkNotNull(values, "values");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.addAll(values);
	}

	@Override
	public <V> Collection<V> valueRange(String key, int startIndex, int endIndex) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.valueRange(startIndex, endIndex);
	}

	@Override
	public <V> Collection<V> valueRangeReversed(String key, int startIndex, int endIndex) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.valueRangeReversed(startIndex, endIndex);
	}

	@Override
	public <V> Map<V, Double> valueRangeWithScore(String key, int startIndex, int endIndex) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		Collection<ScoredEntry<V>> scoredEntries = scoredSortedSet.entryRange(startIndex, endIndex);
		return this.toScoreMap(scoredEntries);
	}

	@Override
	public <V> Map<V, Double> valueRangeReversedWithScore(String key, int startIndex, int endIndex) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		Collection<ScoredEntry<V>> scoredEntries = scoredSortedSet.entryRangeReversed(startIndex, endIndex);
		return this.toScoreMap(scoredEntries);
	}

	@Override
	public <V> Collection<V> valueRange(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.valueRange(startScore, startScoreInclusive, endScore, endScoreInclusive);
	}

	@Override
	public <V> Collection<V> valueRangeReversed(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.valueRangeReversed(startScore, startScoreInclusive, endScore, endScoreInclusive);
	}

	@Override
	public <V> Collection<V> valueRange(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive, int offset, int count) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.valueRange(startScore, startScoreInclusive, endScore, endScoreInclusive, offset, count);
	}

	@Override
	public <V> Collection<V> valueRangeReversed(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive, int offset, int count) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.valueRangeReversed(startScore, startScoreInclusive, endScore, endScoreInclusive, offset,
				count);
	}

	@Override
	public <V> Map<V, Double> valueRangeWithScore(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		Collection<ScoredEntry<V>> scoredEntries = scoredSortedSet.entryRange(startScore, startScoreInclusive, endScore,
				endScoreInclusive);
		return this.toScoreMap(scoredEntries);
	}

	@Override
	public <V> Map<V, Double> valueRangeReversedWithScore(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		Collection<ScoredEntry<V>> scoredEntries = scoredSortedSet.entryRangeReversed(startScore, startScoreInclusive,
				endScore, endScoreInclusive);
		return this.toScoreMap(scoredEntries);
	}

	@Override
	public <V> Map<V, Double> valueRangeWithScore(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive, int offset, int count) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		Collection<ScoredEntry<V>> scoredEntries = scoredSortedSet.entryRange(startScore, startScoreInclusive, endScore,
				endScoreInclusive, offset, count);
		return this.toScoreMap(scoredEntries);
	}

	@Override
	public <V> Map<V, Double> valueRangeReversedWithScore(String key, double startScore, boolean startScoreInclusive,
			double endScore, boolean endScoreInclusive, int offset, int count) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		Collection<ScoredEntry<V>> scoredEntries = scoredSortedSet.entryRangeReversed(startScore, startScoreInclusive,
				endScore, endScoreInclusive, offset, count);
		return this.toScoreMap(scoredEntries);
	}

	@Override
	public <V> List<Double> getScope(String key, List<V> values) {
		Preconditions.checkNotNull(key, "key");
		Preconditions.checkNotNull(values, "value");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.getScore(values);
	}

	@Override
	public <V> boolean remove(String key, V value) {
		Preconditions.checkNotNull(key, "key");
		Preconditions.checkNotNull(value, "value");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.remove(value);
	}

	@Override
	public <V> boolean remove(String key, Collection<V> values) {
		Preconditions.checkNotNull(key, "key");
		Preconditions.checkNotNull(values, "values");
		RScoredSortedSet<V> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.removeAll(values);
	}

	@Override
	public int removeRange(String key, int startIndex, int endIndex) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<Object> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.removeRangeByRank(startIndex, endIndex);
	}

	@Override
	public int removeRangeWithScore(String key, double startScore, boolean startScoreInclusive, double endScore,
			boolean endScoreInclusive) {
		Preconditions.checkNotNull(key, "key");
		RScoredSortedSet<Object> scoredSortedSet = this.redissonClient.getScoredSortedSet(key);
		return scoredSortedSet.removeRangeByScore(startScore, startScoreInclusive, endScore, endScoreInclusive);
	}

	private <V> Map<V, Double> toScoreMap(Collection<ScoredEntry<V>> scoredEntries) {
		if (scoredEntries.isEmpty()) {
			return Collections.emptyMap();
		}
		Map<V, Double> result = Maps.newHashMapWithExpectedSize(scoredEntries.size());
		for (ScoredEntry<V> scoredEntry : scoredEntries) {
			result.put(scoredEntry.getValue(), scoredEntry.getScore());
		}
		return result;
	}

}
