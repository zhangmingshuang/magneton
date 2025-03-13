package javax.annotation;

import java.lang.annotation.*;

/**
 * Spring的Factories的SPI标识 .
 * <p>
 * 标记某个接口或者类采用的spring.factories配置加载的。
 *
 * @author zhangmsh
 * @since 2021/11/5
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpringFactories {

}