package org.magneton.spring.starter.modules.wechat.offiaccount;

import org.magneton.module.wechat.mp.core.handler.MpHandler;
import org.magneton.module.wechat.mp.core.handler.MpHandlerRegistrar;
import org.magneton.module.wechat.mp.core.router.MpDispatchProcessor;
import org.magneton.module.wechat.mp.core.router.MpDispatchProcessorRegistrar;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 微信公众号消息处理器后置处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class WechatMpHandlerPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof MpHandler) {
			MpHandlerRegistrar.register((MpHandler) bean);
		}
		if (bean instanceof MpDispatchProcessor) {
			MpDispatchProcessorRegistrar.register((MpDispatchProcessor) bean);
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

}