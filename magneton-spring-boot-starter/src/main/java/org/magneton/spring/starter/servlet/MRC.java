package org.magneton.spring.starter.servlet;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import org.springframework.core.NamedInheritableThreadLocal;

/**
 * Magneton Request Context
 *
 * @author zhangmsh
 * @since 2023.1
 */
public class MRC {

	private static final ThreadLocal<Object> HOLDER = new NamedInheritableThreadLocal<>("Magneton Request Context");

	private static final ThreadLocal<Map<?, ?>> REF_HOLDER = new NamedInheritableThreadLocal<>(
			"Magneton Ref Request Context");

	private MRC() {

	}

	/**
	 * request end.
	 */
	public static void clean() {
		HOLDER.remove();
		REF_HOLDER.remove();
	}

	@Nullable
	@CanIgnoreReturnValue
	public static <E> E setAndGet(E obj) {
		Preconditions.checkNotNull(obj, "obj must be not null");
		E oldObj = (E) HOLDER.get();
		HOLDER.set(obj);
		return oldObj;
	}

	public static <E> void set(E obj) {
		Preconditions.checkNotNull(obj, "obj must be not null");
		HOLDER.set(obj);
	}

	@Nullable
	public static <E> E get() {
		return (E) HOLDER.get();
	}

	public static <K, V> Map<K, V> map() {
		Map<K, V> ref = (Map<K, V>) REF_HOLDER.get();
		if (ref == null) {
			ref = Maps.newHashMap();
			REF_HOLDER.set(ref);
		}
		return ref;
	}

}
