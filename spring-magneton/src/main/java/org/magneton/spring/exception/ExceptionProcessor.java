package org.magneton.spring.exception;

/**
 * customer a expcetion processor.
 *
 * <p>in default, the global exception advice process the {@code ResponseException} only which
 * transit to a {@code Response} message.
 *
 * <p>if want process other exception, use this {@code ExceptionProcessor} to register the specify
 * {@code Expcetion} processor.
 *
 * <p>to process the {@code NullPointException}:
 *
 * <pre>{@code
 * public class ExceptionProcessorImpl implements ExceptionProcessor {
 *
 * \@Override
 * public void registerExceptionProcessor(ExceptionProcessorRegister register) {
 *    register.addHandler(
 *      NullPointerException.class,
 *      e -> {
 *        return Response.exception().message(NULL_ERROR);
 *      });
 *    }
 * }
 * }</pre>
 *
 * @author zhangmsh
 * @version 1.0.0
 * @since 2020/12/25
 */
public interface ExceptionProcessor {

  /**
   * register the specify exception processor.
   *
   * @param register exception processor register.
   */
  void registerExceptionProcessor(ExceptionProcessorRegister register);
}
