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

package org.magneton.monitor.core;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 全局监控上下文
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
@Slf4j
public class GMC {

	private static final AtomicLong ID = new AtomicLong(0);

	/**
	 * 全局变量
	 */
	private static final Map<String, String> GLOBAL_CONTEXT = Maps.newConcurrentMap();

	/**
	 * 本地变量
	 */
	private static final TransmittableThreadLocal<Map<String, String>> CONTEXT = new TransmittableThreadLocal<>();

	/**
	 * Hostname
	 */
	private static final Map<String, String> BASE_DATA = Maps.newHashMapWithExpectedSize(2);

	static {
		try {
			InetAddress ia = InetAddress.getLocalHost();
			BASE_DATA.put("hostname", ia.getCanonicalHostName());
			BASE_DATA.put("hostIp", ia.getHostAddress());
			GLOBAL_CONTEXT.putAll(BASE_DATA);
		}
		catch (UnknownHostException e) {
			log.error("get local host error", e);
		}
	}

	private GMC() {
		// private
	}

	/**
	 * 设置全局上下文
	 * @param key 变量Key
	 * @param value 变量值
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	@Nullable
	public static Object putGlobal(String key, String value) {
		Preconditions.checkNotNull(key, "key must not be null");
		Preconditions.checkNotNull(value, "the key %s of value must not be null", key);
		return GLOBAL_CONTEXT.put(key, value);
	}

	/**
	 * 设置上下文
	 * @param key 变量Key
	 * @param value 变量值
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	@Nullable
	public static Object put(String key, String value) {
		Preconditions.checkNotNull(key, "key must not be null");
		Preconditions.checkNotNull(value, "the key %s of value must not be null", key);

		Map<String, String> body = CONTEXT.get();
		if (body == null) {
			body = new ConcurrentHashMap<>(16);
			CONTEXT.set(body);
		}
		return body.put(key, value);
	}

	/**
	 * 如果key不存在，则设置全局上下文
	 * @param key 变量Key
	 * @param value 变量值
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	public static Object putGlobalIfAbsent(String key, String value) {
		Preconditions.checkNotNull(key, "key must not be null");
		Preconditions.checkNotNull(value, "value must not be null");

		return GLOBAL_CONTEXT.putIfAbsent(key, value);
	}

	/**
	 * 如果key不存在，则设置上下文
	 * @param key 变量Key
	 * @param value 变量值
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	@Nullable
	public static Object putIfAbsent(String key, String value) {
		Preconditions.checkNotNull(key, "key must not be null");
		Preconditions.checkNotNull(value, "value must not be null");

		Map<String, String> body = CONTEXT.get();
		if (body == null) {
			body = new ConcurrentHashMap<>(16);
			CONTEXT.set(body);
		}
		return body.putIfAbsent(key, value);
	}

	/**
	 * 获取全局上下文
	 * @return 全局上下文
	 */
	public static Map<String, String> getGlobalContext() {
		return Collections.unmodifiableMap(GLOBAL_CONTEXT);
	}

	/**
	 * 获取线程上下文
	 * @return 线程上下文
	 */
	public static Map<String, String> getContext() {
		Map<String, String> body = CONTEXT.get();
		if (body == null) {
			return new HashMap<>(0);
		}

		return Collections.unmodifiableMap(body);
	}

	/**
	 * 判断全局上下文是否包含key
	 * @param key 变量Key
	 * @return true: 包含; false: 不包含
	 */
	public static boolean globalContain(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		return GLOBAL_CONTEXT.containsKey(key);
	}

	/**
	 * 判断上下文是否包含key
	 * @param key 变量Key
	 * @return true: 包含; false: 不包含
	 */
	public static boolean contain(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		Map<String, String> body = CONTEXT.get();
		if (body == null) {
			return false;
		}
		return body.containsKey(key);
	}

	/**
	 * 删除全局上下文
	 * @param key 变量Key
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	@Nullable
	public static String remoteGlobal(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		return GLOBAL_CONTEXT.remove(key);
	}

	/**
	 * 删除上下文
	 * @param key 变量Key
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	@Nullable
	public static String remove(String key) {
		Preconditions.checkNotNull(key, "key must not be null");

		Map<String, String> body = CONTEXT.get();
		if (body == null) {
			return null;
		}
		return body.remove(key);
	}

	/**
	 * 删除全局上下文
	 * @param key 变量Key
	 * @return 如果对应的key已经存在，则返回原来的值，否则返回null
	 */
	@Nullable
	public static String globalRemove(String key) {
		Preconditions.checkNotNull(key, "key must not be null");
		if (BASE_DATA.containsKey(key)) {
			throw new IllegalArgumentException("can not remove the key: " + key);
		}
		return GLOBAL_CONTEXT.remove(key);
	}

	/**
	 * 清空上下文
	 */
	public static void remove() {
		CONTEXT.remove();
	}

	/**
	 * 清空全局上下文
	 */
	public static void globalRemove() {
		GLOBAL_CONTEXT.clear();
		GLOBAL_CONTEXT.putAll(BASE_DATA);
	}

	/**
	 * 清空所有上下文
	 */
	public static void removeAll() {
		remove();
		globalRemove();
	}

	public static long nextId() {
		return ID.incrementAndGet();
	}

	public static String nextId(String prefix) {
		return prefix + "-" + nextId();
	}

	public static String nextId(String preId, String prefix) {
		return preId + "-" + nextId(prefix);
	}

}
