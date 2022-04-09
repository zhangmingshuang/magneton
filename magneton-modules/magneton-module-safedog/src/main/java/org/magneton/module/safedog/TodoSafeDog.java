package org.magneton.module.safedog;

/**
 * 行为验证安全狗.
 *
 * @author zhangmsh 09/04/2022
 * @since 2.0.8
 */
public interface TodoSafeDog<T> {

	boolean validate(T body);

}
