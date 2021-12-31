package org.magneton.test.test.config;

import org.magneton.test.test.parser.Definition;

/**
 * 配置增强处理器.
 *
 * @author zhangmsh 2021/8/23
 * @since 2.0.0
 */
public interface ConfigPostProcessor {

	default void beforeConfig(Config config, Definition definition) {
	}

	default void afterConfig(Object result, Config config, Definition definition) {
	}

}
