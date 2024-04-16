package org.magneton.spring.starter.modules.wechat.offiaccount;

import org.magneton.foundation.exception.DuplicateFoundException;
import org.magneton.module.wechat.offiaccount.handler.MessageHandler;
import org.magneton.module.wechat.offiaccount.router.MessageRouterRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 微信公众号消息处理器后置处理器.
 *
 * @author zhangmsh.
 * @since 2024
 */
public class WechatOffiaccountMessageHandlerPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof MessageHandler) {
			MessageHandler exist = MessageRouterRegistry.registerMsgRoute((MessageHandler) bean);
			if (exist != null) {
				throw new DuplicateFoundException(bean, exist);
			}
		}
		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

}