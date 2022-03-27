package org.magneton.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh 2022/2/15
 * @since 1.2.0
 */
@ComponentScan("org.magneton.support")
@EnableCaching
@Configuration
@Slf4j
public class SupportAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		log.info("--------support-auto-configuration----------");
	}

}
