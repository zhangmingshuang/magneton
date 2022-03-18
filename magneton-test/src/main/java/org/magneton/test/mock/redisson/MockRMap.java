package org.magneton.test.mock.redisson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.magneton.test.mock.UnsupportedException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.redisson.api.ObjectListener;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.mapreduce.RMapReduce;
import org.redisson.client.codec.Codec;

/**
 * .
 *
 * @author zhangmsh 2021/11/19
 * @since 2.0.3
 */
public class MockRMap<K, V> implements RMap<K, V> {

	private Map<K, V> map = Maps.newConcurrentMap();

	@Override
	public void loadAll(boolean replaceExistingValues, int parallelism) {

	}

	@Override
	public void loadAll(Set<? extends K> keys, boolean replaceExistingValues, int parallelism) {

	}

	@Override
	public V get(Object key) {
		return this.map.get(key);
	}

	@Override
	public V put(K key, V value) {
		return this.map.put(key, value);
	}

	@Override
	public V putIfAbsent(K key, V value) {
		return this.map.putIfAbsent(key, value);
	}

	@Override
	public V putIfExists(K key, V value) {
		return this.map.replace(key, value);
	}

	@Override
	public Set<K> randomKeys(int count) {
		Set<K> ks = this.map.keySet();
		ArrayList<K> list = Lists.newArrayList(ks);
		Collections.shuffle(list);
		count = Math.min(count, list.size());
		return Sets.newHashSet(list.subList(0, count));
	}

	@Override
	public Map<K, V> randomEntries(int count) {
		Set<Entry<K, V>> entries = this.map.entrySet();
		ArrayList<Entry<K, V>> list = Lists.newArrayList(entries);
		Collections.shuffle(list);
		count = Math.min(count, list.size());
		return list.subList(0, count).stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	@Override
	public <KOut, VOut> RMapReduce<K, V, KOut, VOut> mapReduce() {
		throw UnsupportedException.INSTANCE;
	}

	@Override
	public RCountDownLatch getCountDownLatch(K key) {
		return null;
	}

	@Override
	public RPermitExpirableSemaphore getPermitExpirableSemaphore(K key) {
		return null;
	}

	@Override
	public RSemaphore getSemaphore(K key) {
		return null;
	}

	@Override
	public RLock getFairLock(K key) {
		return null;
	}

	@Override
	public RReadWriteLock getReadWriteLock(K key) {
		return null;
	}

	@Override
	public RLock getLock(K key) {
		return null;
	}

	@Override
	public int valueSize(K key) {
		return 0;
	}

	@Override
	public V addAndGet(K key, Number delta) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public V remove(Object key) {
		return null;
	}

	@Override
	public V replace(K key, V value) {
		return null;
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		return false;
	}

	@Override
	public boolean remove(Object key, Object value) {
		return false;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {

	}

	@Override
	public void clear() {

	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map, int batchSize) {

	}

	@Override
	public Map<K, V> getAll(Set<K> keys) {
		return null;
	}

	@Override
	public long fastRemove(K... keys) {
		return 0;
	}

	@Override
	public boolean fastPut(K key, V value) {
		return false;
	}

	@Override
	public boolean fastReplace(K key, V value) {
		return false;
	}

	@Override
	public boolean fastPutIfAbsent(K key, V value) {
		return false;
	}

	@Override
	public boolean fastPutIfExists(K key, V value) {
		return false;
	}

	@Override
	public Set<K> readAllKeySet() {
		return null;
	}

	@Override
	public Collection<V> readAllValues() {
		return null;
	}

	@Override
	public Set<Entry<K, V>> readAllEntrySet() {
		return null;
	}

	@Override
	public Map<K, V> readAllMap() {
		return null;
	}

	@Override
	public Set<K> keySet() {
		return null;
	}

	@Override
	public Set<K> keySet(int count) {
		return null;
	}

	@Override
	public Set<K> keySet(String pattern, int count) {
		return null;
	}

	@Override
	public Set<K> keySet(String pattern) {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}

	@Override
	public Collection<V> values(String keyPattern) {
		return null;
	}

	@Override
	public Collection<V> values(String keyPattern, int count) {
		return null;
	}

	@Override
	public Collection<V> values(int count) {
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet(String keyPattern) {
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet(String keyPattern, int count) {
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet(int count) {
		return null;
	}

	@Override
	public boolean expire(long timeToLive, TimeUnit timeUnit) {
		return false;
	}

	@Override
	public boolean expireAt(long timestamp) {
		return false;
	}

	@Override
	public boolean expireAt(Date timestamp) {
		return false;
	}

	@Override
	public boolean expire(Instant instant) {
		return false;
	}

	@Override
	public boolean clearExpire() {
		return false;
	}

	@Override
	public long remainTimeToLive() {
		return 0;
	}

	@Override
	public RFuture<V> mergeAsync(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		return null;
	}

	@Override
	public RFuture<V> computeAsync(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return null;
	}

	@Override
	public RFuture<V> computeIfAbsentAsync(K key, Function<? super K, ? extends V> mappingFunction) {
		return null;
	}

	@Override
	public RFuture<V> computeIfPresentAsync(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
		return null;
	}

	@Override
	public RFuture<Void> loadAllAsync(boolean replaceExistingValues, int parallelism) {
		return null;
	}

	@Override
	public RFuture<Void> loadAllAsync(Set<? extends K> keys, boolean replaceExistingValues, int parallelism) {
		return null;
	}

	@Override
	public RFuture<Integer> valueSizeAsync(K key) {
		return null;
	}

	@Override
	public RFuture<Map<K, V>> getAllAsync(Set<K> keys) {
		return null;
	}

	@Override
	public RFuture<Void> putAllAsync(Map<? extends K, ? extends V> map) {
		return null;
	}

	@Override
	public RFuture<Void> putAllAsync(Map<? extends K, ? extends V> map, int batchSize) {
		return null;
	}

	@Override
	public RFuture<Set<K>> randomKeysAsync(int count) {
		return null;
	}

	@Override
	public RFuture<Map<K, V>> randomEntriesAsync(int count) {
		return null;
	}

	@Override
	public RFuture<V> addAndGetAsync(K key, Number delta) {
		return null;
	}

	@Override
	public RFuture<Boolean> containsValueAsync(Object value) {
		return null;
	}

	@Override
	public RFuture<Boolean> containsKeyAsync(Object key) {
		return null;
	}

	@Override
	public RFuture<Integer> sizeAsync() {
		return null;
	}

	@Override
	public RFuture<Long> fastRemoveAsync(K... keys) {
		return null;
	}

	@Override
	public RFuture<Boolean> fastPutAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<Boolean> fastReplaceAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<Boolean> fastPutIfAbsentAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<Boolean> fastPutIfExistsAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<Set<K>> readAllKeySetAsync() {
		return null;
	}

	@Override
	public RFuture<Collection<V>> readAllValuesAsync() {
		return null;
	}

	@Override
	public RFuture<Set<Entry<K, V>>> readAllEntrySetAsync() {
		return null;
	}

	@Override
	public RFuture<Map<K, V>> readAllMapAsync() {
		return null;
	}

	@Override
	public RFuture<V> getAsync(K key) {
		return null;
	}

	@Override
	public RFuture<V> putAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<V> removeAsync(K key) {
		return null;
	}

	@Override
	public RFuture<V> replaceAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<Boolean> replaceAsync(K key, V oldValue, V newValue) {
		return null;
	}

	@Override
	public RFuture<Boolean> removeAsync(Object key, Object value) {
		return null;
	}

	@Override
	public RFuture<V> putIfAbsentAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<V> putIfExistsAsync(K key, V value) {
		return null;
	}

	@Override
	public RFuture<Boolean> expireAsync(long timeToLive, TimeUnit timeUnit) {
		return null;
	}

	@Override
	public RFuture<Boolean> expireAtAsync(Date timestamp) {
		return null;
	}

	@Override
	public RFuture<Boolean> expireAtAsync(long timestamp) {
		return null;
	}

	@Override
	public RFuture<Boolean> expireAsync(Instant instant) {
		return null;
	}

	@Override
	public RFuture<Boolean> clearExpireAsync() {
		return null;
	}

	@Override
	public RFuture<Long> remainTimeToLiveAsync() {
		return null;
	}

	@Override
	public Long getIdleTime() {
		return null;
	}

	@Override
	public long sizeInMemory() {
		return 0;
	}

	@Override
	public void restore(byte[] state) {

	}

	@Override
	public void restore(byte[] state, long timeToLive, TimeUnit timeUnit) {

	}

	@Override
	public void restoreAndReplace(byte[] state) {

	}

	@Override
	public void restoreAndReplace(byte[] state, long timeToLive, TimeUnit timeUnit) {

	}

	@Override
	public byte[] dump() {
		return new byte[0];
	}

	@Override
	public boolean touch() {
		return false;
	}

	@Override
	public void migrate(String host, int port, int database, long timeout) {

	}

	@Override
	public void copy(String host, int port, int database, long timeout) {

	}

	@Override
	public boolean move(int database) {
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean unlink() {
		return false;
	}

	@Override
	public void rename(String newName) {

	}

	@Override
	public boolean renamenx(String newName) {
		return false;
	}

	@Override
	public boolean isExists() {
		return false;
	}

	@Override
	public Codec getCodec() {
		return null;
	}

	@Override
	public int addListener(ObjectListener listener) {
		return 0;
	}

	@Override
	public void removeListener(int listenerId) {

	}

	@Override
	public RFuture<Long> getIdleTimeAsync() {
		return null;
	}

	@Override
	public RFuture<Long> sizeInMemoryAsync() {
		return null;
	}

	@Override
	public RFuture<Void> restoreAsync(byte[] state) {
		return null;
	}

	@Override
	public RFuture<Void> restoreAsync(byte[] state, long timeToLive, TimeUnit timeUnit) {
		return null;
	}

	@Override
	public RFuture<Void> restoreAndReplaceAsync(byte[] state) {
		return null;
	}

	@Override
	public RFuture<Void> restoreAndReplaceAsync(byte[] state, long timeToLive, TimeUnit timeUnit) {
		return null;
	}

	@Override
	public RFuture<byte[]> dumpAsync() {
		return null;
	}

	@Override
	public RFuture<Boolean> touchAsync() {
		return null;
	}

	@Override
	public RFuture<Void> migrateAsync(String host, int port, int database, long timeout) {
		return null;
	}

	@Override
	public RFuture<Void> copyAsync(String host, int port, int database, long timeout) {
		return null;
	}

	@Override
	public RFuture<Boolean> moveAsync(int database) {
		return null;
	}

	@Override
	public RFuture<Boolean> deleteAsync() {
		return null;
	}

	@Override
	public RFuture<Boolean> unlinkAsync() {
		return null;
	}

	@Override
	public RFuture<Void> renameAsync(String newName) {
		return null;
	}

	@Override
	public RFuture<Boolean> renamenxAsync(String newName) {
		return null;
	}

	@Override
	public RFuture<Boolean> isExistsAsync() {
		return null;
	}

	@Override
	public RFuture<Integer> addListenerAsync(ObjectListener listener) {
		return null;
	}

	@Override
	public RFuture<Void> removeListenerAsync(int listenerId) {
		return null;
	}

}
