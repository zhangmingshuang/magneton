package javax.annotation.concurrent;

import java.lang.annotation.*;

/**
 * @author zhangmsh 2022/6/2
 * @since 1.0.0
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface MethodNotThreadSafe {

}
