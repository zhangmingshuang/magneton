package org.magneton.support.adapter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Indexed;

/**
 * @author zhangmsh 2022/4/9
 * @since 1.0.0
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ExcludeInterceptor {

	/**
	 * 排除的作用拦截器
	 * @return 拦截器
	 */
	Class<? extends InterceptorAdapter>[] value() default {};

}
