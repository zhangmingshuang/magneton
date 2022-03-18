package org.magneton.spring.starter.exception;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/28
 */
public interface ExceptionProcessorContext extends ExceptionProcessorRegister {

	/**
	 * exception handle.
	 * @param exception process expcetion.
	 * @return process result.
	 * @throws Exception if the expcetion has not any handler.
	 */
	Object handle(Exception exception) throws Exception;

	/**
	 * register a expception processor to context.
	 * @param exceptionProcessor exception processor.
	 * @throws DuplicateProcessorException if duplication exception type is set.
	 */
	void registerExceptionProcessor(ExceptionProcessor exceptionProcessor);

}
