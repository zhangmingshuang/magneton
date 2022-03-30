/*
 * Copyright (C) 2005 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.magneton.core.reflect;

import cn.hutool.core.util.ClassUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.magneton.core.base.Preconditions;
import org.magneton.core.collect.ConcurrentReferenceHashMap;
import org.magneton.foundation.exception.UtilException;

/**
 * Static utilities relating to Java reflection.
 *
 * @since 12.0
 */
@ElementTypesAreNonnullByDefault
public final class Reflection {

	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

	private static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

	private static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

	private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/**
	 * Cache for {@link Class#getDeclaredMethods()} plus equivalent default methods from
	 * Java 8 based interfaces, allowing for fast iteration.
	 */
	private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap<>(256);

	/**
	 * Cache for {@link Class#getDeclaredFields()}, allowing for fast iteration.
	 */
	private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap<>(256);

	private Reflection() {
	}

	/**
	 * Returns the package name of {@code clazz} according to the Java Language
	 * Specification (section 6.7). Unlike {@link Class#getPackage}, this method only
	 * parses the class name, without attempting to define the {@link Package} and hence
	 * load files.
	 */
	public static String getPackageName(Class<?> clazz) {
		return getPackageName(clazz.getName());
	}

	/**
	 * Returns the package name of {@code classFullName} according to the Java Language
	 * Specification (section 6.7). Unlike {@link Class#getPackage}, this method only
	 * parses the class name, without attempting to define the {@link Package} and hence
	 * load files.
	 */
	public static String getPackageName(String classFullName) {
		int lastDot = classFullName.lastIndexOf('.');
		return (lastDot < 0) ? "" : classFullName.substring(0, lastDot);
	}

	/**
	 * Ensures that the given classes are initialized, as described in <a href=
	 * "http://java.sun.com/docs/books/jls/third_edition/html/execution.html#12.4.2">JLS
	 * Section 12.4.2</a>.
	 *
	 * <p>
	 * WARNING: Normally it's a smell if a class needs to be explicitly initialized,
	 * because static state hurts system maintainability and testability. In cases when
	 * you have no choice while inter-operating with a legacy framework, this method helps
	 * to keep the code less ugly.
	 * @throws ExceptionInInitializerError if an exception is thrown during initialization
	 * of a class
	 */
	public static void initialize(Class<?>... classes) {
		for (Class<?> clazz : classes) {
			try {
				Class.forName(clazz.getName(), true, clazz.getClassLoader());
			}
			catch (ClassNotFoundException e) {
				throw new AssertionError(e);
			}
		}
	}

	/**
	 * Returns a proxy instance that implements {@code interfaceType} by dispatching
	 * method invocations to {@code handler}. The class loader of {@code interfaceType}
	 * will be used to define the proxy class. To implement multiple interfaces or specify
	 * a class loader, use {@link Proxy#newProxyInstance}.
	 * @throws IllegalArgumentException if {@code interfaceType} does not specify the type
	 * of a Java interface
	 */
	public static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
		Preconditions.checkNotNull(handler);
		Preconditions.checkArgument(interfaceType.isInterface(), "%s is not an interface", interfaceType);
		Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[] { interfaceType },
				handler);
		return interfaceType.cast(object);
	}

	// Method handling

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name and
	 * no parameters. Searches all superclasses up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @return the Method object, or {@code null} if none found
	 */
	@Nullable
	public static Method findMethod(Class<?> clazz, String name) {
		return findMethod(clazz, name, EMPTY_CLASS_ARRAY);
	}

	/**
	 * Attempt to find a {@link Method} on the supplied class with the supplied name and
	 * parameter types. Searches all superclasses up to {@code Object}.
	 * <p>
	 * Returns {@code null} if no {@link Method} can be found.
	 * @param clazz the class to introspect
	 * @param name the name of the method
	 * @param paramTypes the parameter types of the method (may be {@code null} to
	 * indicate any signature)
	 * @return the Method object, or {@code null} if none found
	 */
	@Nullable
	public static Method findMethod(Class<?> clazz, String name, @Nullable Class<?>... paramTypes) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		Preconditions.checkNotNull(name, "Method name must not be null");
		Class<?> searchType = clazz;
		while (searchType != null) {
			Method[] methods = (searchType.isInterface() ? searchType.getMethods()
					: getDeclaredMethods(searchType, false));
			for (Method method : methods) {
				if (name.equals(method.getName()) && (paramTypes == null || hasSameParams(method, paramTypes))) {
					return method;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object with no
	 * arguments. The target object can be {@code null} when invoking a static
	 * {@link Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @return the invocation result, if any
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target) {
		return invokeMethod(method, target, EMPTY_OBJECT_ARRAY);
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object with the
	 * supplied arguments. The target object can be {@code null} when invoking a static
	 * {@link Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to {@link #handleReflectionException}.
	 * @param method the method to invoke
	 * @param target the target object to invoke the method on
	 * @param args the invocation arguments (may be {@code null})
	 * @return the invocation result, if any
	 */
	@Nullable
	public static Object invokeMethod(Method method, @Nullable Object target, @Nullable Object... args) {
		try {
			return method.invoke(target, args);
		}
		catch (Exception ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}

	/**
	 * Variant of {@link Class#getDeclaredMethods()} that uses a local cache in order to
	 * avoid the JVM's SecurityManager check and new Method instances. In addition, it
	 * also includes Java 8 default methods from locally implemented interfaces, since
	 * those are effectively to be treated just like declared methods.
	 * @param clazz the class to introspect
	 * @return the cached array of methods
	 * @throws IllegalStateException if introspection fails
	 * @since 5.2
	 * @see Class#getDeclaredMethods()
	 */
	public static Method[] getDeclaredMethods(Class<?> clazz) {
		return getDeclaredMethods(clazz, true);
	}

	// Field handling

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name}. Searches all superclasses up to {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	@Nullable
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	/**
	 * Attempt to find a {@link Field field} on the supplied {@link Class} with the
	 * supplied {@code name} and/or {@link Class type}. Searches all superclasses up to
	 * {@link Object}.
	 * @param clazz the class to introspect
	 * @param name the name of the field (may be {@code null} if type is specified)
	 * @param type the type of the field (may be {@code null} if name is specified)
	 * @return the corresponding Field object, or {@code null} if not found
	 */
	@Nullable
	public static Field findField(Class<?> clazz, @Nullable String name, @Nullable Class<?> type) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		Preconditions.checkArgument(name != null || type != null, "Either name or type of the field must be specified");
		Class<?> searchType = clazz;
		while (Object.class != searchType && searchType != null) {
			Field[] fields = getDeclaredFields(searchType);
			for (Field field : fields) {
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

	/**
	 * Set the field represented by the supplied {@linkplain Field field object} on the
	 * specified {@linkplain Object target object} to the specified {@code value}.
	 * <p>
	 * In accordance with {@link Field#set(Object, Object)} semantics, the new value is
	 * automatically unwrapped if the underlying field has a primitive type.
	 * <p>
	 * This method does not support setting {@code static final} fields.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * @param field the field to set
	 * @param target the target object on which to set the field
	 * @param value the value to set (may be {@code null})
	 */
	public static void setField(Field field, @Nullable Object target, @Nullable Object value) {
		try {
			field.set(target, value);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
		}
	}

	/**
	 * Get the field represented by the supplied {@link Field field object} on the
	 * specified {@link Object target object}. In accordance with
	 * {@link Field#get(Object)} semantics, the returned value is automatically wrapped if
	 * the underlying field has a primitive type.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException(Exception)}.
	 * @param field the field to get
	 * @param target the target object from which to get the field
	 * @return the field's current value
	 */
	@Nullable
	public static Object getField(Field field, @Nullable Object target) {
		try {
			return field.get(target);
		}
		catch (IllegalAccessException ex) {
			handleReflectionException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}
	// Exception handling

	/**
	 * Handle the given reflection exception.
	 * <p>
	 * Should only be called if no checked exception is expected to be thrown by a target
	 * method, or if an error occurs while accessing a method or field.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an IllegalStateException
	 * with an appropriate message or UndeclaredThrowableException otherwise.
	 * @param ex the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method or field: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	/**
	 * Handle the given invocation target exception. Should only be called if no checked
	 * exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of such a root cause.
	 * Throws an UndeclaredThrowableException otherwise.
	 * @param ex the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(InvocationTargetException ex) {
		rethrowRuntimeException(ex.getTargetException());
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the <em>target
	 * exception</em> of an {@link InvocationTargetException}. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Rethrows the underlying exception cast to a {@link RuntimeException} or
	 * {@link Error} if appropriate; otherwise, throws an
	 * {@link UndeclaredThrowableException}.
	 * @param ex the exception to rethrow
	 * @throws RuntimeException the rethrown exception
	 */
	public static void rethrowRuntimeException(Throwable ex) {
		if (ex instanceof RuntimeException) {
			throw (RuntimeException) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	/**
	 * Rethrow the given {@link Throwable exception}, which is presumably the <em>target
	 * exception</em> of an {@link InvocationTargetException}. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Rethrows the underlying exception cast to an {@link Exception} or {@link Error} if
	 * appropriate; otherwise, throws an {@link UndeclaredThrowableException}.
	 * @param ex the exception to rethrow
	 * @throws Exception the rethrown exception (in case of a checked exception)
	 */
	public static void rethrowException(Throwable ex) throws Exception {
		if (ex instanceof Exception) {
			throw (Exception) ex;
		}
		if (ex instanceof Error) {
			throw (Error) ex;
		}
		throw new UndeclaredThrowableException(ex);
	}

	// --------------------------------------
	/**
	 * 实例化对象
	 * @param <T> 对象类型
	 * @param clazz 类名
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(String clazz) throws UtilException {
		try {
			return (T) Class.forName(clazz).newInstance();
		}
		catch (Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 实例化对象
	 * @param <T> 对象类型
	 * @param clazz 类
	 * @param params 构造函数参数
	 * @return 对象
	 * @throws UtilException 包装各类异常
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) throws UtilException {
		if (org.magneton.core.base.Arrays.isNullOrEmpty(params)) {
			final Constructor<T> constructor = getConstructor(clazz);
			try {
				return constructor.newInstance();
			}
			catch (Exception e) {
				throw new UtilException(e, "Instance class [{}] error!", clazz);
			}
		}

		final Class<?>[] paramTypes = ClassUtil.getClasses(params);
		final Constructor<T> constructor = getConstructor(clazz, paramTypes);
		if (null == constructor) {
			throw new UtilException("No Constructor matched for parameter types: [{}]", new Object[] { paramTypes });
		}
		try {
			return constructor.newInstance(params);
		}
		catch (Exception e) {
			throw new UtilException(e, "Instance class [{}] error!", clazz);
		}
	}

	/**
	 * 尝试遍历并调用此类的所有构造方法，直到构造成功并返回
	 * <p>
	 * 对于某些特殊的接口，按照其默认实现实例化，例如： <pre>
	 *     Map       -》 HashMap
	 *     Collction -》 ArrayList
	 *     List      -》 ArrayList
	 *     Set       -》 HashSet
	 * </pre>
	 * @param <T> 对象类型
	 * @param beanClass 被构造的类
	 * @return 构造后的对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceIfPossible(Class<T> beanClass) {
		Preconditions.checkNotNull(beanClass);
		// 某些特殊接口的实例化按照默认实现进行
		if (beanClass.isAssignableFrom(AbstractMap.class)) {
			beanClass = (Class<T>) HashMap.class;
		}
		else if (beanClass.isAssignableFrom(List.class)) {
			beanClass = (Class<T>) ArrayList.class;
		}
		else if (beanClass.isAssignableFrom(Set.class)) {
			beanClass = (Class<T>) HashSet.class;
		}

		try {
			return newInstance(beanClass);
		}
		catch (Exception e) {
			// ignore
			// 默认构造不存在的情况下查找其它构造
		}

		final Constructor<T>[] constructors = getConstructors(beanClass);
		Class<?>[] parameterTypes;
		for (Constructor<T> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			if (0 == parameterTypes.length) {
				continue;
			}
			setAccessible(constructor);
			try {
				return constructor.newInstance(ClassUtil.getDefaultValues(parameterTypes));
			}
			catch (Exception ignore) {
				// 构造出错时继续尝试下一种构造方式
			}
		}
		return null;
	}
	// ---------------------------------------------------------------------------------------------------------

	/**
	 * 查找类中的指定参数的构造方法，如果找到构造方法，会自动设置可访问为true
	 * @param <T> 对象类型
	 * @param clazz 类
	 * @param parameterTypes 参数类型，只要任何一个参数是指定参数的父类或接口或相等即可，此参数可以不传
	 * @return 构造方法，如果未找到返回null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
		if (null == clazz) {
			return null;
		}

		final Constructor<?>[] constructors = getConstructors(clazz);
		Class<?>[] pts;
		for (Constructor<?> constructor : constructors) {
			pts = constructor.getParameterTypes();
			if (ClassUtil.isAllAssignableFrom(pts, parameterTypes)) {
				// 构造可访问
				setAccessible(constructor);
				return (Constructor<T>) constructor;
			}
		}
		return null;
	}

	/**
	 * 获得一个类中所有构造列表
	 * @param <T> 构造的对象类型
	 * @param beanClass 类，非{@code null}
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] getConstructors(Class<T> beanClass) throws SecurityException {
		Preconditions.checkNotNull(beanClass);
		return (Constructor<T>[]) getConstructorsDirectly(beanClass);
	}

	/**
	 * 获得一个类中所有构造列表，直接反射获取，无缓存
	 * @param beanClass 类
	 * @return 字段列表
	 * @throws SecurityException 安全检查异常
	 */
	public static Constructor<?>[] getConstructorsDirectly(Class<?> beanClass) throws SecurityException {
		return beanClass.getDeclaredConstructors();
	}

	// ---------------------------------------------------------------------------------------------------------
	/**
	 * 设置方法为可访问（私有方法可以被外部调用）
	 * @param <T> AccessibleObject的子类，比如Class、Method、Field等
	 * @param accessibleObject 可设置访问权限的对象，比如Class、Method、Field等
	 * @return 被设置可访问的对象
	 * @since 4.6.8
	 */
	public static <T extends AccessibleObject> T setAccessible(T accessibleObject) {
		if (null != accessibleObject && false == accessibleObject.isAccessible()) {
			accessibleObject.setAccessible(true);
		}
		return accessibleObject;
	}

	private static Method[] getDeclaredMethods(Class<?> clazz, boolean defensive) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		Method[] result = declaredMethodsCache.get(clazz);
		if (result == null) {
			try {
				Method[] declaredMethods = clazz.getDeclaredMethods();
				List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
				if (defaultMethods != null) {
					result = new Method[declaredMethods.length + defaultMethods.size()];
					System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
					int index = declaredMethods.length;
					for (Method defaultMethod : defaultMethods) {
						result[index] = defaultMethod;
						index++;
					}
				}
				else {
					result = declaredMethods;
				}
				declaredMethodsCache.put(clazz, (result.length == 0 ? EMPTY_METHOD_ARRAY : result));
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName()
						+ "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return (result.length == 0 || !defensive) ? result : result.clone();
	}

	private static boolean hasSameParams(Method method, Class<?>[] paramTypes) {
		return (paramTypes.length == method.getParameterCount()
				&& Arrays.equals(paramTypes, method.getParameterTypes()));
	}

	@Nullable
	private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
		List<Method> result = null;
		for (Class<?> ifc : clazz.getInterfaces()) {
			for (Method ifcMethod : ifc.getMethods()) {
				if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
					if (result == null) {
						result = new ArrayList<>();
					}
					result.add(ifcMethod);
				}
			}
		}
		return result;
	}

	/**
	 * This variant retrieves {@link Class#getDeclaredFields()} from a local cache in
	 * order to avoid the JVM's SecurityManager check and defensive array copying.
	 * @param clazz the class to introspect
	 * @return the cached array of fields
	 * @throws IllegalStateException if introspection fails
	 * @see Class#getDeclaredFields()
	 */
	private static Field[] getDeclaredFields(Class<?> clazz) {
		Preconditions.checkNotNull(clazz, "Class must not be null");
		Field[] result = declaredFieldsCache.get(clazz);
		if (result == null) {
			try {
				result = clazz.getDeclaredFields();
				declaredFieldsCache.put(clazz, (result.length == 0 ? EMPTY_FIELD_ARRAY : result));
			}
			catch (Throwable ex) {
				throw new IllegalStateException("Failed to introspect Class [" + clazz.getName()
						+ "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
			}
		}
		return result;
	}

}
