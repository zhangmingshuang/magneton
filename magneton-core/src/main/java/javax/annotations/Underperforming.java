package javax.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 表示存在一定的性能问题，性能较低下，可能会存在较长的调用时间.
 *
 * @author zhangmsh 2022/2/9
 * @since 1.2.0
 */
@Retention(RetentionPolicy.CLASS)
public @interface Underperforming {

}
