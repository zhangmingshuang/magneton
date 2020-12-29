package org.magneton.spring.exception;

/**
 * .
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/28
 */
@SuppressWarnings("ProhibitedExceptionDeclared")
public interface ExceptionProcessorContext extends ExceptionProcessorRegister {

  /**
   * exception handle.
   *
   * @param exception process expcetion.
   * @return process result.
   * @throws Exception if the expcetion has not any handler.
   */
  @SuppressWarnings("java:S112")
  Object handle(Exception exception) throws Exception;

  /**
   * register a expception processor to context.
   *
   * @param exceptionProcessor exception processor.
   */
  void registerExceptionProcessor(ExceptionProcessor exceptionProcessor);
}
