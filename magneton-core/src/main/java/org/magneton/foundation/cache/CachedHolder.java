package org.magneton.foundation.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.magneton.foundation.MoreArrays;
import org.magneton.foundation.reflect.MoreReflection;

/**
 * @author zhangmsh 2022/3/24
 * @since 1.0.0
 */
public class CachedHolder<T> {

	private final T object;

	private final Class<T> clazz;

	private CachedHolder(T object) {
		this.object = object;
		this.clazz = (Class<T>) object.getClass();
	}

	public static <T> CachedHolder from(T object) {
		return new CachedHolder<T>(Preconditions.checkNotNull(object));
	}

	public List<Object> invoke() throws InvocationTargetException, IllegalAccessException {
		return this.invokeWithArgs((Object) null);
	}

	public List<Object> invokeWithArgs(@Nullable Object... args)
			throws InvocationTargetException, IllegalAccessException {
		Method[] declaredMethods = MoreReflection.getDeclaredMethods(Preconditions.checkNotNull(this.clazz));
		if (MoreArrays.isNullOrEmpty(declaredMethods)) {
			return Collections.emptyList();
		}
		List<Object> result = Lists.newArrayList(declaredMethods.length);
		for (Method declaredMethod : declaredMethods) {
			Object invoke = declaredMethod.invoke(this.object, args);
			result.add(invoke);
		}
		return result;
	}

}
