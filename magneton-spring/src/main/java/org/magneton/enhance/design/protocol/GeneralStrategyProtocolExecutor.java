/*
 * Copyright (c) 2020-2030  Xiamen Nascent Corporation. All rights reserved.
 *
 * https://www.nascent.cn
 *
 * 厦门南讯股份有限公司创立于2010年，是一家始终以技术和产品为驱动，帮助大消费领域企业提供客户资源管理（CRM）解决方案的公司。
 * 福建省厦门市软件园二期观日路22号501
 * 客服电话 400-009-2300
 * 电话 +86（592）5971731 传真 +86（592）5971710
 *
 * All source code copyright of this system belongs to Xiamen Nascent Co., Ltd.
 * Any organization or individual is not allowed to reprint, publish, disclose, embezzle, sell and use it for other illegal purposes without permission!
 */

package org.magneton.enhance.design.protocol;

import com.google.common.base.Preconditions;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.magneton.enhance.design.context.BosomStrategy;
import org.magneton.spring.core.foundation.spi.SPILoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的策略协议执行器
 *
 * @author zhangmsh.
 * @since 1.0.0
 */
public class GeneralStrategyProtocolExecutor implements StrategyProtocolExecutor {

	private final List<StrategyProtocol> strategyProtocols = new ArrayList<>();

	public GeneralStrategyProtocolExecutor() {
		this.init();
	}

	protected void init() {
		List<StrategyProtocol> protocols = SPILoader.loadInstance(StrategyProtocol.class);
		for (StrategyProtocol protocol : protocols) {
			this.registerStrategyProtocol(protocol);
		}
	}

	protected void registerStrategyProtocol(StrategyProtocol protocol) {
		this.strategyProtocols.add(Preconditions.checkNotNull(protocol, "protocol"));

		StrategyProtocolRegisterProcessor processor = BosomStrategy
				.getOrDefault(StrategyProtocolRegisterProcessor.class, "GeneralStrategyProtocolExecutor");
		processor.handle(this.strategyProtocols);
	}

	protected List<StrategyProtocol> getStrategyProtocols() {
		return this.strategyProtocols;
	}

	@Override
	public Object convert(Object strategy) {
		Enhancer enhancer = new Enhancer();
		Class<?> clazz = strategy.getClass();
		if (clazz.isAnonymousClass()) {
			clazz = clazz.getSuperclass();
		}
		enhancer.setSuperclass(clazz);
		enhancer.setCallback(new Proxy(strategy, this.strategyProtocols));
		return enhancer.create();
	}

	public static class Proxy implements MethodInterceptor {

		private Object strategy;

		private List<StrategyProtocol> strategyProtocols;

		public Proxy(Object strategy, List<StrategyProtocol> strategyProtocols) {
			this.strategy = strategy;
			this.strategyProtocols = strategyProtocols;
		}

		@Override
		public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
			if (method.getDeclaringClass().isAssignableFrom(Object.class)) {
				return method.invoke(this.strategy, args);
			}
			StrategyContext context = new StrategyContext();
			context.setStrategy(this.strategy);
			context.setMethod(method);
			context.setArgs(args);
			for (int i = 0; i < this.strategyProtocols.size(); i++) {
				StrategyProtocol strategyProtocol = this.strategyProtocols.get(i);
				Object value = strategyProtocol.exec(context);
				context.addChain(strategyProtocol, value);
			}
			return context.lastResponse();
		}

	}

}