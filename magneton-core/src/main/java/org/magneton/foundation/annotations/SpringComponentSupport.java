package org.magneton.foundation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用来标识一个接口可以被SpringBoot的组件扫描到.
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SpringComponentSupport {

}
