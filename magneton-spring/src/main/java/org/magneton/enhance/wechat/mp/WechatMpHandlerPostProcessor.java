package org.magneton.enhance.wechat.mp;

import org.magneton.enhance.wechat.mp.core.message.handler.MpHandler;
import org.magneton.enhance.wechat.mp.core.message.handler.MpHandlerRegistrar;
import org.magneton.enhance.wechat.mp.core.router.MpDispatchProcessor;
import org.magneton.enhance.wechat.mp.core.router.MpDispatchProcessorRegistrar;
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