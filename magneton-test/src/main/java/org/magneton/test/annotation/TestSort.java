package org.magneton.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序，值越小越优先.
 *
 * @author zhangmsh 2021/8/2
 * @since
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestSort {

	/**
	 * Useful constant for the highest precedence value.
	 *
	 * <p>
	 * See Also: Integer.MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 *
	 * @see java.lang.Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

	/**
	 * 排序值，值越大越晚执行
	 * @return 排序值
	 */
	int value() default LOWEST_PRECEDENCE;

}
