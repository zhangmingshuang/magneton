package org.magneton.enhance.pay.wxv3.core;

import com.google.common.base.Preconditions;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author zhangmsh 2022/7/15
 * @since 1.0.1
 */
public class WxPayConfigAdapter implements MethodInterceptor {

	private final WxPayConfig defaultConfig;

	private final WxPayConfig config;

	public static WxPayConfig create(WxPayConfig defaultConfig, WxPayConfig config) {
		Preconditions.checkNotNull(defaultConfig, "defaultConfig");
		Preconditions.checkNotNull(defaultConfig, "config");

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(WxPayConfig.class);
		enhancer.setCallback(new WxPayConfigAdapter(defaultConfig, config));
		return (WxPayConfig) enhancer.create();
	}

	public WxPayConfigAdapter(WxPayConfig defaultConfig, WxPayConfig config) {
		this.defaultConfig = defaultConfig;
		this.config = config;
	}

	@Override
	public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		Object value = methodProxy.invoke(this.config, args);
		if (value != null) {
			return value;
		}
		return methodProxy.invoke(this.defaultConfig, args);
	}

}
