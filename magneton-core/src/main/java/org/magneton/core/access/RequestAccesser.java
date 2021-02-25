package org.magneton.core.access;

import java.util.function.Supplier;

/**
 * .
 *
 * @author zhangmsh 2021/2/25
 * @since 4.0.0
 */
public interface RequestAccesser<T> extends Accesser {

  Accessible access(String key, Supplier<Boolean> supplier);
}
