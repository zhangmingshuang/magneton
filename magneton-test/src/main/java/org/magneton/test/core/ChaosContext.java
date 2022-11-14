package org.magneton.test.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import org.magneton.test.annotation.TestAutowired;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.annotation.TestComponentScan;
import org.magneton.test.aware.Aware;
import org.magneton.test.util.AnnotationUtil;
import org.magneton.test.util.GenericUtil;
import org.magneton.test.util.SortUtil;

/**
 * .
 *
 * @author zhangmsh 2021/8/2
 * @since 2.0.0
 */
public class ChaosContext {

	private static final Set<String> LOAD_PACKAGES = Sets.newHashSet();

	private static final Map<String, Object> INITIALIZED_OBJECTS = Maps.newHashMap();

	private static final Multimap<String, String> ALIAS_NAMES = ArrayListMultimap.create();

	private static final Multimap<Class, Aware> AWARED_OBJECTS = ArrayListMultimap.create();

	private ChaosContext() {
	}

	@SuppressWarnings({ "OverlyBroadCatchBlock", "NewExceptionWithoutArguments" })
	public static void init(Class<? extends ChaosApplication> chaosApplication) {

		try {
			Set<String> scanPackages = Sets.newHashSet();
			TestComponentScan componentScan = AnnotationUtil.findAnnotations(chaosApplication, TestComponentScan.class);
			if (componentScan != null) {
				String scanPackage = componentScan.value();
				if (Strings.isNullOrEmpty(scanPackage)) {
					scanPackage = chaosApplication.getPackage().getName();
				}
				scanPackages.add(scanPackage);
			}

			loadAndInitialize(scanPackages);

			refresh();
		}
		catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	private static void refresh() throws IllegalAccessException {
		for (Entry<String, Object> entry : INITIALIZED_OBJECTS.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			doRefresh(key, value);
		}
	}

	private static void doRefresh(String className, Object obj) throws IllegalAccessException {
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			TestAutowired autowired = AnnotationUtil.findAnnotations(field, TestAutowired.class);
			if (autowired == null) {
				continue;
			}
			field.setAccessible(true);

			Class fieldClass = field.getType();
			if (Collection.class.isAssignableFrom(fieldClass)) {
				List list = Lists.newArrayList();
				List<Class<?>> generics = GenericUtil.getGenerics(field);
				Verify.verify(!generics.isEmpty(), "获取集合泛型错误:%s#%s，要注入的集合必须声明泛型类型", fieldClass, field);
				INITIALIZED_OBJECTS.forEach((k, o) -> {
					if (generics.get(0).isAssignableFrom(o.getClass())) {
						list.add(o);
					}
				});
				if (autowired.required()) {
					Verify.verify(!list.isEmpty(), "无法找到%s的对象注入到%s#%s中", generics.get(0), className, field.getName());
				}
				SortUtil.sort(list);
				field.set(obj, list);
			}
			else {
				Object o = getComponent(fieldClass.getName());
				Verify.verify(o != null, "无法找到%s的对象注入到%s#%s中", fieldClass.getName(), className, field.getName());
				field.set(obj, o);
			}
		}
		if (AfterAutowrited.class.isAssignableFrom(obj.getClass())) {
			((AfterAutowrited) obj).afterAutowrited();
		}
		doAware(className, obj);
	}

	private static void doAware(String className, Object obj) {
		if (!AWARED_OBJECTS.isEmpty()) {
			AWARED_OBJECTS.forEach((awareClass, awareObject) -> {
				if (awareClass.isAssignableFrom(obj.getClass())) {
					awareObject.setInterest(obj);
				}
			});
		}
	}

	@SuppressWarnings("MethodWithMoreThanThreeNegations")
	private static void loadAndInitialize(Set<String> scanPackages) throws IOException {
		ClassPath classPath = ClassPath.from(ChaosContext.class.getClassLoader());

		Set<ClassInfo> allClassInfos = Sets.newHashSet();
		for (String scanPackage : scanPackages) {
			ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(scanPackage);
			allClassInfos.addAll(classInfos);
			LOAD_PACKAGES.add(scanPackage);
		}

		Set<String> componentScanPackages = Sets.newHashSet();
		for (ClassInfo classInfo : allClassInfos) {
			if (INITIALIZED_OBJECTS.containsKey(classInfo.getName())) {
				continue;
			}
			Class<?> clazz = classInfo.load();
			TestComponent component = AnnotationUtil.findAnnotations(clazz, TestComponent.class);
			if (component != null) {
				addLoadClass(clazz);
			}
			TestComponentScan componentScan = AnnotationUtil.findAnnotations(clazz, TestComponentScan.class);
			if (componentScan != null && !Strings.isNullOrEmpty(componentScan.value())
					&& !LOAD_PACKAGES.contains(componentScan.value())) {
				componentScanPackages.add(componentScan.value());
			}
		}
		if (!componentScanPackages.isEmpty()) {
			loadAndInitialize(componentScanPackages);
		}
	}

	private static void addLoadClass(Class<?> clazz) {
		try {
			if (clazz.isInterface()) {
				return;
			}

			Object obj = clazz.getConstructor().newInstance();
			if (Aware.class.isAssignableFrom(clazz)) {
				List<Class<?>> generics = GenericUtil.getGenerics(clazz);
				Verify.verify(generics.isEmpty(), "Aware类必须声明泛型类型，如Aware<Xxx>");
				AWARED_OBJECTS.put(generics.get(0), (Aware) obj);
				return;
			}
			String className = clazz.getName();
			Class<?>[] interfaces = clazz.getInterfaces();
			if (interfaces.length > 0) {
				for (Class<?> anInterface : interfaces) {
					ALIAS_NAMES.put(anInterface.getName(), className);
					className = anInterface.getName();
				}
			}
			Object exist = INITIALIZED_OBJECTS.put(clazz.getName(), obj);
			if (exist != null) {
				throw new RuntimeException("存在多个相同的类：" + clazz.getName());
			}
		}
		catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	@Nullable
	public static Object getComponent(String name) {
		Object o = INITIALIZED_OBJECTS.get(Preconditions.checkNotNull(name));
		if (o == null) {
			Collection<String> aliasNames = ALIAS_NAMES.get(name);
			if (!aliasNames.isEmpty()) {
				for (String aliasName : aliasNames) {
					o = INITIALIZED_OBJECTS.get(aliasName);
					if (o != null) {
						break;
					}
				}
			}
		}
		return o;
	}

	@Nullable
	public static <T> T getComponent(Class<T> clazz) {
		return (T) getComponent(Preconditions.checkNotNull(clazz).getName());
	}

}
