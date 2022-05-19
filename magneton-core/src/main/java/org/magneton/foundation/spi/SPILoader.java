package org.magneton.foundation.spi;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import javax.annotation.Nullable;
import org.magneton.foundation.exception.DuplicateFoundException;

/**
 * SPI类加载器.
 *
 * @author zhangmsh
 * @since 2021/11/5
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
		Preconditions.checkNotNull(classLoader, "classLoader must be not null");

		verifyIsSpiClass(clazz);

		ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
		Iterator<T> iterator = serviceLoader.iterator();
		T result = null;
		while (iterator.hasNext()) {
			if (result != null) {
				throw new DuplicateFoundException(result, iterator.next());
			}
			result = iterator.next();
		}
		return result;
	}

	public static <T> Iterator<Class<T>> load(Class<T> clazz) {
		return load(clazz, Thread.currentThread().getContextClassLoader());
	}

	public static <T> Iterator<Class<T>> load(Class<T> clazz, ClassLoader classLoader) {
		Preconditions.checkNotNull(classLoader, "classLoader must be not null");

		verifyIsSpiClass(clazz);

		DeinstanceServiceLoader<T> loadClasses = DeinstanceServiceLoader.load(clazz, classLoader);
		return loadClasses.iterator();
	}

	public static <T> List<T> loadServices(Class<T> clazz, ClassLoader classLoader) {
		Preconditions.checkNotNull(classLoader, "classLoader must be not null");

		verifyIsSpiClass(clazz);

		ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz, classLoader);
		List<T> result = Lists.newArrayList();
		serviceLoader.iterator().forEachRemaining(result::add);
		return result;
	}

	public static <T> List<T> loadServices(Class<T> clazz) {
		return loadServices(clazz, Thread.currentThread().getContextClassLoader());
	}

	private static <T> void verifyIsSpiClass(Class<T> clazz) {
		Preconditions.checkNotNull(clazz, "clazz");
		SPI spi = clazz.getAnnotation(SPI.class);
		Verify.verify(spi != null, "%s @SPI not found. must be annotation with @SPI", clazz);
	}

}
