package org.magneton.spring.starter;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.magneton.spring.starter.core.PropertyDesResolver;
import org.magneton.spring.starter.properties.MagnetonProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
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

	/**
	 * 配置信息解码处理器
	 * @return EncryptablePropertyResolver
	 * @since zhongjinbin 20210916
	 */
	@ConditionalOnMissingBean
	@ConditionalOnClass(EncryptablePropertyResolver.class)
	@Bean(name = "encryptablePropertyResolver")
	public EncryptablePropertyResolver encryptablePropertyResolver() {
		return new EncryptablePropertyResolver() {
			private final PropertyDesResolver propertyDesResolver = new PropertyDesResolver();

			@Override
			public String resolvePropertyValue(String s) {
				return this.propertyDesResolver.resolvePropertyValue(s);
			}
		};
	}

}