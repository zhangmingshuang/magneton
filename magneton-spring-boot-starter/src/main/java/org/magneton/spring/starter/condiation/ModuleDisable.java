package org.magneton.spring.starter.condiation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 禁用模块
 * @author zhangmsh.
 * @since 2023.9
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleDisable {

	Modules[] value() default {};

}
