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
 * .
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
