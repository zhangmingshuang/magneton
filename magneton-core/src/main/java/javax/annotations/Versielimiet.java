package javax.annotations;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * .
 *
 * @author zhangmsh 2022/2/11
 * @since 1.2.0
 */
@Retention(CLASS)
public @interface Versielimiet {

	String component();

	String version();

}
