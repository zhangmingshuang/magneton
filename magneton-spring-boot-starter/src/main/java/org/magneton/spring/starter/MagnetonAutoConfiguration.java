package org.magneton.spring.starter;

import org.magneton.spring.starter.properties.MagnetonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Magneton Auto Configuration.
 *
 * @author zhangmsh
 * @since 2021/1/22
 */
@ComponentScan
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MagnetonProperties.class)
public class MagnetonAutoConfiguration {

	// @Bean
	// public RunLauncherContextInitializer runLauncherInitializer() {
	// return RunLauncherContextInitializer.getInstance();
	// }

}