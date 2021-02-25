package org.magneton;

import org.magneton.properties.MagnetonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
public class MagnetonAutoConfiguration {}
