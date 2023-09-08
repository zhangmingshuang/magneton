package javax.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author zhangmsh 2022/6/5
 * @since 1.0.0
 */
@Retention(RetentionPolicy.CLASS)
public @interface Description {

	String value();

}
