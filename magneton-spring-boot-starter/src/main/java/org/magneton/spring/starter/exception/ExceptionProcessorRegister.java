package org.magneton.spring.starter.exception;

import java.util.function.Function;

/**
 * expcetion processor register.
 *
 * @author zhangmsh
 * @since 2020/12/25
 * @see ExceptionProcessor
 */
public interface ExceptionProcessorRegister {

	/**
	 * add a expcetion handler.
	 * @param exception expcetion class. eg: {@code NullPointException.class}
	 * @param handler process handler.
	 * @param <E> T
	 * @param <R> T
	 */
	<E extends Exception, R> void addHandler(Class<E> exception, Function<E, R> handler);

}