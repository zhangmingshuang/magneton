package org.magneton.enhance.pvuv;

import org.magneton.annotation.Cleanup;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 支持缓存的PVUV
 *
 * @author zhangmsh.
 * @since M2023.9
 */
public class CachedPvUv extends AbstractPvUv {

	private static final Map<String, Uv> UV_CACHE = new ConcurrentHashMap<>();

	private AbstractPvUv abstractPvUv;

	public CachedPvUv(AbstractPvUv abstractPvUv) {
		super(null);
		this.abstractPvUv = abstractPvUv;
	}

	@Override
	public Uv uv(String group) {
		return UV_CACHE.computeIfAbsent(group, k -> {
			Uv uv = this.abstractPvUv.uv(group);
			return (Uv) Proxy.newProxyInstance(Uv.class.getClassLoader(), new Class[] { Uv.class },
					new UvProxy(uv, group, this.getPvUvConfig()));
		});
	}

	public boolean isCached(String group) {
		return UV_CACHE.containsKey(group);
	}

	public static class UvProxy implements InvocationHandler {

		private final Uv uv;

		private final String group;

		private final PvUvConfig pvUvConfig;

		private Method cleanMethod;

		public UvProxy(Uv uv, String group, PvUvConfig pvUvConfig) {
			this.uv = uv;
			this.group = group;
			this.pvUvConfig = pvUvConfig;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (this.cleanMethod == null) {
				Cleanup cleanup = method.getAnnotation(Cleanup.class);
				if (cleanup != null) {
					this.cleanMethod = method;
				}
			}
			Object res = method.invoke(this.uv, args);
			if (this.cleanMethod == method) {
				UV_CACHE.remove(this.group);
			}
			return res;
		}

	}

}