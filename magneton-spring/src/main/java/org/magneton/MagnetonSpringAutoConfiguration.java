package org.magneton;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring自动配置器.
 *
 * @author zhangmsh
 * @since 2024
 */
@ComponentScan
@Configuration(proxyBeanMethods = false)
public class MagnetonSpringAutoConfiguration {

}
