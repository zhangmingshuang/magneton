package org.magneton.framework.core.design.spring;

import com.google.common.base.Strings;
import org.magneton.framework.core.design.*;
import org.magneton.framework.core.design.chain.ChainBus;
import org.magneton.framework.core.design.chain.ChainNode;
import org.magneton.framework.core.design.processor.EventBusRegister;
import org.magneton.framework.core.design.scenario.BizScenario;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.OrderUtils;

/**
 * 后置处理器加载器
 *
 * @author zhangmsh
 * @since 2025.2
 */
public class DesignSpringLoader implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ChainNode) {
			return this.doChainNode(bean, beanName);
		}
		Strategy strategy = AnnotationUtils.getAnnotation(bean.getClass(), Strategy.class);
		if (strategy != null) {
			return this.doStrategy(bean, beanName, strategy);
		}
		EventBusComponent eventBusComponent = AnnotationUtils.getAnnotation(bean.getClass(), EventBusComponent.class);
		if (eventBusComponent != null) {
			return this.doEventBusComponent(bean, beanName, eventBusComponent);
		}
		return bean;
	}

	public Object doEventBusComponent(Object bean, String beanName, EventBusComponent eventBusComponent)
			throws BeansException {
		EventBusRegister.register(bean, eventBusComponent);
		return bean;
	}

	private Object doStrategy(Object bean, String beanName, Strategy strategy) {
		Class[] target = strategy.target();
		if (target.length == 0) {
			target = bean.getClass().getInterfaces();
		}
		if (target.length == 0) {
			throw new StrategyException(String.format("%s @Strategy#target未设置策略类,并且未实现任何接口", bean.getClass()));
		}
		for (Class targeClass : target) {
			BizScenario bizScenario = BizScenario.valueOf(strategy.bizId(), strategy.useCase(), strategy.scenario());
			StrategyRegistrar.register(targeClass, bean, bizScenario);
		}
		return bean;
	}

	private Object doChainNode(Object bean, String beanName) {
		Chain chainAnno = AnnotationUtils.getAnnotation(bean.getClass(), Chain.class);
		if (chainAnno != null && !Strings.isNullOrEmpty(chainAnno.chainId())) {
			String chainId = chainAnno.chainId();
			String nodeId = chainAnno.nodeId();
			org.magneton.framework.core.design.chain.Chain chain = ChainBus.getChain(chainId);
			if (chain == null) {
				ChainRegistrar.register(chainId);
				chain = ChainBus.getChain(chainId);
			}
			if (Strings.isNullOrEmpty(nodeId)) {
				chain.add((ChainNode) bean);
			}
			else {
				chain.add(nodeId, (ChainNode) bean);
			}
			chain.sort((o1, o2) -> OrderUtils.getOrder(o1.getClass(), Ordered.LOWEST_PRECEDENCE)
					- OrderUtils.getOrder(o2.getClass(), Ordered.LOWEST_PRECEDENCE));
		}
		ChainBus.addNode(beanName, (ChainNode) bean);
		return bean;
	}

}
