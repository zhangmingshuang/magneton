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

package org.magneton.test.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 * 生成工具.
 *
 * @author zhangmsh 2021/8/3
 * @since 2.0.0
 */
@Slf4j
public class GenericUtil {

	private GenericUtil() {
	}

	public static List<Class<?>> getGenerics(Field field) {
		if (field.getGenericType().getClass() == Class.class) {
			return Collections.emptyList();
		}
		List<Class<?>> classes = Lists.newArrayList();
		Type[] actualTypeArguments = ((ParameterizedTypeImpl) field.getGenericType()).getActualTypeArguments();
		if (actualTypeArguments.length < 1) {
			return Collections.emptyList();
		}
		for (Type actualTypeArgument : actualTypeArguments) {
			classes.add(getTypeClass(actualTypeArgument));
		}
		return classes;
	}

	//
	// @Nullable
	// public static Inject getClass(Class<?> clazz) {
	// return getClass(clazz, 0);
	// }
	//
	public static List<Class<?>> getGenerics(Class<?> clazz) {
		try {
			List<Type> actualTypeArguments = Lists.newArrayList();
			if (clazz.isInterface()) {
				Type[] genericInterfaces = clazz.getGenericInterfaces();
				if (genericInterfaces.length < 1) {
					return Collections.emptyList();
				}
				for (Type genericInterface : genericInterfaces) {
					Type[] actualTypeArgument = ((ParameterizedTypeImpl) genericInterface).getActualTypeArguments();
					actualTypeArguments.addAll(Lists.newArrayList(actualTypeArgument));
				}

			}
			else {
				Type type = clazz.getGenericSuperclass();
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Type[] actualTypeArgument = parameterizedType.getActualTypeArguments();
				actualTypeArguments.addAll(Lists.newArrayList(actualTypeArgument));
			}
			if (actualTypeArguments.isEmpty()) {
				return Collections.emptyList();
			}
			List<Class<?>> classes = Lists.newArrayList();
			for (Type actualTypeArgument : actualTypeArguments) {
				classes.add(getTypeClass(actualTypeArgument));
			}
			return classes;
		}
		catch (Throwable e) {
			// ignore
		}
		return Collections.emptyList();
	}
	//
	// @Nullable
	// public static Inject getClass(Type type) {
	// try {
	// Type[] actualTypeArguments =
	// ((ParameterizedTypeImpl) TypeToken.of(type).getType()).getActualTypeArguments();
	// if (actualTypeArguments.length < 1) {
	// return null;
	// }
	// return getActualClass(actualTypeArguments[0]);
	// } catch (Throwable e) {
	// // ignore
	// }
	// return null;
	// }

	// @Nullable
	// public static Inject getClass(Object object) {
	// return getClass(object, 0);
	// }
	//
	// @Nullable
	// public static Inject getClass(Object object, int index) {
	// try {
	// if (Field.class.isAssignableFrom(object.getClass())) {
	// Type[] actualTypeArguments =
	// ((ParameterizedTypeImpl) ((Field)
	// object).getGenericType()).getActualTypeArguments();
	// if (actualTypeArguments.length <= index) {
	// return null;
	// }
	// Type actualTypeArgument = actualTypeArguments[index];
	// return getActualClass(actualTypeArgument);
	// }
	// } catch (Throwable e) {
	// // ignore
	// log.warn("get generic type error:{}", e.getMessage());
	// }
	// return null;
	// }

	// private static Inject getActualClass(Type actualTypeArgument) {
	// if (ParameterizedType.class.isAssignableFrom(actualTypeArgument.getClass())) {
	// return Inject.of(
	// (Class) ((ParameterizedType) actualTypeArgument).getRawType(), actualTypeArgument);
	// }
	// return Inject.of((Class) actualTypeArgument);
	// }

	public static Class getTypeClass(Type type) {
		if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
			return (Class) ((ParameterizedType) type).getRawType();
		}
		return (Class) type;
	}

}
