/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.foundation.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A basic copy on write HashMap.
 * <p>
 * If an instance is cloned then any methods invoked on the instance or clone that result
 * in state modification will result in copying of the state before modification.
 *
 * @author Paul.Sandoz@Oracle.Com
 * @author pavel.bucek@oracle.com
 * @author Bryan.Zhang
 */
public class CopyOnWriteHashMap<K, V> extends ConcurrentHashMap<K, V> {

	volatile ConcurrentHashMap<K, V> view;

	private ConcurrentHashMap<K, V> duplicate(ConcurrentHashMap<K, V> original) {
		// SUBTLETY: note that original.entrySet() grabs the entire contents of the
		// original Map in a
		// single call. This means that if the original map is Thread-safe or another
		// CopyOnWriteHashMap,
		// we can safely iterate over the list of entries.
		return new ConcurrentHashMap<>(original);
	}

	public CopyOnWriteHashMap(ConcurrentHashMap<K, V> that) {
		this.view = this.duplicate(that);
	}

	public CopyOnWriteHashMap() {
		this(new ConcurrentHashMap<>());
	}

	@Override
	public CopyOnWriteHashMap<K, V> clone() {
		return new CopyOnWriteHashMap(this.view);
	}

	/*
	 * ********************** READ-ONLY OPERATIONS
	 **********************/

	@Override
	public int size() {
		return this.view.size();
	}

	@Override
	public boolean isEmpty() {
		return this.view.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.view.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.view.containsValue(value);
	}

	@Override
	public V get(Object key) {
		return this.view.get(key);
	}

	@Override
	public KeySetView<K, V> keySet() {
		return this.view.keySet();
	}

	@Override
	public Collection<V> values() {
		return this.view.values();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return this.view.entrySet();
	}

	@Override
	public String toString() {
		return this.view.toString();
	}

	/*
	 * ********************** UPDATING OPERATIONS
	 *
	 * These operations all follow a common pattern:
	 *
	 * 1. Create a copy of the existing view. 2. Update the copy. 3. Perform a volatile
	 * write to replace the existing view.
	 *
	 * Note that if you are not concerned about lost updates, you could dispense with the
	 * synchronization entirely.
	 **********************/

	@Override
	public V put(K key, V value) {
		synchronized (this) {
			ConcurrentHashMap<K, V> newCore = this.duplicate(this.view);
			V result = newCore.put(key, value);
			this.view = newCore; // volatile write
			return result;
		}
	}

	@Override
	public V remove(Object key) {
		synchronized (this) {
			ConcurrentHashMap<K, V> newCore = this.duplicate(this.view);
			V result = newCore.remove(key);
			this.view = newCore; // volatile write
			return result;
		}
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> t) {
		synchronized (this) {
			ConcurrentHashMap<K, V> newCore = this.duplicate(this.view);
			newCore.putAll(t);
			this.view = newCore; // volatile write
		}
	}

	@Override
	public void clear() {
		synchronized (this) {
			this.view = new ConcurrentHashMap<>(); // volatile write
		}
	}

}
