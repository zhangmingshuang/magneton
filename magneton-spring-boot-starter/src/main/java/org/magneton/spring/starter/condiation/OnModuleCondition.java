package org.magneton.spring.starter.condiation;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 模块条件.
 *
 * @author zhangmsh.
 * @since 2023.9
 * @see ConditionalOnModule
 */
public class OnModuleCondition implements ConfigurationCondition {

	@Override
	public ConfigurationPhase getConfigurationPhase() {
		return ConfigurationPhase.PARSE_CONFIGURATION;
	}

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return false;
	}

}
