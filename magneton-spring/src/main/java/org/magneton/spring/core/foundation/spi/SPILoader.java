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

package org.magneton.spring.core.foundation.spi;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import org.magneton.spring.core.exception.DuplicateFoundException;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SPI加载器.
 *
 * @author zhangmsh
 * @since 1.0.0
 */
public class SPILoader {

	private SPILoader() {
	}

	@Nullable
	public static <T> T loadOneOnly(Class<T> clazz) {
		return loadOneOnly(clazz, Thread.currentThread().getContextClassLoader());
	}

	@Nullable
	public static <T> T loadOneOnly(Class<T> clazz, ClassLoader classLoader) {
		verifyIsSpiClass(clazz);
		Preconditions.checkNotNull(classLoader, "classLoader must not be null");

		ConditionServiceLoader<T> serviceLoader = ConditionServiceLoader.load(clazz, classLoader, true);
		Iterator<Class<T>> iterator = serviceLoader.iterator();
		T result = null;
		while (iterator.hasNext()) {
			Class<T> tmpResult = iterator.next();
			if (result != null && tmpResult != null) {
				throw new DuplicateFoundException(result, tmpResult);
			}
			List<T> providerInstances = serviceLoader.getProviderInstances(tmpResult);
			if (providerInstances.size() > 1) {
				throw new DuplicateFoundException(providerInstances.get(0), providerInstances.get(1));
			}
			result = providerInstances.isEmpty() ? null : providerInstances.get(0);
		}
		return result;
	}

	public static <T> Iterator<Class<T>> load(Class<T> clazz) {
		return load(clazz, Thread.currentThread().getContextClassLoader());
	}

	public static <T> Iterator<Class<T>> load(Class<T> clazz, ClassLoader classLoader) {
		verifyIsSpiClass(clazz);
		Preconditions.checkNotNull(classLoader, "classLoader must not be null");

		ConditionServiceLoader<T> loadClasses = ConditionServiceLoader.load(clazz, classLoader);
		List<Class<T>> result = new ArrayList<>();
		loadClasses.iterator().forEachRemaining(loadClazz -> {
			if (loadClazz != null) {
				result.add(loadClazz);
			}
		});
		return result.iterator();
	}

	public static <T> List<T> loadInstance(Class<T> clazz) {
		return loadInstance(clazz, Thread.currentThread().getContextClassLoader());
	}

	public static <T> List<T> loadInstance(Class<T> clazz, ClassLoader classLoader) {
		verifyIsSpiClass(clazz);
		Preconditions.checkNotNull(classLoader, "classLoader must not be null");

		ConditionServiceLoader<T> loadClasses = ConditionServiceLoader.load(clazz, classLoader, true);
		Iterator<Class<T>> iterator = loadClasses.iterator();
		List<T> result = Lists.newArrayList();
		while (iterator.hasNext()) {
			Class<T> next = iterator.next();
			if (next == null) {
				continue;
			}
			result.addAll(loadClasses.getProviderInstances(next));
		}
		return result;
	}

	private static <T> void verifyIsSpiClass(Class<T> clazz) {
		Preconditions.checkNotNull(clazz, "clazz");
		SPI spi = clazz.getAnnotation(SPI.class);
		Verify.verify(spi != null, "%s @SPI not found. must be annotation with @SPI", clazz);
	}

}
