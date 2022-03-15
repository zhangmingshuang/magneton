package org.magneton.support;

import cn.org.atool.fluent.mybatis.spring.MapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh 2022/2/15
 * @since 1.2.0
 */
@ComponentScan("org.magneton.support")
@MapperScan(basePackages = "org.magneton.support.*.mapper")
@EnableCaching
@Configuration
@Slf4j
public class SupportAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		log.info("--------support-auto-configuration----------");
	}

	@Bean
	public MapperFactory mapperFactory() {
		return new MapperFactory();
	}

}
