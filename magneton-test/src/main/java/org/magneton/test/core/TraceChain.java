/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号401
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.test.core;

import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 调用链
 *
 * @author zhangmsh 2021/8/9
 * @since 1.0.0
 */
@Getter
public class TraceChain {

	private static final ThreadLocal<TraceChain> THREAD_LOCAL = new ThreadLocal();

	private Class root;

	private final Map<Class, Queue<Inject>> injectChain = Maps.newHashMap();

	private TraceChain() {
	}

	public static TraceChain current() {
		TraceChain traceChain = THREAD_LOCAL.get();
		if (traceChain == null) {
			traceChain = new TraceChain();
			THREAD_LOCAL.set(traceChain);
		}
		return traceChain;
	}

	public void start(Class clazz) {
		this.root = clazz;
	}

	public void end() {
		THREAD_LOCAL.remove();
	}

	public Class getRoot() {
		return this.root;
	}

	public void inject(Class clazz, @Nullable Object remark) {
		Preconditions.checkNotNull(clazz, "clazz");

		this.injectChain.computeIfAbsent(clazz, c -> Queues.newArrayDeque()).add(new Inject(clazz, remark));
	}

	public void injectTo(Class clazz, @Nullable Object remark, Class toClazz) {
		Preconditions.checkNotNull(clazz, "clazz");
		Preconditions.checkNotNull(toClazz, "toClazz");

		this.injectChain.computeIfAbsent(toClazz, c -> Queues.newArrayDeque()).add(new Inject(clazz, remark));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(256);
		builder.append("源").append(this.root).append("注入链：");
		Set<Map.Entry<Class, Queue<Inject>>> entries = this.injectChain.entrySet();
		for (Map.Entry<Class, Queue<Inject>> entry : entries) {
			builder.append(entry.getKey()).append("[");
			Queue<Inject> value = entry.getValue();
			Inject inject;
			while ((inject = value.poll()) != null) {
				builder.append(inject.clazz);
				if (Objects.nonNull(inject.remark)) {
					builder.append("(").append(inject.remark).append(")");
				}
				builder.append("-->");
			}
			builder.append("]==>");
		}
		return builder.toString();
	}

	@AllArgsConstructor
	public static class Inject {

		private Class clazz;

		@Nullable
		private Object remark;

	}

}
