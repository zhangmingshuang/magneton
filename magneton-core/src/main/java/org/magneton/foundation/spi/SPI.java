package org.magneton.foundation.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个SPI标识.
 *
 * 用来标识一个类是通过SPI机制加载的。
 *
 * @author zhangmsh 2022/2/7
 * @since 1.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {

}
