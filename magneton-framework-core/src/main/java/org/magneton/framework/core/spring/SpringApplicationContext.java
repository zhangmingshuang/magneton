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

package org.magneton.framework.core.spring;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * spring 工具类
 *
 * @author zhangmsh
 * @since 2024
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext context = null;

	public static <T> T getBean(Class<T> clazz) {
		valid();
		return context.getBean(clazz);
	}

	public static <T> T getBean(String beanId) {
		valid();
		return (T) context.getBean(beanId);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		valid();
		return context.getBean(beanName, clazz);
	}

	public static ApplicationContext getContext() {
		valid();
		return context;
	}

	public static void publishEvent(ApplicationEvent event) {
		valid();
		try {
			context.publishEvent(event);
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	public static void valid() {
		Preconditions.checkNotNull(context, "Spring未初始化完成，请在Spring初始化完成之后调用");
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringApplicationContext.context = context;
	}

}