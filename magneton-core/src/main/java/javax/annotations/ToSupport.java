package javax.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 待支持.
 *
 * @author zhangmsh 2022/2/15
 * @since 1.2.0
 */
@Retention(RetentionPolicy.CLASS)
public @interface ToSupport {

	/**
	 * 即将在哪个版本支持
	 * @return 支持的版本
	 */
	String value();

}
