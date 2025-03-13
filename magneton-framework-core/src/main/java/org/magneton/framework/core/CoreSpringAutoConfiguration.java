package org.magneton.framework.core;

import org.magneton.framework.core.design.spring.DesignSpringLoader;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring自动配置.
 *
 * @author zhangmsh
 * @since 2024
 */
@Configuration(proxyBeanMethods = false)
public class CoreSpringAutoConfiguration {

	@Bean
	public BeanPostProcessor designSpringLoader() {
		return new DesignSpringLoader();
	}

}
