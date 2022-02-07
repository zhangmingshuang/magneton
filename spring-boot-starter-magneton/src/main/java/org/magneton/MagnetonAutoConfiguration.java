package org.magneton;

import org.magneton.properties.MagnetonProperties;
import org.magneton.spring.launcher.RunLauncherContextInitializer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2021/1/22
 */
@Configuration
@EnableConfigurationProperties(MagnetonProperties.class)
public class MagnetonAutoConfiguration {

	@Bean
	public RunLauncherContextInitializer runLauncherInitializer() {
		return RunLauncherContextInitializer.getInstance();
	}

}
