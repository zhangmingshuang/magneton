package org.magneton.spring.cache;

import com.google.common.base.Strings;
import org.magneton.spring.properties.MagnetonProperties;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * MCache的使用判断
 *
 * @author zhangmsh
 * @since 2024
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MCacheCondition implements Condition {

	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		Environment environment = conditionContext.getEnvironment();
		String property = environment.getProperty(MagnetonProperties.CACHE_STRATEGY);
		return !Strings.isNullOrEmpty(property) && !"false".equalsIgnoreCase(property);
	}

}
