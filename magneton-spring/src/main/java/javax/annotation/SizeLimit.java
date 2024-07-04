package javax.annotation;

import java.lang.annotation.*;

/**
 * .
 *
 * @author zhangmsh 2022/4/28
 * @since 2.0.8
 */
@Inherited
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface SizeLimit {

	int value();

}
