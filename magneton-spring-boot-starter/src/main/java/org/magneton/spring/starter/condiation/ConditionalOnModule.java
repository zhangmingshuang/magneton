package org.magneton.spring.starter.condiation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模块条件.
 * @author zhangmsh.
 * @since 2023.9
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Conditional(OnModuleCondition.class)
public @interface ConditionalOnModule {

	Modules[] value() default {};

}
