package org.magneton.spring.starter.servlet;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.springframework.core.NamedInheritableThreadLocal;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Request Context
 *
 * @author zhangmsh
 * @since 2023.1
 */
@SuppressWarnings("unchecked")
public class RequestContext {

	private static final ThreadLocal<Object> HOLDER = new NamedInheritableThreadLocal<>("Magneton Request Context");

	private static final ThreadLocal<Map<?, ?>> REF_HOLDER = new NamedInheritableThreadLocal<>(
			"Magneton Ref Request Context");

	private RequestContext() {

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
		Preconditions.checkNotNull(obj, "obj must not be null");
		E oldObj = (E) HOLDER.get();
		HOLDER.set(obj);
		return oldObj;
	}

	public static <E> void set(E obj) {
		Preconditions.checkNotNull(obj, "obj must not be null");
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
