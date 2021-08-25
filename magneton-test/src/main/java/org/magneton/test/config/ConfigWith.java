package org.magneton.test.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.magneton.test.annotation.TestComponent;
import org.magneton.test.core.InjectType;

/**
 * .
 *
 * @author zhangmsh 2021/8/19
 * @since 2.0.0
 */
@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@TestComponent
public @interface ConfigWith {

  InjectType value();
}
